package com.stockstrategyanalyzer.job.onestocktest;

import java.util.ArrayList;
import java.util.List;

import com.stockstrategy.tools.Utils;
import com.stockstrategyanalyzer.result.ITaskResult;


public class OneStockTestResult implements ITaskResult{
	private long jobId; // used to find the job in db
	private String startDate;
	private String endDate;
	private String strategy;
	private String stockCode;
	private float gain;
	private float lost;
	
	private List<OneStockTestDetailItem> detailItems;
	
	public OneStockTestResult(long jobId){
		this.jobId = jobId;
		this.detailItems = new ArrayList<OneStockTestDetailItem>();
	}
	
	public OneStockTestResult(long jobId, String stockCode, String strategy, String startDate, String endDate, float gain, float lost){
		this.jobId = jobId;
		this.stockCode = stockCode;
		this.strategy = strategy;
		this.startDate = startDate;
		this.endDate = endDate;
		this.gain = gain;
		this.lost = lost;
		this.detailItems = new ArrayList<OneStockTestDetailItem>();
	}
	
	public void addDetailItem(OneStockTestDetailItem item){
		this.detailItems.add(item);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("One Stock Job Result for job with id: " + this.jobId+"\n");
		for (OneStockTestDetailItem item:detailItems){
			sb.append(item.toString()).append('\n');
		}
		sb.append("Stock:"+stockCode+"  Strategy:"+strategy+"  startdate="+startDate+"  endDate="+endDate);
		sb.append("Gain="+Utils.format3(gain)+"  lost="+Utils.format3(lost));
		return sb.toString();
	}

	public long getJobId() {
		return jobId;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getStrategy() {
		return strategy;
	}

	public String getStockCode() {
		return stockCode;
	}

	public float getGain() {
		return gain;
	}

	public float getLost() {
		return lost;
	}

	public List<OneStockTestDetailItem> getDetailItems() {
		return detailItems;
	}

	public OneStockTestResult setStartDate(String startDate) {
		this.startDate = startDate;
		return this;
	}

	public OneStockTestResult setEndDate(String endDate) {
		this.endDate = endDate;
		return this;
	}

	public OneStockTestResult setStrategy(String strategy) {
		this.strategy = strategy;
		return this;
	}

	public OneStockTestResult setStockCode(String stockCode) {
		this.stockCode = stockCode;
		return this;
	}

	public OneStockTestResult setGain(float gain) {
		this.gain = gain;
		return this;
	}

	public OneStockTestResult setLost(float lost) {
		this.lost = lost;
		return this;
	}

	
}
