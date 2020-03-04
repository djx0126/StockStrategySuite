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
public class Stestd_5 extends AbstractStrategyStatisticData {
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

	private static String[] pool = {"000815", "002455", "002489", "000019", "002233", "600776", "601777", "600800", "000913", "601890", "600338", "600586", "002025", "600118", "002623", "002133", "002295", "300391", "600178", "600779", "002220", "300257", "002410", "600775", "300122", "600385", "600608", "002163", "002156", "000561", "600560", "600711", "300103", "000752", "300062", "000026", "600190", "600156", "600077", "600230", "600894", "300346", "000619", "300395", "600131", "600605", "002054", "600088", "600038", "600282", "600735", "600543", "002086", "600888", "002258", "002125", "000987", "601339", "000036", "000570", "603555", "000738", "002336", "002637", "002088", "300231", "600505", "601678", "002033", "002490", "300270", "002651", "002079", "002482", "002457", "600470", "002381", "002498", "600523", "600080", "600444", "002105", "002275", "600289", "000548", "000930", "002130", "300029", "002154", "002287", "002323", "601012", "600137", "300263", "600478", "601177", "600356", "000565", "600396", "603088", "600685", "002400", "600985", "600635", "300275", "002268", "002222", "002031", "000666", "002731", "002671", "002665", "000713", "601333", "600366", "000017", "002216", "601998", "002734", "300251", "002621", "603126", "002243", "600305", "002058", "000828", "600668", "002661", "600592", "002465", "000537", "600392", "002277", "600979", "300256", "300401", "600854", "002149", "603018", "000993", "000417", "600337", "002418", "600278", "002114", "002468", "002399", "002082", "600161", "600715", "600268", "600892", "600420", "600079", "002217"};
	private static Set<String> stockPool = Arrays.stream(pool).collect(Collectors.toSet());


	public Stestd_5() {
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