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
public class Steste extends AbstractStrategyStatisticData {
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

	public Steste() {
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
			DataArray dea = dataMap.getDataArray(Constant.MACDDEA);
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
					for (int j = i - 2; j > 2; j--) {
						if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
							lastDeathCrossK = j;
							break;
						}
					}

					int lastGoldenCross = -1;
					double maxDif = 0;
					for (int j = lastDeathCrossK - 2; lastDeathCrossK > 20 && j > 2; j--) {
						if (dif.getValue(j) > maxDif) {
							maxDif = dif.getValue(j);
						}

						if (macd.getValue(j) > 0 && macd.getValue(j - 1) < 0) {
							lastGoldenCross = j;
							break;
						}
					}

					if (lastDeathCrossK > 0 && lastGoldenCross > 0) {
						if (i - lastGoldenCross <60 && i - lastGoldenCross > 12
						) {
							tobuy = true;

							int nextDeathCross = -1;
							for (int j = i+1 ; j<close.size()-GAIN;j++)
							{
								if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
									nextDeathCross = j;
									break;
								}
							}

							if (nextDeathCross > 0) {
								double gain = (close.getValue(nextDeathCross) - close.getValue(i)) * 100/close.getValue(i);
//								System.out.printf("%s %s %.2f dif=%.3f d_sc=%d maxDif=%.3f d_bc=%d dif_bc=%.3f\n", close.getDate(i), stockCode, gain, dif.getValue(i), i - lastDeathCrossK, maxDif, i- lastGoldenCross, dif.getValue(lastGoldenCross));
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