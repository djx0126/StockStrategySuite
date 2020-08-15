package com.stockstrategyanalyzer.job.findtobuy;

import java.util.HashMap;
import java.util.Map;

import com.stockstrategy.tools.Utils;

public class StrategyFindResult {

	private String strategy;
	
	private Map<String, Float> stockGainMap;
	
	public StrategyFindResult(String strategy){

		this.strategy = strategy;
		stockGainMap = new HashMap<String, Float>();
	}
	
	public void addStockWithGain(String stockCode, float gain){
		stockGainMap.put(stockCode, gain);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(">>>  " + strategy+" ("+String.valueOf(stockGainMap.size())+"): \n");

		int i=0;
		for (String stockCode:stockGainMap.keySet()){
			float gain = stockGainMap.get(stockCode);
			String stockGainText = Utils.format3(gain) +" %";
			sb.append(stockCode+": "+stockGainText+" / ");
			if ((i+1)%5==0) {
				sb.append("\n");
			}
			i++;
		}
		
		if ((stockGainMap.size())%5 != 0){
			sb.append("\n");
		}
		
		return sb.toString();
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public Map<String, Float> getStockGainMap() {
		return stockGainMap;
	}

	
}
