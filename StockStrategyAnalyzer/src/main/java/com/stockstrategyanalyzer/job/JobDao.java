package com.stockstrategyanalyzer.job;


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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.stockstrategy.tools.Utils;

@Component
public class JobDao {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	
	public int deleteJob(long jobId){
		String sql = "delete from jobs where id = "+jobId;
		return jdbc.update(sql);
	}
	
	public Job createJob(int type, JobParam jobParam){
		int newJobState= JobState.NEW;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "insert into jobs(type, state, creationDate, startDate, endDate, strategy, stockCode) values(?,?,?,?,?,?,?)";
		jdbc.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, type);
				ps.setInt(2, newJobState);
				ps.setString(3, Utils.today());
				ps.setString(4, jobParam.getStartDate());
				ps.setString(5, jobParam.getEndDate());
				ps.setString(6, jobParam.getStrategy());
				ps.setString(7, jobParam.getStockCode());
				return ps;
			}
		}, keyHolder);
		
		long id = keyHolder.getKey().longValue();
		Job newJob = new Job(id);
		newJob.setType(type);
		newJob.setParam(jobParam);
		return newJob;
	}
	
	public Job getJob(long id){
		String sql = "select id,type,state,creationDate, startDate, endDate, strategy, stockCode from jobs where id=?";
		Job job = new Job(id);
		JobParam jobParam = new JobParam();
		jdbc.query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setLong(1, id);
				return ps;
			}
		}, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				int type = rs.getInt(2);
				int state = rs.getInt(3);
				String creationDate = rs.getString(4);
				String startDate = rs.getString(5);
				String endDate = rs.getString(6);
				String strategy = rs.getString(7);
				String stockCode = rs.getString(8);
				
				job.setType(type);
				job.setState(state);
				job.setCreationDate(creationDate);
				jobParam.setStartDate(startDate).setEndDate(endDate).setStrategy(strategy).setStockCode(stockCode);
				job.setParam(jobParam);
			}
		});
		return job;
	}
	
	public List<Job> getJobs(){
		String sql = "select id,type,state,creationDate, startDate, endDate, strategy, stockCode  from jobs";
		List<Job> jobs = jdbc.query(sql, new RowMapper<Job>(){

			@Override
			public Job mapRow(ResultSet rs, int rowNum) throws SQLException {
				int id = rs.getInt(1);
				int type = rs.getInt(2);
				int state = rs.getInt(3);
				String creationDate = rs.getString(4);
				String startDate = rs.getString(5);
				String endDate = rs.getString(6);
				String strategy = rs.getString(7);
				String stockCode = rs.getString(8);
				
				Job job = new Job(id);
				job.setType(type);
				job.setState(state);
				job.setCreationDate(creationDate);
				
				JobParam jobParam = new JobParam();
				jobParam.setStartDate(startDate).setEndDate(endDate).setStrategy(strategy).setStockCode(stockCode);
				job.setParam(jobParam);
				return job;
			}});
		
		return jobs;
	}
	
	public void setJobState(long jobId, int state){
		String sql = "update jobs set state=? where id=?";
		jdbc.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, state);
				ps.setLong(2, jobId);
				return ps;
			}
		});
	}
}
