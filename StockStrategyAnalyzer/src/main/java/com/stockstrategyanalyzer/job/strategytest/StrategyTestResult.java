package com.stockstrategyanalyzer.job.strategytest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.stockstrategyanalyzer.result.ITaskResult;

public class StrategyTestResult implements ITaskResult {
	private long jobId;
	private Map<String, OneStrategyResult> strategyResultMap;
	private Map<String, OneStrategyResultDetail> strategyResultDetailMap;
	
	
	public StrategyTestResult(long jobId){
		this.jobId = jobId;
		this.strategyResultMap = new ConcurrentHashMap<>();
		this.strategyResultDetailMap = new ConcurrentHashMap<>();
	}
	
	public void addStrategyResult(OneStrategyResult strategyResult){
		strategyResultMap.put(strategyResult.getStrategy(), strategyResult);
	}
	
	public void addStrategyResultDetail(OneStrategyResultDetail strategyResultDetail){
		strategyResultDetailMap.put(strategyResultDetail.getStrategy(), strategyResultDetail);
	}
	
	

	public long getJobId() {
		return jobId;
	}

	public Map<String, OneStrategyResult> getStrategyResultMap() {
		return strategyResultMap;
	}
	
	public Map<String, OneStrategyResultDetail> getStrategyResultDetailMap() {
		return strategyResultDetailMap;
	}
}
