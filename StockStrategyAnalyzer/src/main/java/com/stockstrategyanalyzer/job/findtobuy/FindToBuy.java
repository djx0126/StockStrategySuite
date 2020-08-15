package com.stockstrategyanalyzer.job.findtobuy;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.stockstrategy.example.panel.tester.StockFinder;
import com.stockstrategy.example.panel.tester.StockStrategyTester;
import com.stockstrategy.simulator.DataAnalyzer;
import com.stockstrategy.simulator.aggregate.AbstractAggregate;
import com.stockstrategy.simulator.aggregate.AggregateMgr;
import com.stockstrategy.simulator.aggregate.BuySellDetailCollector;
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

	private FindResult toSell;
	private AbstractAggregate buySellDetailCollector = new BuySellDetailCollector();
	
	public FindToBuy(Job job, JobParam jobParam) {
		this.jobId = job.getId();
		this.endDate = jobParam.getEndDate();
	}

	@Override
	public ITaskResult runTask() {
		result = new FindResult(jobId, endDate);
		toSell = new FindResult(jobId, endDate);
		
		String startDate = getStartDateByEndDate(endDate);
		
//		this.init(startDate, endDate);
//		this.run();
//		this.resultInit();
//		this.buildFindResult();

		this.init(startDate, endDate);
		AggregateMgr.getInstance().addAggregate(this.buySellDetailCollector);
		this.run();
		this.resultInit();
		this.buildFindResult();
		buildSellResult();

		AggregateMgr.getInstance().releaseAggregate(this.buySellDetailCollector);

		System.out.println("TO SELL >>>>>>>>>>>>>>>>>>>>>");
		System.out.println(toSell);
		System.out.println("TO SELL <<<<<<<<<<<<<<<<<<<<<");
		
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
//					if (j<NUMSTOCKSPRINT){
						String stock = stockList.get(j);
						StockStatisticResult stockResult = StatisticResultManager.getInstance().getResult(stock);
						StatisticResult statisticResult = stockResult.getResult(statisticType);
						gainList.add(statisticResult.getGain());
//					}else {
//						break;
//					}
				}
//				for (int j =0 ;j<NUMSTOCKSPRINT; j++){
				for (int j =0 ;j<stockList.size(); j++){
//					if (j < stockList.size()){
						String stock = stockList.get(j);
						float gain = gainList.get(j);
						
						strategyFindResult.addStockWithGain(stock, gain);
//					}
				}
				result.addStrategyFindResult(strategyFindResult);
			}
		}
//		System.out.println(result);
	}

	private void buildSellResult() {
		Map<String, List<BuySellDetailCollector.Transaction>> statisticsTransactionMap = (Map<String, List<BuySellDetailCollector.Transaction>>) buySellDetailCollector.getResult();
		for (String statisticType : statisticsTransactionMap.keySet()) {
			List<BuySellDetailCollector.Transaction> transactions = statisticsTransactionMap.get(statisticType);
			for (BuySellDetailCollector.Transaction t : transactions) {
				if (t.getSellDate().equals(this.endDate) && !t.isInHand() ) {
					if (!toSell.getStrategyFindResults().containsKey(statisticType)) {
						StrategyFindResult strategyFindResult = new StrategyFindResult(statisticType);
						toSell.addStrategyFindResult(strategyFindResult);
					}
					StrategyFindResult strategyFindResult = toSell.getStrategyFindResults().get(statisticType);
					float gain = (float) (100.0 * (t.getSellPrice() - t.getBuyPrice()) / t.getBuyPrice());
					strategyFindResult.addStockWithGain(t.getStockCode(), gain);
				}
			}
		}
	}

}
