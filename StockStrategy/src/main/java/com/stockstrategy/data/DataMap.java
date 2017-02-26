/**
 * 
 */
package com.stockstrategy.data;

import java.util.*;

/**
 * @author Administrator
 *	hold a map<String statisticType,DataArray statisticDataArray> for one stockcode
 * a huge map that can include all raw_data and statistics_data (in array list) 
 */
public class DataMap implements IStock {
	private String stockCode;
	private Map<String, DataArray> dataMap = null;
	
	public DataMap(String _code)
	{
		this.stockCode = _code;
		dataMap = new HashMap<>();
	}
	
	@Override
	public String getStockCode() {
		return stockCode;
	}

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public boolean containArray(String statisticType)
	{
		return dataMap.containsKey(statisticType);
	}

	public void putDataArray(String statisticType, DataArray statisticDataArray)
	{
		dataMap.put(statisticType, statisticDataArray);
	}
	
	public DataArray getDataArray(String statisticType) throws Exception
	{
		if (containArray(statisticType))
		{
			return dataMap.get(statisticType);
		}else
		{
			throw new Exception("The Statistic Type doesn't exist");
		}
	}

	public Map<String, DataArray> getDataMap() {
		return dataMap;
	}

    public void setDataMap(Map<String, DataArray> dataMap) {
        this.dataMap = dataMap;
    }
}
