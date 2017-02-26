package com.stockstrategyanalyzer.job.onestocktest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class OneStockTestResultDao {
	@Autowired
	private JdbcTemplate jdbc;
	
	public int deleteResultByJobId(long jobId){
		String detailSql = "delete from onestocktestresultdetail where jobId = ?";
		jdbc.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(detailSql);
				ps.setLong(1, jobId);
				return ps;
			}
		});
		
		String sql = "delete from onestocktestresult where jobId = ?";
		int linesDeleted = jdbc.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setLong(1, jobId);
				return ps;
			}
		});
		return linesDeleted;
	}
	
	public OneStockTestResult getResultByJobId(long jobId){
		OneStockTestResult result = new OneStockTestResult(jobId);
		String sql = "select stockCode, strategy, startDate, endDate, gain, lost from onestocktestresult where jobId = ?";
		
		jdbc.query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setLong(1, jobId);
				return ps;
			}
		}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				String stockCode = rs.getString(1);
				String strategy = rs.getString(2);
				String startDate = rs.getString(3);
				String endDate = rs.getString(4);
				float gain = rs.getFloat(5);
				float lost = rs.getFloat(6);
				result.setStockCode(stockCode).setStrategy(strategy).setStartDate(startDate).setEndDate(endDate).setGain(gain).setLost(lost);
				
			}
		});
		
		String detailSql = "select date, buySellCode, price from onestocktestresultdetail where jobId = ? order by date";
		jdbc.query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(detailSql);
				ps.setLong(1, jobId);
				return ps;
			}
		}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				String date = rs.getString(1);
				double buySellCode = rs.getFloat(2);
				double price = rs.getFloat(3);
				OneStockTestDetailItem item = new OneStockTestDetailItem(jobId, date, buySellCode, price);
				result.addDetailItem(item);
			}
		});
		
		return result;
	}
	
	public void saveResult(OneStockTestResult result){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "insert into onestocktestresult(jobId, stockCode, strategy, startDate, endDate, gain, lost) values(?,?,?,?,?,?,?)";
		jdbc.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, result.getJobId());
				ps.setString(2, result.getStrategy());
				ps.setString(3, result.getStockCode());
				ps.setString(4, result.getStartDate());
				ps.setString(5, result.getEndDate());
				ps.setFloat(6, result.getGain());
				ps.setFloat(7, result.getLost());
				return ps;
			}
		}, keyHolder);
		
//		long id = keyHolder.getKey().longValue();
		
		String detailSql = "insert into onestocktestresultdetail(jobId, date, buySellCode, price) values(?,?,?,?)";
		List<OneStockTestDetailItem> detailItems = result.getDetailItems();
		for (OneStockTestDetailItem item:detailItems){
			jdbc.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(detailSql);
					ps.setLong(1, result.getJobId());
					ps.setString(2, item.getDate());
					ps.setFloat(3, (float) item.getBuySellCode());
					ps.setFloat(4, (float) item.getPrice());
					return ps;
				}
			});
		}
	}
}
