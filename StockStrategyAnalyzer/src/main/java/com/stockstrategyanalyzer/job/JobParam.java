package com.stockstrategyanalyzer.job;

public class JobParam {
	private String startDate;
	private String endDate;
	private String strategy;
	private String stockCode;
	
	public JobParam(){}
	
	public String getStartDate() {
		return startDate;
	}
	public JobParam setStartDate(String startDate) {
		this.startDate = startDate;
		return this;
	}
	public String getEndDate() {
		return endDate;
	}
	public JobParam setEndDate(String endDate) {
		this.endDate = endDate;
		return this;
	}
	public String getStrategy() {
		return strategy;
	}
	public JobParam setStrategy(String strategy) {
		this.strategy = strategy;
		return this;
	}
	public String getStockCode() {
		return stockCode;
	}
	public JobParam setStockCode(String stockCode) {
		this.stockCode = stockCode;
		return this;
	}
}
