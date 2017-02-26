package com.djx.stockgainanalyzer;

import com.djx.stockgainanalyzer.data.IStockGain;
import com.djx.stockgainanalyzer.data.StockGainData;

import java.util.List;

public class StockDateSelector extends AbstractDataSelector{
	private String keyDate;
	
	public StockDateSelector(List<? extends IStockGain> data, String date){
		super(data);
		this.keyDate = date;
	}
	
	
	
	public boolean isStockSelected(IStockGain data){
		String stockKeyDate = data.getKeyDate();
		if (stockKeyDate.compareTo(keyDate)<0){
			return true;
		}else{
			return false;
		}
	}
}
