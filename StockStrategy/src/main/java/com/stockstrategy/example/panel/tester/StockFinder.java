package com.stockstrategy.example.panel.tester;

import java.util.LinkedList;
import java.util.List;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.file.StockLister;
import com.stockstrategy.simulator.DataAnalyzer;
import com.stockstrategy.simulator.SimulatorFinder;
import com.stockstrategy.simulator.aggregate.AbstractAggregate;
import com.stockstrategy.simulator.aggregate.AggregateMgr;
import com.stockstrategy.simulator.aggregate.BuyCounter;
import com.stockstrategy.statistic.data.Stestb;
import com.stockstrategy.statistic.result.StatisticResult;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategy.statistic.result.StockStatisticResult;
import com.stockstrategy.tools.Utils;

public class StockFinder extends SimulatorFinder {
	protected static final int yearsIntoCount = 1;
	protected static final int monthsIntoCount = -20;
	protected static final String STARTDATE = "20070101";
	protected final static int NUMSTOCKSPRINT = 100;
	
	//protected AbstractAggregate buyCounterSBreak = new BuyCounter(Stestb.AGGREGATORNAME,Constant.SBreak);
	
	protected static String getStartDateByEndDate(String endDate){
		String startDate = String.valueOf(Integer.parseInt(endDate.substring(0, 4))-yearsIntoCount)+endDate.substring(4);
		startDate = Utils.addMonth(endDate, monthsIntoCount);
		return startDate;
	}
	
	public static void doFind(String endDate){
		String startDate = getStartDateByEndDate(endDate);
		//startDate = STARTDATE;
		doFind(startDate, endDate);
	}
	
	public static void doFind(String startDate, String endDate){
		StockFinder app = new StockFinder();
		//AggregateMgr.getInstance().resetAggregates();
		//AggregateMgr.getInstance().addAggregate(app.buyCounterSBreak);
//		app.resultList = StockStrategyTester.doTest(useBlkList, startDate, endDate);
		//app.buyCounterSBreak.deActive();
		app.init(startDate, endDate);
		app.run();
		app.resultInit();
		app.printDetailResult();

		//AggregateMgr.getInstance().releaseAggregate(app.buyCounterSBreak);
		
	}
	

	
	
	protected void printDetailResult(){
		System.out.println("===detail result=== " + resultList.size());
		for (int i =0 ;i<resultList.size(); i++){
			if (i<resultList.size()){
				String statisticType = resultList.get(i);
				System.out.println(statisticType+": ");
				
				List<String> stockList = null; //sorted on statistic's gain, String statistic Type
				LinkedList<Float> gainList = null;
				
				stockList = DataAnalyzer.sortStock(statisticType, NUMSTOCKSPRINT);
				gainList = new LinkedList<Float>();
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
				String gainText = "";
				for (int j =0 ;j<NUMSTOCKSPRINT; j++){
					if (j < stockList.size()){
						String stock = stockList.get(j);
						float gain = gainList.get(j);
						String stockGainText = Utils.format3(gain) +" %";
						gainText += stock+": "+stockGainText+" / ";
						if ((j+1)%5==0) {
							gainText += "\n";
						}
					}
				}
				System.out.println(String.valueOf(stockList.size())+" +++ "+gainText);
			}
		}
	}
	
	
		

}
