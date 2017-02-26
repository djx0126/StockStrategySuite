package com.djx.stockgainanalyzer;

import com.djx.stockgainanalyzer.data.IStockGain;

import java.util.List;

public interface IStockSelector {
	boolean isStockSelected(IStockGain data); // true: train data, false: test data
	List<IStockGain> getValidateDataList();
	List<IStockGain> getTrainDataList();
}
