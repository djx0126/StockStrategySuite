package com.stockstrategyanalyzer.transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.stockstrategy.simulator.aggregate.BuySellDetailCollector.Transaction;

@Component
public class TransactionDao {
	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Transaction> getTransactions(long jobId, String strategy){
		String strategyResulttTransactionQuerySql = "select stockCode, buyDate, sellDate, buyPrice, sellPrice from strategytesttransaction where jobId = "+jobId +" and strategy='"+strategy+"' order by buyDate asc";
		
		List<Transaction> transactions = jdbc.query(strategyResulttTransactionQuerySql, new RowMapper<Transaction>(){

			@Override
			public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
				String stockCode = rs.getString(1);
				String buyDate = rs.getString(2);
				String sellDate = rs.getString(3);
				double buyPrice = rs.getFloat(4);
				double sellPrice = rs.getFloat(5);
				Transaction trans = new Transaction(stockCode, strategy, buyDate, sellDate, buyPrice, sellPrice);
				return trans;
			}});
		return transactions;
	}

	public void saveTransactions(long jobId, String strategy, List<Transaction> transactions) {
		String sqlString = "insert into strategytesttransaction(jobId, strategy, stockCode, buyDate, sellDate, buyPrice, sellPrice) values(?,?,?,?,?,?,?)";
		jdbc.batchUpdate(sqlString, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Transaction trans = transactions.get(i);
				ps.setLong(1, jobId);
				ps.setString(2, strategy);
				ps.setString(3, trans.getStockCode());
				ps.setString(4, trans.getBuyDate());
				ps.setString(5, trans.getSellDate());
				ps.setFloat(6, (float) trans.getBuyPrice());
				ps.setFloat(7, (float) trans.getSellPrice());
			}

			@Override
			public int getBatchSize() {
				return transactions.size();
			}
		});
	}

	public int deleteTransactionsByJobId(long jobId){
		String transactionsResultDeleteSql = "delete from strategytesttransaction where jobId = "+jobId;
		return jdbc.update(transactionsResultDeleteSql);
	}
}
