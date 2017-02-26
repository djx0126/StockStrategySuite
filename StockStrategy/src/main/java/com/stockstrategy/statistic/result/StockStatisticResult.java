/**
 * 
 */
package com.stockstrategy.statistic.result;

import java.util.HashMap;

/**
 * @author Administrator
 * hold a set of statistic result of one stock, key is statisticType
 */
public class StockStatisticResult {
	private String stockCode = null;
	private HashMap<String, StatisticResult> results = null; //String statisticType, 
	
	
	public StatisticResult  getResult(String statisticType){
		if (contains(statisticType)){
			return results.get(statisticType);
		}else{
			return null;
		}
	}
	
	public void add(String statisticType, StatisticResult result){
		results.put(statisticType, result);
	}
	
	public boolean contains(String statisticType){
		return results.containsKey(statisticType);
	}
	
	public StockStatisticResult(String stockCode){
		this.stockCode = stockCode;
		results = new HashMap<String, StatisticResult>();
	}
	
	public HashMap<String, StatisticResult> getResults(){
		return results;
	}
	
	public String getStockCode(){
		return stockCode;
	}
}
