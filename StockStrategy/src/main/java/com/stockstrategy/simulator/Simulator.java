package com.stockstrategy.simulator;

import java.util.List;

import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.SharedStockDataHolder;
import com.stockstrategy.data.StockDataHolder;
import com.stockstrategy.file.RemoteDataReader;
import com.stockstrategy.file.StockLister;



public class Simulator {
	//private static SimulatorApp myApp = null;
	//private String stockCode = null;
	//private String startDate = null;
	//private String endDate = null;
	
	
	
	
	
	
	
	public static void actualExecute(String stockCode, String startDate, String endDate){
		DataMap dataMap = prepareData(stockCode, startDate, endDate);
		if (dataMap==null){return;}
		
		//calculate by strategy
		DataAnalyzer.calCustomStatics(stockCode, dataMap);//calculate customer statistic data
		
		//save data
		StockDataHolder.getInstance().put(stockCode, dataMap);
		
		//calculate the gain for the stock
		DataAnalyzer.analyze(stockCode);
	}
	
	public static void actualExecuteWithStatistic(String stockCode, String startDate, String endDate){
		DataMap dataMap = prepareData(stockCode, startDate, endDate);
		if (dataMap==null){return;}
		
		//calculate by strategy
		DataAnalyzer.calCustomStatics(stockCode, dataMap);//calculate customer statistic data
		
		//save data
		StockDataHolder.getInstance().put(stockCode, dataMap);
		
		//calculate the gain for the stock to buy
		DataAnalyzer.findAnalyze(stockCode, endDate);
	}
	
	public static DataMap prepareData(String stockCode, String startDate, String endDate){
		//fetch data
		RemoteDataReader dfr = new RemoteDataReader(stockCode, startDate, endDate);
		DataMap dataMap= dfr.readDataMap();//fetch raw data;
		if (dataMap == null){return null;}
		//save data
		StockDataHolder.getInstance().put(stockCode, dataMap);
				
				
		//Analyze data
		DataAnalyzer.calBasicStatics(stockCode, dataMap);//calculate basic statistic data
		
		//Analyze data
		DataAnalyzer.calAdvancedStatics(stockCode, dataMap);//calculate basic Advanced data
		
		return dataMap;
	}
	
	public static void prepareSharedStockData(String startDate, String endDate){
		List<String> sharedStockList = StockLister.getSharedStockList();
		for (String stockCode:sharedStockList){
			//fetch data
			RemoteDataReader dfr = new RemoteDataReader(stockCode, startDate, endDate);
			DataMap dataMap= dfr.readDataMap();//fetch raw data;
			
			if (dataMap != null){
				//calculate by strategy
				DataAnalyzer.calBasicStatics(stockCode, dataMap);//calculate customer statistic data

				//save data
				SharedStockDataHolder.getInstance().put(stockCode, dataMap);
			}
		}
	}
	
}
