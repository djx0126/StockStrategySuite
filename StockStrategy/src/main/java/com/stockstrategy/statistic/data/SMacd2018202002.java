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
public class SMacd2018202002 extends AbstractStrategyStatisticData {
	/*
	 * (non-Javadoc)
	 *
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
	 * java.lang.String, com.stock.data.DataMap)
	 */
	private int GAIN = 5;
	private static String myStatisticType = Constant.SMacd2018202002;
	private static String START_DATE = "20200301";

	private static String[] pool = {"000063", "000401", "000652", "000877", "000990", "002028", "002118", "002124", "002291", "002382", "002395", "002434", "002458", "002570", "300114", "300169", "300220", "300316", "300326", "300390", "300430", "300446", "300450", "300527", "300555", "300685", "300711", "600072", "600118", "600218", "600234", "600425", "600517", "600536", "600602", "600613", "600677", "600729", "600892", "603060", "603108", "603383", "603669"};
	private static Set<String> stockPool = Arrays.stream(pool).collect(Collectors.toSet());


	public SMacd2018202002() {
		super(myStatisticType);
	}

	@Override
	public String getStartDate() {
		return START_DATE;
	}

	@Override
	public boolean isAvgByDay() {
		return true;
	}

	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
									DataMap dataMap) {
		DataArray statisticArray = null;
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray open = dataMap.getDataArray(Constant.OPEN);
			DataArray macd = dataMap.getDataArray(Constant.MACD);
			DataArray ma20 = dataMap.getDataArray(Constant.MA20);
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			int start = 0;
			int count = 0;
			for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
			}

			if (!stockPool.contains(stockCode)) {
				return statisticArray;
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

				if (macd.getValue(i) > 0 && macd.getValue(i - 1) < 0) {
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