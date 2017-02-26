package com.djx.stockgainanalyzer;

import com.djx.stockgainanalyzer.data.IStockGain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StockRandomSelector extends AbstractDataSelector{
	private static double DEFAULT_RATE = 0.7d;
	
	private double selectRate = 0.7d;
	private Map<String, Boolean> stockSelected = new HashMap<>();

	public StockRandomSelector(List<? extends IStockGain> data){
		this(data, DEFAULT_RATE);
	}
	
	public StockRandomSelector(List<? extends IStockGain> data, double rate){
		super(data);
		this.selectRate = rate;
		randomSelectStock();
	}
	
	private void randomSelectStock(){
		
		Random random = new Random(System.currentTimeMillis());
    	for (int i=0;i<this.allData.size();i++){
			IStockGain stockGainData = this.allData.get(i);
    		if (!stockSelected.containsKey(stockGainData.getStockCode())){
    			if (random.nextDouble()<selectRate){
    				stockSelected.put(stockGainData.getStockCode(), true);
        		}else{
        			stockSelected.put(stockGainData.getStockCode(), false);
        		}
    		}
    	}
	}
	
	
	
	public boolean isStockSelected(IStockGain data){
		String stockCode = data.getStockCode();
		
		if (stockSelected.containsKey(stockCode) && stockSelected.get(stockCode)){
			return true;
		}else{
			return false;
		}
	}
	
}
