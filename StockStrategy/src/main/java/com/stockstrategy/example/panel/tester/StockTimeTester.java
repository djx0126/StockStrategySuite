package com.stockstrategy.example.panel.tester;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategy.tools.Utils;

@Deprecated
public class StockTimeTester extends StockStrategyTester {
	protected static ArrayList rate;
	protected static ArrayList gain;
	protected static ArrayList toBuy;
	protected static Map<String, List> stockRateResult;
	protected static Map<String, List> stockGainResult;
	protected static Map<String, List> stockToBuyResult;
	protected static final String RATEFILENAME="RateResults.txt";
	protected static final String GAINFILENAME="GainResults.txt";
	protected static final String TOBUYFILENAME="ToBuyResults.txt";
	protected static final String testDates[]={"20110101","20110201","20110301","20110401","20110501","20110601","20110701","20110801","20110901","20111001","20111101","20111201"};
	protected static final int MONTHSTOTEST = -12;
	/*
	public static void main(String[] args){
		//doTest();
		System.out.println("To Start");
		System.out.println("End>>>");
	}
	*/
	public static void doTest(){
		initResultMap();
		for (String endDate : testDates){
			String startDate = Utils.addMonth(endDate, MONTHSTOTEST);
			doTest( startDate,endDate);
		}
		writeResults();
	}
	
	protected static void writeResults() {
		try {
			writeResults(RATEFILENAME,  stockRateResult);
			writeResults(GAINFILENAME,  stockGainResult);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected static void writeResults(String fileName,  Map resultsMap) throws IOException{
		File resultFile = new File(Constant.MGMT_DIR+Constant.SP+fileName);
		if(!resultFile.exists()){
			resultFile.createNewFile();
		}
		FileWriter resultFileWriter = new FileWriter(resultFile);
		Date date = new Date();
		resultFileWriter.append("\n  "+date  );
		resultFileWriter.append("\ndate");
		for (String endDate:testDates){
			resultFileWriter.append(" "+endDate);
		}
		
		for(String strategy:Constant.CUSTOMSTATISTICSTYPES){
			resultFileWriter.append("\n"+strategy);
			List strategyResult = (List) resultsMap.get(strategy);
			for (int i=0;i<strategyResult.size();i++){
				String result = (String) strategyResult.get(i);
				resultFileWriter.append(" "+result);
			}
		}
		
		resultFileWriter.close();
	}
	
	@Override
	protected  void printResult() {
		for (int i =0 ;i<resultList.size(); i++){
			if (i<resultList.size()){
				String statisticType = resultList.get(i);
				float meanGain = meanGainList.get(i);
				float allLost = allLostList.get(i);
				
				String gainText = Utils.format3(meanGain) +"%";
				String allLostText = Utils.format3(allLost) +"%";
				
				int allGainCounter = 0;
				int allLostCounter = 0;
				for (String stock : StatisticResultManager.getInstance().getStatisticResults().keySet()){
					int gain = (int)StatisticResultManager.getInstance().getResult(stock).getResult(statisticType).getGain();
					if (gain > 100f){
						allGainCounter++;
					}else if (gain <100f){
						allLostCounter++;
					}
				}
				
				float netGain = allNetGainList.get(i);
				float rate = netGain/allLost;
				String rateText = Utils.format3(rate);
				
				stockRateResult.get(statisticType).add(rateText);
				stockGainResult.get(statisticType).add(Utils.format3(meanGain));
				//stockGainResult.get(statisticType).add(Utils.format3(meanGain));
			}
		}
	}
	
	
	public static LinkedList<String> doTest(String startDate, String endDate){
		StockTimeTester app = new StockTimeTester();
		app.init(startDate, endDate);
		app.run();
		app.resultInit();
		//app.printDetailResult();
		app.printResult();
		return null;
	}
	
	protected static void initResultMap(){
		stockRateResult = new HashMap<String, List>();
		stockGainResult = new HashMap<String, List>();
		stockToBuyResult = new HashMap<String, List>();
		for (String strategy: Constant.CUSTOMSTATISTICSTYPES){
			stockRateResult.put(strategy, new ArrayList());
			stockGainResult.put(strategy, new ArrayList());
			stockToBuyResult.put(strategy, new ArrayList());
		}
	}
	
}
