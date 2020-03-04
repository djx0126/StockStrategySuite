/**
 *
 */
package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import android.graphics.Paint;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;
import com.stockstrategy.data.SharedStockDataHolder;

/**
 * @author Administrator
 *
 *         when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 ,
 *         tigger sell , set value to -1
 *
 *
 *
 */
public class Stesta_5 extends AbstractStrategyStatisticData {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
	 * java.lang.String, com.stock.data.DataMap)
	 */
	private int PREVIOUS = 10;
	private int GAIN = 5;
	private final double LIMIT = PREVIOUS;
	private static String myStatisticType = Constant.SPre30Gain5I;
	private static String START_DATE = "20140301";



	public Stesta_5() {
		super(myStatisticType);
	}


	public int getPrevious(){
		return this.PREVIOUS;
	}
	public int getGain(){
		return this.GAIN;
	}
	public double getLimit(){
		return this.LIMIT;
	}


	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
									DataMap dataMap) {
		DataArray statisticArray = null;
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray open = dataMap.getDataArray(Constant.OPEN);
			DataArray macd = dataMap.getDataArray(Constant.MACD);
			DataArray macddif = dataMap.getDataArray(Constant.MACDDIF);
			DataArray macddea = dataMap.getDataArray(Constant.MACDDEA);
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			int start = 0;
			int count = 0;
			for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
			}

			for (int i = 0; i < close.size(); i++) {

				if (i < 1) {
					continue;
				}

				if (Math.abs(close.getValue(i) - open.getValue(i)) > open.getValue(i) * 0.1f
						|| Math.abs(close.getValue(i) - close.getValue(i - 1)) > close.getValue(i - 1) * 0.15) {
					start = i;
					count = 0;
					continue;
				}
				boolean tobuy = false;

				if (macd.getValue(i) > 0.01 && macd.getValue(i - 1) < -0.01) {
					tobuy = true;
				}

				if (tobuy){
					statisticArray.setValue(i, 1);
				}

			}
			//sell: cross ma5 ma10
			for (int i = 0 ; i<close.size()-GAIN;i++)
			{
				if (i < 1) {
					continue;
				}

				if (macd.getValue(i) < 0 && macd.getValue(i - 1) > 0) {
					statisticArray.setValue(i, -1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statisticArray;
	}

}
