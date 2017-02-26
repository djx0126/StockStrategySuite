package com.stockstrategy.statistic.data;

import java.util.HashMap;
import java.util.Set;

/**
 * @author Administrator
 * hold statistics dataType, including basic/advanced statisticData, and customStatisticsData as well
 */
public class StatisticManager {
	private static StatisticManager mStatisticManager = null;
	private HashMap<String, AbstractStatisticData> statistics = null;
	
	public AbstractStatisticData getStatistic(String type){
		if (contains(type))
		{
			return statistics.get(type);
		}else{
			return null;
		}
		
	}
	
	public HashMap<String, AbstractStatisticData> getStatistics(){
		return statistics;
	}
	
	public Set<String> getStatisticTypes(){
		return statistics.keySet();
	}
	
	public synchronized void clear(){
		statistics.clear();
	}
	
	public synchronized void putStatistic(String type, AbstractStatisticData statistic){
		if (!statistics.containsKey(type))
		{
			statistics.put(type, statistic);
		}
	}
	
	public synchronized boolean contains(String type)
	{
		//if (type!=null){
		//	return statistics.containsKey(type);
		//}else {
		//	return false;
		//}
		
		return (type!=null) && statistics.containsKey(type);
	}
	
	public static synchronized StatisticManager getInstance(){
		if (mStatisticManager ==null)
		{
			mStatisticManager = new StatisticManager();
		}
		return mStatisticManager;
	}
	private StatisticManager(){
		statistics = new HashMap<String, AbstractStatisticData>();
	}
}
