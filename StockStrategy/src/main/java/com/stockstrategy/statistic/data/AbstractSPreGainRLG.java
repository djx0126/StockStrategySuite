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

/**
 * @author Administrator
 * 
 *         when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 ,
 *         tigger sell , set value to -1
 * 
 * 
 * 
 */
public abstract class AbstractSPreGainRLG extends AbstractStrategyStatisticData {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
	 * java.lang.String, com.stock.data.DataMap)
	 */
	private int PREVIOUS = 30;
	private int GAIN = 5;
	private final double LIMIT = PREVIOUS;
	private String myStatisticType = Constant.SPre30Gain5I;

	private double[] closeOffset;
	private double[] volOffset;
	private double[] openOffset;
	private double[] highOffset;
	private double[] lowOffset;
	private double theta0;
	private double H_THREHOLD;

	public AbstractSPreGainRLG(String myStatisticType, int pre, int gain,
			double[] closeOffset, double[] closeOpenOffset,
			double[] volOffset, double[] openOffset,
			double[] highOffset, double[] lowOffset, double theta0, double H_THREHOLD
			) {
		super(myStatisticType);
		this.myStatisticType = myStatisticType;
		this.PREVIOUS = pre;
		this.GAIN = gain;
		this.closeOffset = closeOffset;
		this.volOffset = volOffset;
		this.openOffset = openOffset;
		this.highOffset = highOffset;
		this.lowOffset = lowOffset;
		this.theta0 = theta0;
		this.H_THREHOLD = H_THREHOLD;
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
	
	public double[][] getOffsetParams(){
		double[][] result = new double[5][];
		result[0] = this.closeOffset;
		result[1] = this.openOffset;
		result[2] = this.highOffset;
		result[3] = this.lowOffset;
		result[4] = this.volOffset;
		return result;
	}
	

	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray statisticArray = null;
		DataArray open = null;
		DataArray close = null;
		DataArray high = null;
		DataArray low = null;
		DataArray vol = null;
		if (!dataMap.containArray(Constant.MA5)
				|| !dataMap.containArray(Constant.MA10)) {
			return null;
		}
		try {
			open = dataMap.getDataArray(Constant.OPEN);
			close = dataMap.getDataArray(Constant.CLOSE);
			high = dataMap.getDataArray(Constant.HIGH);
			low = dataMap.getDataArray(Constant.LOW);
			vol = dataMap.getDataArray(Constant.VOL);

			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);

			for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
			}

			// buy: 1.cross ma5 ma10
			// * 2.ma10 < ma30
			// * 3.VolMa20 < VolMa60

			for (int i = 0; i < close.size(); i++) {
				statisticArray.setValue(i, 0);

				boolean tobuy = false;

				if (i < 1) {
					continue;
				}

				if (Math.abs(close.getValue(i) - open.getValue(i)) > open
						.getValue(i) * 0.1f
						|| Math.abs(close.getValue(i) - close.getValue(i - 1)) > close
								.getValue(i - 1) * 0.15) {
					continue;
				}
				double[] closeList = new double[PREVIOUS];
				double[] volList = new double[PREVIOUS];
				double[] openList = new double[PREVIOUS];
				double[] highList = new double[PREVIOUS];
				double[] lowList = new double[PREVIOUS];
				double[][] valueLists = new double[5][];
				valueLists[0] = closeList;
				valueLists[1] = openList;
				valueLists[2] = highList;
				valueLists[3] = lowList;
				valueLists[4] = volList;

				for (int j = i, k = 0; j > 0 && k < PREVIOUS; j--, k++) {
					double c = close.getValue(j) / close.getValue(j - 1);
					double v = vol.getValue(j) / vol.getValue(j - 1);
					double o = open.getValue(j) / open.getValue(j - 1);
					double h = high.getValue(j) / high.getValue(j - 1);
					double l = low.getValue(j) / low.getValue(j - 1);

					closeList[k] = Math.log10(c);
					volList[k] = Math.log10(v);
					openList[k] = Math.log10(o);
					highList[k] = Math.log10(h);
					lowList[k] = Math.log10(l);
					// System.out.println(" " + offset + " price:" + price +
					// ", vol=" + v + ", closeOpen" + day);

				}
				double value = 0;
				
				value = getThetaX(valueLists, getOffsetParams(), theta0);
				
				value = sigmod(value);
				
				if (value > this.H_THREHOLD) {
					tobuy = true;
				}

				if (tobuy) {
					statisticArray.setValue(i, 1);
				}

			}
			// sell: cross ma5 ma10
			int gainLeft = -1;
			for (int i = 0; i < close.size(); i++) {
				if (gainLeft > 0) {
					gainLeft--;
				}
				if (statisticArray.getValue(i) > 0) {
					gainLeft = GAIN;
				}
				if (gainLeft == 0) {
					gainLeft = -1;
					statisticArray.setValue(i, -1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return statisticArray;
	}
	
	private static double getThetaX(double[][] valueLists, double[][] params, double theta0){
		double result = 0 ;
		for (int i=0;i<valueLists.length;i++){
			for (int j = 0;j<valueLists[i].length;j++){
				result += valueLists[i][j] * params[i][j];
			}
		}
		result += theta0;
		return result;
	}
	
	private static double sigmod(double x){
    	return 1.0d/(1.0d+Math.exp(-x));
    }

}
