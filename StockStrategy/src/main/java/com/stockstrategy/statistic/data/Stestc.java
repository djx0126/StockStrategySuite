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
public class Stestc extends AbstractStrategyStatisticData {
		/*
		 * (non-Javadoc)
		 *
		 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
		 * java.lang.String, com.stock.data.DataMap)
		 */
		private int PREVIOUS = 10;
		private int GAIN = 5;
		private final double LIMIT = PREVIOUS;
		private static String myStatisticType = Constant.Stestb;
		private static String START_DATE = "20140301";

//		private static String[] pool = {"000690", "002427", "002465", "002723", "300219", "600305", "600547", "600619", "601890"};
//		private static String[] pool = {"000690", "002042", "000403", "002723", "300219", "002414"};
		private static String[] pool = {"002184", "002194", "002760", "600130", "002543", "600128", "300157", "603022", "600635", "002414", "002214", "300101", "603200", "002568", "600278", "002306", "000690", "601519", "002865", "000576", "600368", "002063", "002195", "603660", "603699", "002124", "002451", "002803", "601177", "300232", "601113", "600141", "002614", "600522", "002230", "002268", "002281", "000538", "000813", "603383", "600740", "300059", "600436", "603228", "300316"};
		private static Set<String> stockPool = Arrays.stream(pool).collect(Collectors.toSet());


		public Stestc() {
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