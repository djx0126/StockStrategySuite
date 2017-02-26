package com.stockstrategy.data;

import java.util.*;

import com.stockstrategy.constant.Constant;




/**
 * @author Administrator
 *	class for a arraylist for the specified data ---- a pair of value: a index and a Data;
 *
 *  hold the name for the data(statistic data's name) as a String;
 */
public class DataArray implements IStock {
	protected DataMap dataMap;
	
	protected String stockCode;
	
	protected String statisticType;
	//private int index = 0;
	protected ArrayList<IData> dataList = null;
	
	protected Map<String, Integer> dateIndexMap;
	
	protected String buyStatisticType;
	protected String sellStatisticType;
	
	public void setBuyStatisticType(String buyStatisticType) {
		this.buyStatisticType = buyStatisticType;
	}
	
	public String getBuyStatisticType() {
		return buyStatisticType;
	}
	
	public void setSellStatisticType(String sellStatisticType) {
		this.sellStatisticType = sellStatisticType;
	}
	
	public String getSellStatisticType() {
		return sellStatisticType;
	}

	public DataArray(String stockCode, String statisticType, DataMap dataMap)
	{
		this(stockCode, statisticType, dataMap, Constant.CLOSE);
	}
	
	public DataArray(String stockCode, String statisticType, DataMap dataMap, String buySellStatisticType)
	{
		this(stockCode, statisticType, dataMap, buySellStatisticType, buySellStatisticType);
	}
	
	public DataArray(String stockCode, String statisticType, DataMap dataMap, String buyStatisticType, String sellStatisticType)
	{
		this.dataMap = dataMap;
		this.stockCode = stockCode;
		this.statisticType = statisticType;
		dataList = new ArrayList<>();
		dateIndexMap = new HashMap<>();
		this.buyStatisticType = buyStatisticType;
		this.sellStatisticType = sellStatisticType;
	}
	
	public int size()
	{
		return dataList.size(); 
	}
	
	@Override
	public String getStockCode() {
		return stockCode;
	}

	public String getStatisticType() {
		return statisticType;
	}

	public void setStatisticType(String statisticType) {
		this.statisticType = statisticType;
	}

	public void addData(IData d)
	{
		dateIndexMap.put(d.getDate(), dataList.size());
		dataList.add(d);
	}
	
	public void addRawData(String date, double value)
	{
		dateIndexMap.put(date, dataList.size());
		dataList.add(new RawData( date, value));
	}
	
	public double getValue(int i) throws Exception  
	{
		if (i < 0 || i >= dataList.size())
		{
			throw new Exception("beyond the index of dataList, stockCode="+stockCode+" index="+i +"  size="+dataList.size());
		}
		return dataList.get(i).getValue();
	}
	
	public void setValue(int i, double value) throws Exception  
	{
		if (i < 0 && i >= dataList.size())
		{
			throw new Exception("beyond the index of dataList");
		}
		dataList.get(i).setValue(value);
	}
	
	public String getDate(int i) throws Exception
	{
		if (i < 0 && i >= dataList.size())
		{
			throw new Exception("beyond the index of dataList");
		}
		return dataList.get(i).getDate();
	}
	
	public int getIndexByDate(String date){
		if(dateIndexMap.containsKey(date)){
			return dateIndexMap.get(date);
		}else{
			return -1;
		} 
	}
	
	
	
	
}
