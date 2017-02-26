package com.stockstrategyanalyzer.job.findtobuy;

import java.util.HashMap;
import java.util.Map;

import com.stockstrategyanalyzer.result.ITaskResult;

public class FindResult implements ITaskResult {
	private long jobId;
	private String endDate;
	
	private Map<String, StrategyFindResult> strategyFindResultMap;
	
	public FindResult(long jobId, String endDate){
		this.jobId = jobId;
		this.endDate = endDate;
		this.strategyFindResultMap = new HashMap<String, StrategyFindResult>();
	}
	
	public void addStrategyFindResult(StrategyFindResult result){
		this.strategyFindResultMap.put(result.getStrategy(), result);
	}
	
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("===detail result=== " + strategyFindResultMap.size()+"\n");
		for(StrategyFindResult strategyFindResult: strategyFindResultMap.values()){
			sb.append(strategyFindResult.toString());
		}
		return sb.toString();
	}

	public long getJobId() {
		return jobId;
	}

	public Map<String, StrategyFindResult> getStrategyFindResults() {
		return strategyFindResultMap;
	}

	public String getEndDate() {
		return endDate;
	}

}
