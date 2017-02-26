package com.stockstrategyanalyzer.job.strategytest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stockstrategy.tools.Utils;

public class OneStrategyResultDetail {
private String strategy;
	
	private List<Map<String, Float>> gainList;
	private List<Map<String, Float>> lostList;
	
	public OneStrategyResultDetail(String strategy){
		this.strategy = strategy;
		
		this.gainList = new ArrayList<Map<String,Float>>();
		this.lostList = new ArrayList<Map<String,Float>>();
	}
	
	public void addToGainList(String stockCode, float gain){
		addStockGainLost(gainList, stockCode, gain);
	}
	
	public void addToLostList(String stockCode, float gain){
		addStockGainLost(lostList, stockCode, gain);
	}
	
	private void addStockGainLost(List<Map<String, Float>> list, String stockCode, float gain){
		Map<String, Float> stockGainPair = new HashMap<String, Float>();
		stockGainPair.put(stockCode, gain);
		list.add(stockGainPair);
	}

	public String getStrategy() {
		return strategy;
	}

	public List<Map<String, Float>> getGainList() {
		return gainList;
	}

	public List<Map<String, Float>> getLostList() {
		return lostList;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("+++ " );
		appendStockGainLostString(sb, gainList);
		
		sb.append("\n--- " );
		appendStockGainLostString(sb, lostList);
		
		return sb.toString();
	}
	
	private void appendStockGainLostString(StringBuilder sb, List<Map<String, Float>> list){
		int i=0;
		for(Map<String, Float> stockGainPair: list){
			for(String stockCode: stockGainPair.keySet()){
				float gain = stockGainPair.get(stockCode);
				String stockGainText = Utils.format3(gain) + " %";
				sb.append(stockCode + ": " + stockGainText + " / ");
				
                if ((++i) % 5 == 0) {
                    sb.append("\n");
                }
                
			}
		}
		if (i%5!=0){
			sb.append("\n");
		}
	}
	
}
