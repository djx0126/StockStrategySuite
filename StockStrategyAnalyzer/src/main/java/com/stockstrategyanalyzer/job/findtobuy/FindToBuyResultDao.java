package com.stockstrategyanalyzer.job.findtobuy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class FindToBuyResultDao {
	@Autowired
	private JdbcTemplate jdbc;
	
	public int deleteFindResultByJobId(long jobId){
		String sql = "delete from strategyfindresult where jobId = "+String.valueOf(jobId);
		return jdbc.update(sql);
	}
	
	public FindResult loadFindResultByJobId(long jobId){
		
		String endDateQuerySql = "select distinct enddate from jobs where id = "+jobId;
		String endDate = jdbc.queryForObject(endDateQuerySql, String.class);
		
		String strategyQuerySql = "select distinct strategy from strategyfindresult where jobId = "+jobId;
		
		List<String> strategyList = jdbc.query(strategyQuerySql, new RowMapper<String>(){

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}});
		
		
		
		FindResult findResult = new FindResult(jobId, endDate);

		strategyList.stream().forEach(strategy -> {
			StrategyFindResult strategyFindResult = new StrategyFindResult(strategy);
			findResult.addStrategyFindResult(strategyFindResult);

			String strategyDetailSql = "select stockCode, gain from strategyfindresult where jobId = "+jobId +" and strategy='"+strategy +"'";
			jdbc.query(strategyDetailSql, rs -> {
                String stockCode = rs.getString(1);
                float gain = rs.getFloat(2);
                strategyFindResult.addStockWithGain(stockCode, gain);
            });
		});

		return findResult;
	}
	
	public void saveFindResult(FindResult findResult){
		long jobId = findResult.getJobId();
		String endDate = findResult.getEndDate();
		Map<String, StrategyFindResult> strategyFindResults = findResult.getStrategyFindResults();
		String strategyFindSql = "insert into strategyfindresult(jobId, endDate, strategy, stockCode, gain) values(?,?,?,?,?)";
		for(StrategyFindResult strategyFindResult: strategyFindResults.values()){
			Map<String, Float> stockGainMap = strategyFindResult.getStockGainMap();
			
			for (String stockCode: stockGainMap.keySet()){
				float gain = stockGainMap.get(stockCode);
				jdbc.update(new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(strategyFindSql);
						ps.setLong(1, jobId);
						ps.setString(2, endDate);
						ps.setString(3, strategyFindResult.getStrategy());
						ps.setString(4, stockCode);
						ps.setFloat(5, gain);
						return ps;
					}
				});
			}
			
		}
	}
}
