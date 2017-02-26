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
public class StockDataHolder {
	private static StockDataHolder mStockDataHolder= null;
	
	private HashMap<String, DataMap> stockData = null;
	
	
	
	public synchronized void fetchStockData(String stockCode, String startDate, String endDate){
		
	}
	
	public synchronized void fetchStockData(String stockCode, String startDate){
		
	}
	
	public synchronized void fetchStockData(String stockCode){
		//new Thread(new Simulator(stockCode)).start();
	}
	
	public static synchronized StockDataHolder getInstance(){
		if (mStockDataHolder == null)
		{
			mStockDataHolder = new StockDataHolder();
		}
		return mStockDataHolder;
	}
	private StockDataHolder(){
		stockData = new HashMap<String, DataMap>();
	}
	
	public synchronized void clear(){
		stockData.clear();
	} 
	
	public synchronized void put(String stockCode,DataMap dataMap){
		stockData.put(stockCode, dataMap);
	}
	
	public synchronized void remove(String stockCode){
		if (stockData.containsKey(stockCode)){
			stockData.remove(stockCode);
		}
	}
	
	public synchronized Set<String> getStocks(){
		return stockData.keySet();
	}
	
	public synchronized DataMap get(String stockCode)
	{
		if (stockData.containsKey(stockCode))
		{
			return stockData.get(stockCode);
		}else
		{
			return null;
		}
	}
	
	public  boolean containStock(String stockCode){
		return stockData.containsKey(stockCode);
	}
}
