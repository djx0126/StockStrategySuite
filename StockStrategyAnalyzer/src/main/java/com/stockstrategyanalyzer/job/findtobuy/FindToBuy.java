package com.stockstrategyanalyzer.job.findtobuy;

import java.util.LinkedList;
import java.util.List;

import com.stockstrategy.example.panel.tester.StockFinder;
import com.stockstrategy.example.panel.tester.StockStrategyTester;
import com.stockstrategy.simulator.DataAnalyzer;
import com.stockstrategy.statistic.result.StatisticResult;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategy.statistic.result.StockStatisticResult;
import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobParam;
import com.stockstrategyanalyzer.result.ITaskResult;
import com.stockstrategyanalyzer.task.ITask;

public class FindToBuy extends StockFinder implements ITask {
	private long jobId; // used to find the job in db
	private String endDate;
	
	private FindResult result;
	
	public FindToBuy(Job job, JobParam jobParam) {
		this.jobId = job.getId();
		this.endDate = jobParam.getEndDate();
	}

	@Override
	public ITaskResult runTask() {
		result = new FindResult(jobId, endDate);
		
		String startDate = getStartDateByEndDate(endDate);
		
		this.init(startDate, endDate);
		this.run();
		this.resultInit();
		this.buildFindResult();
		
		return result;
	}
	
	
	
	protected void buildFindResult(){
		for (int i =0 ;i<resultList.size(); i++){
			if (i<resultList.size()){
				String statisticType = resultList.get(i);
				
				StrategyFindResult strategyFindResult = new StrategyFindResult(statisticType);
				
				List<String> stockList = null; //sorted on statistic's gain, String statistic Type
				LinkedList<Float> gainList = null;
				
				stockList = DataAnalyzer.sortStock(statisticType, NUMSTOCKSPRINT);
				gainList = new LinkedList<>();
				for (int j = 0;j<stockList.size();j++){
					if (j<NUMSTOCKSPRINT){
						String stock = stockList.get(j);
						StockStatisticResult stockResult = StatisticResultManager.getInstance().getResult(stock);
						StatisticResult statisticResult = stockResult.getResult(statisticType);
						gainList.add(statisticResult.getGain());
					}else {
						break;
					}
				}
				for (int j =0 ;j<NUMSTOCKSPRINT; j++){
					if (j < stockList.size()){
						String stock = stockList.get(j);
						float gain = gainList.get(j);
						
						strategyFindResult.addStockWithGain(stock, gain);
					}
				}
				result.addStrategyFindResult(strategyFindResult);
			}
		}
//		System.out.println(result);
	}

}
