/**
 * 
 */
package com.stockstrategy.statistic.data;

import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.statistic.result.StatisticResult;

import java.util.HashMap;

/**
 * @author Administrator
 *
 */
public abstract class AbstractStatisticData {
	private String type = null; // the type name of a statistic
	private String startDate = "20140701";
	
	
	public abstract DataArray generate(String stockCode, String statisticType, DataMap dataMap);

	public HashMap<String, StatisticResult> aggregate(HashMap<String, StatisticResult> stockResults){
		return stockResults;
	}
	
	public String getStartDate(){
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public AbstractStatisticData (String type)// the type name of a statistic
	{
		this.type = type;
	}
	public String getType(){
		if (type !=null){
			return type;
		}else{
			return "";
		}
	}
	
	protected double MIN(DataArray data, int startIdx, int endIdx) throws Exception{
		double min = data.getValue(endIdx);
		for (int i=endIdx;i>=startIdx;i--){
			if (data.getValue(i)<min){
				min = data.getValue(i);
			}
		}
		return min;
	}
	
	protected double MAX(DataArray data, int startIdx, int endIdx) throws Exception{
		double max = data.getValue(endIdx);
		for (int i=endIdx;i>=startIdx;i--){
			if (data.getValue(i)>max){
				max = data.getValue(i);
			}
		}
		return max;
	}
	
	protected double AVG(DataArray data, int startIdx, int endIdx) throws Exception{
		if (startIdx>endIdx){
			return 0;
		}
		double avg = 0;
		for (int i=endIdx;i>=startIdx;i--){
			avg+=data.getValue(i);
		}
		avg= avg/(endIdx-startIdx+1);
		return avg;
	}
	
}
