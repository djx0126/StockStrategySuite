package com.djx.stockgainanalyzer.data;

import java.util.List;

import com.djx.stockgainanalyzer.StockAnalyzer;
import com.djx.stockgainanalyzer.Utils;

public class CachedStockGainData {
	public static final int PREVIOUS_NUM = StockAnalyzer.PREVIOUS_NUM;
	public static final int GAIN_NUM = StockAnalyzer.GAIN_NUM;
	public static final Field[] PARAMS = StockAnalyzer.PARAMS;
	
	StockGainData data;
	List<PreviousData> pDatas;
	PreviousData[] pDataArr;
	
	double[][] cachedValue= new double[Field.LENGTH][PREVIOUS_NUM];
	double value ;
	
	public CachedStockGainData(StockGainData data, double[][] offsetParams, double[][] scaleParams){
		this.data = data;
		this.pDatas = data.getPreviousData();
		pDataArr = new PreviousData[pDatas.size()];
		for (int i = 0;i < pDataArr.length;i++){
			pDataArr[i] = pDatas.get(i);
		}
		
		for (int k = 0;k<cachedValue.length;k++){
			cachedValue[k] = new double[PREVIOUS_NUM];
		}
		List<PreviousData> pDatas = data.getPreviousData();
		value = 0.0d;
		for (int j=0;j<PREVIOUS_NUM;j++){
			PreviousData pData = pDatas.get(j);
			
			for (Field param: PARAMS){
				double temp =pData.getValue(param)+offsetParams[param.getIdx()][j];
				cachedValue[param.getIdx()][j] = temp*temp/(scaleParams[param.getIdx()][j]*scaleParams[param.getIdx()][j]);
				value += cachedValue[param.getIdx()][j];
			}
		}
	}

	
	public double getValueWithSquareScale(int fieldIdx, int preIdx, double scaleValue2, double offsetValue){
		PreviousData pData = pDataArr[preIdx];
		double temp = pData.getValue(Field.fields[fieldIdx])+offsetValue;
		temp = temp * temp / (scaleValue2);
		return value - cachedValue[fieldIdx][preIdx] + temp;
	}
	

	public void updateWithSqareScale(int fieldIdx, int preIdx, double scaleValue2, double offsetValue){
		PreviousData pData = pDataArr[preIdx];
		double temp = pData.getValue(Field.fields[fieldIdx])+offsetValue;
		temp = temp * temp / (scaleValue2);
		value = value - cachedValue[fieldIdx][preIdx] + temp;
		cachedValue[fieldIdx][preIdx] = temp;
	}
	
	public StockGainData getRawData(){
		return this.data;
	}
}
