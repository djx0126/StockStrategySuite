package com.stockstrategyanalyzer.job.onestocktest;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.StockDataHolder;
import com.stockstrategy.simulator.DataAnalyzer;
import com.stockstrategy.simulator.Simulator;
import com.stockstrategy.statistic.data.StatisticManager;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobParam;
import com.stockstrategyanalyzer.result.ITaskResult;
import com.stockstrategyanalyzer.task.ITask;

public class OneStockTestTask implements ITask{
	private long jobId; // used to find the job in db
	private String startDate;
	private String endDate;
	private String strategy;
	private String stockCode;
	
	public OneStockTestTask(Job job, JobParam jobParam){
		this.jobId = job.getId();
		this.startDate = jobParam.getStartDate();
		this.endDate = jobParam.getEndDate();
		this.strategy = jobParam.getStrategy();
		this.stockCode = jobParam.getStockCode();
	}

	@Override
	public ITaskResult runTask() {
		
		Simulator.prepareSharedStockData(startDate, endDate);
		Simulator.actualExecute(stockCode, startDate, endDate);
		DataAnalyzer.analyze(stockCode);
		
		float gain = StatisticResultManager.getInstance().getResult(stockCode).getResult(strategy).getGain();
		float lost = StatisticResultManager.getInstance().getResult(stockCode).getResult(strategy).getLost();
		OneStockTestResult taskResult = new OneStockTestResult(jobId, stockCode, strategy, startDate, endDate, gain, lost);
		
		try {
			DataArray buySellArray = StockDataHolder.getInstance().get(stockCode).getDataArray(strategy);
			DataArray close = StockDataHolder.getInstance().get(stockCode).getDataArray(Constant.CLOSE);
			for (int i=0;i<=buySellArray.size()-1;i++){
				String date = buySellArray.getDate(i);
				double actionCode = buySellArray.getValue(i);
				
				if (actionCode<0 || actionCode>0){
					OneStockTestDetailItem detailItem = new OneStockTestDetailItem(jobId, date, actionCode, close.getValue(i));
					taskResult.addDetailItem(detailItem);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(taskResult);
		
		StockDataHolder.getInstance().remove(stockCode);
		StatisticManager.getInstance().clear();
		StatisticResultManager.getInstance().clear();
		return taskResult;
	}
}
