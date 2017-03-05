/**
 * 
 */
package com.stockstrategy.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 *	hold all stocks data using a map<String stockCode, DataMap dataMap>
 *	usage: StockDataHolder.getInstance().put(stockCode, dataMap);
 */
public class StockDataHolder {
	private static StockDataHolder mStockDataHolder= null;
	
	private Map<String, DataMap> stockData = null;
	
	public static synchronized StockDataHolder getInstance(){
		if (mStockDataHolder == null)
		{
			mStockDataHolder = new StockDataHolder();
		}
		return mStockDataHolder;
	}
	private StockDataHolder(){
		stockData = new ConcurrentHashMap<>();
	}
	
	public void clear(){
		stockData.clear();
	} 
	
	public void put(String stockCode,DataMap dataMap){
		stockData.put(stockCode, dataMap);
	}
	
	public void remove(String stockCode){
		if (stockData.containsKey(stockCode)){
			stockData.remove(stockCode);
		}

	}
	
	public DataMap get(String stockCode)
	{
		return stockData.getOrDefault(stockCode, null);
	}
	
	public  boolean containStock(String stockCode){
		return stockData.containsKey(stockCode);
	}
}
