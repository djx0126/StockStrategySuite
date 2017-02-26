/**
 * 
 */
package com.stockstrategy.statistic.result;

import java.util.HashMap;

/**
 * @author Administrator
 * hold and manage all stocks statistic results;
 */
public class StatisticResultManager {
	private static StatisticResultManager mStatisticResultManager = null;
	private HashMap<String, StockStatisticResult> statisticResults = null; //String stock
	
	
	
	
	
	
	
	
	
	
	
	public StockStatisticResult  getResult(String stockCode){
		if (contains(stockCode)){
			return statisticResults.get(stockCode);
		}else{
			return null;
		}
	}
	
	public void add(String stockCode, StockStatisticResult result){
		statisticResults.put(stockCode, result);
	}
	
	public boolean contains(String stockCode){
		return statisticResults.containsKey(stockCode);
	}
	
	public HashMap<String, StockStatisticResult> getStatisticResults(){
		return statisticResults;
	}
	
	public int size(){
		return statisticResults.size();
	}
	
	public void clear(){
		statisticResults.clear();
	}
	
	public synchronized static StatisticResultManager getInstance(){
		if (mStatisticResultManager == null){
			mStatisticResultManager = new StatisticResultManager();
		}
		return mStatisticResultManager;
	}
	
	private StatisticResultManager(){
		statisticResults = new HashMap<String, StockStatisticResult>();
	}

}
