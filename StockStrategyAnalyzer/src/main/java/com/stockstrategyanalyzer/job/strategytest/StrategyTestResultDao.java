package com.stockstrategyanalyzer.job.strategytest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.stockstrategy.simulator.aggregate.BuySellDetailCollector.Transaction;
import com.stockstrategyanalyzer.transaction.TransactionDao;


@Component
public class StrategyTestResultDao {
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private TransactionDao transactionDao;
	
	public int deleteStrategyTestResultByJobId(long jobId){
		transactionDao.deleteTransactionsByJobId(jobId);
		String strategyResultDetailGainQuerySql = "delete from strategytestresultdetailgain where jobId = "+jobId;
		jdbc.update(strategyResultDetailGainQuerySql);
		String strategyResultDetailLostQuerySql = "delete from strategytestresultdetaillost where jobId = "+jobId;
		jdbc.update(strategyResultDetailLostQuerySql);
		String sql = "delete from strategytestresult where jobId = "+jobId;
		return jdbc.update(sql);
	}
	
	public StrategyTestResult loadStrategyTestResultByJobId(long jobId){
		
		boolean withDetail = false;
		return loadStrategyTestResultByJobId(jobId, withDetail);
	}
	
	public StrategyTestResult loadStrategyTestResultByJobId(long jobId, boolean withTransactions){
		StrategyTestResult strategyTestResult = new StrategyTestResult(jobId);
		
		String strategyQuerySql = "select distinct strategy from strategytestresult where jobId = "+jobId;
		
		List<String> strategyList = jdbc.query(strategyQuerySql, new RowMapper<String>(){

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}});
		
		for (String strategy: strategyList){
			OneStrategyResult strategyResult = new OneStrategyResult(strategy);
			strategyTestResult.addStrategyResult(strategyResult);

			String strategyResultQuerySql = "select meanGain, allLost, netGain, gainCounter, lostCounter, transactionGainCounter, transactionLostCounter, transactionGain, transactionLost from strategytestresult where jobId = "+jobId +" and strategy='"+strategy+"'";
			jdbc.query(strategyResultQuerySql, new RowCallbackHandler(){

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					float meanGain = rs.getFloat(1);
					float allLost = rs.getFloat(2);
					float netGain = rs.getFloat(3);
					int gainCounter = rs.getInt(4);
					int lostCounter = rs.getInt(5);
					int transactionGainCounter = rs.getInt(6);
					int transactionLostCounter = rs.getInt(7);
					float transactionGain = rs.getFloat(8);
					float transactionLost = rs.getFloat(9);
					strategyResult.setMeanGain(meanGain).setAllLost(allLost).setNetGain(netGain).setGainCounter(gainCounter).setLostCounter(lostCounter).setTransactionGainCounter(transactionGainCounter).setTransactionLostCounter(transactionLostCounter).setTransactionGain(transactionGain).setTransactionLost(transactionLost);
				}});

			// transactions
			if (withTransactions){
				List<Transaction> transactions = transactionDao.getTransactions(jobId, strategy);
				strategyResult.setTransactions(transactions);
			}
		}

		return strategyTestResult;
	}
	
	public void saveStrategyTestResult(StrategyTestResult strategyTestResult){
		long jobId = strategyTestResult.getJobId();
		Map<String, OneStrategyResult> strategyTestResultMap = strategyTestResult.getStrategyResultMap();
		String strategySql = "insert into strategytestresult(jobId, strategy, meanGain, allLost, netGain, gainCounter, lostCounter, transactionGainCounter, transactionLostCounter, transactionGain, transactionLost) values(?,?,?,?,?,?,?,?,?,?,?)";
		strategyTestResultMap.keySet().stream().parallel().forEach(strategy -> {
			OneStrategyResult strategyResult = strategyTestResultMap.get(strategy);
			jdbc.update(con -> {
                PreparedStatement ps = con.prepareStatement(strategySql);
                ps.setLong(1, jobId);
                ps.setString(2, strategy);
                ps.setFloat(3, strategyResult.getMeanGain());
                ps.setFloat(4, strategyResult.getAllLost());
                ps.setFloat(5, strategyResult.getNetGain());
                ps.setInt(6, strategyResult.getGainCounter());
                ps.setInt(7, strategyResult.getLostCounter());
                ps.setInt(8, strategyResult.getTransactionGainCounter());
                ps.setInt(9, strategyResult.getTransactionLostCounter());
                ps.setFloat(10, strategyResult.getTransactionGain());
                ps.setFloat(11, strategyResult.getTransactionLost());
                return ps;
            });

			List<Transaction> transactions = strategyResult.getTransactions();
			transactionDao.saveTransactions(jobId, strategy, transactions);
		});
	}
	

}
