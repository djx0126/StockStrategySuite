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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Administrator
 *
 *         when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 ,
 *         tigger sell , set value to -1
 *
 *
 *
 */
public class Stestd extends AbstractStrategyStatisticData {
	/*
	 * (non-Javadoc)
	 *
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
	 * java.lang.String, com.stock.data.DataMap)
	 */
	private int PREVIOUS = 10;
	private int GAIN = 5;
	private final double LIMIT = PREVIOUS;
	private static String myStatisticType = Constant.Steste;
	private static String START_DATE = "20140301";

	public Stestd() {
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
			DataArray dif = dataMap.getDataArray(Constant.MACDDIF);
			DataArray atr = dataMap.getDataArray(Constant.ATR);
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			int start = 0;
			int count = 0;
			for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
			}

			for (int i = 0; i < close.size(); i++) {

				if (i < 10) {
					continue;
				}

				if (close.getValue(i) - close.getValue(i - 1) > 0.09 * close.getValue(i - 1)) {
					continue;
				}

				if (Math.abs(close.getValue(i) - open.getValue(i)) > open.getValue(i) * 0.1f
						|| Math.abs(close.getValue(i) - close.getValue(i - 1)) > close.getValue(i - 1) * 0.15) {
					start = i;
					count = 0;
					continue;
				}
				boolean tobuy = false;

				if (macd.getValue(i) > 0 && macd.getValue(i - 1) < 0  ) {
					int lastDeathCrossK = -1;
					for (int j = i - 10; j > 2; j--) {
						if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
							lastDeathCrossK = j;
							break;
						}
					}

					boolean minorDeath = false;
					for (int j = i -1; j > 2 && j>i-9 ; j--) {
						if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
							minorDeath = true;
							break;
						}
					}

					int lastGoldenCross = -1;
					double maxDif = -100;
					for (int j = lastDeathCrossK - 2; lastDeathCrossK > 20 && j > 2; j--) {
						double dif1 = dif.getValue(j)/ Math.abs(atr.getValue(j));
						if (dif1 > maxDif) {
							maxDif = dif1;
						}

						if (macd.getValue(j) > 0 && macd.getValue(j - 1) < 0) {
							lastGoldenCross = j;
							break;
						}
					}

					if (lastDeathCrossK > 0 && lastGoldenCross > 0 && !minorDeath) {
						if (i - lastGoldenCross <60 && i - lastGoldenCross > 12
						) {
							double difValue = dif.getValue(i) / Math.abs(atr.getValue(i));
							double dif2Value = dif.getValue(lastGoldenCross) / Math.abs(atr.getValue(lastGoldenCross));
							if (difValue > -1.7919316196121167 && difValue< -0.51787771638634
									&& maxDif > -2.9202901143859785 && maxDif < -0.3895218154269262
									&& dif2Value> -0.443338738719806 && dif2Value < 0.4092168481486915) {
								tobuy = true;
							}
						}
					}
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