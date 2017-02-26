/**
 * 
 */
package com.stockstrategy.data;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Administrator
 *	hold all stocks data using a map<String stockCode, DataMap dataMap>
 *	usage: StockDataHolder.getInstance().put(stockCode, dataMap);
 */
public class SharedStockDataHolder {
	private static SharedStockDataHolder mStockDataHolder= new SharedStockDataHolder();
	
	private HashMap<String, DataMap> stockData = new HashMap<String, DataMap>();
	
	
		
	public static SharedStockDataHolder getInstance(){
		
		return mStockDataHolder;
	}
	
	
	public synchronized void clear(){
		stockData.clear();
	} 
	
	public synchronized void put(String stockCode,DataMap dataMap){
		if (stockData.containsKey(stockCode)){
			stockData.remove(stockCode);
		}
		stockData.put(stockCode, dataMap);
	}
	
	
	public synchronized Set<String> getStocks(){
		return stockData.keySet();
	}
	
	public DataMap get(String stockCode)
	{
		if (stockData.containsKey(stockCode))
		{
			return stockData.get(stockCode);
		}else
		{
			return null;
		}
	}
	
	public boolean containStock(String stockCode){
		return stockData.containsKey(stockCode);
	}
}
