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
import com.stockstrategy.file.StockLister;
import com.stockstrategy.tools.Utils;

import java.util.Arrays;

/**
 * @author Administrator
 * 
 *         when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 ,
 *         tigger sell , set value to -1
 * 
 * 
 * 
 */
public abstract class AbstractSPreGain extends AbstractStrategyStatisticData {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
	 * java.lang.String, com.stock.data.DataMap)
	 */
	private int PREVIOUS = 30;
	private int GAIN = 5;
	private double LIMIT = PREVIOUS;
	private String myStatisticType = Constant.SPre30Gain5I;

	private double[] closeOffset = null;
	private double[] volOffset = null;
	private double[] openOffset = null;
	private double[] highOffset = null;
	private double[] lowOffset = null;
	private double[] closeScale = null;
	private double[] volScale = null;
	private double[] openScale = null;
	private double[] highScale = null;
	private double[] lowScale = null;

	private int[] dayFields = null;
	private int[] maFields = null;
	private int[] overAllmaFields = null;
	private int maxMa=-1;
	private int maxOverAllMa=-1;
	private boolean serializedList = false;
	private double[] offsetList = null;
	private double[] scaleList = null;
	private double[] scaleSquareList = null;

	private double[] mean = null;
	private double[] stdV = null;

	private boolean skipBigChange = true;
	private boolean forceBuy = false;
	
	public AbstractSPreGain(String myStatisticType, int pre, int gain, double limit){
		this(myStatisticType, pre, gain, limit, null, null, null, null, null,
				null, null, null, null, null);
	}

	public AbstractSPreGain(String myStatisticType, int pre, int gain,
			double[] closeOffset, double[] volOffset, double[] openOffset, double[] highOffset, double[] lowOffset,
			double[] closeScale,  double[] volScale,  double[] openScale,  double[] highScale,  double[] lowScale) {
		this(myStatisticType, pre, gain, pre, // limit == pre
		closeOffset, volOffset, openOffset, highOffset, lowOffset,
		closeScale,  volScale,  openScale,  highScale,  lowScale);
	}

	public AbstractSPreGain(String myStatisticType, int pre, int gain, double limit,
							double[] closeOffset, double[] volOffset, double[] openOffset, double[] highOffset, double[] lowOffset,
							double[] closeScale,  double[] volScale,  double[] openScale,  double[] highScale,  double[] lowScale) {
		super(myStatisticType);
		this.myStatisticType = myStatisticType;
		this.PREVIOUS = pre;
		this.GAIN = gain;
		this.LIMIT = limit;
		this.closeOffset = closeOffset;
		this.volOffset = volOffset;
		this.openOffset = openOffset;
		this.highOffset = highOffset;
		this.lowOffset = lowOffset;
		this.closeScale = closeScale;
		this.volScale = volScale;
		this.openScale = openScale;
		this.highScale = highScale;
		this.lowScale = lowScale;
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
			DataMap dataMap) throws Exception {
		DataArray statisticArray = null;

		if (!dataMap.containArray(Constant.MA5)
				|| !dataMap.containArray(Constant.MA10)) {
			return null;
		}
		try {
			
			
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
            DataArray sellArray = new DataArray(stockCode, getSellArrayStatisticName(), dataMap);
//            dataMap.putDataArray(getSellArrayStatisticName(), sellArray);

			for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
				RawData sellData = new RawData(close.getDate(i), 0);
				sellArray.addData(sellData);
			}
			
			for (int i = 0; i < close.size(); i++) {
				statisticArray.setValue(i, 0);
				sellArray.setValue(i, 0);
			}

			// buy: 1.cross ma5 ma10
			// * 2.ma10 < ma30
			// * 3.VolMa20 < VolMa60
			
			setBuy(statisticArray, dataMap);
			
			// sell: cross ma5 ma10
			
			setSell(statisticArray, sellArray, dataMap);

			for (int i = 0; i<close.size(); i++) {
				if (sellArray.getValue(i) < 0) {
					statisticArray.setValue(i, sellArray.getValue(i));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (this.forceBuy) {
			statisticArray.setBuyStatisticType(Constant.ForceBuyPrice);
		}

		return statisticArray;
	}
	
	protected void setBuy(DataArray statisticArray, DataMap dataMap)  throws Exception{
		DataArray open = null;
		DataArray close = null;
		DataArray high = null;
		DataArray low = null;
		DataArray vol = null;
		open = dataMap.getDataArray(Constant.OPEN);
		close = dataMap.getDataArray(Constant.CLOSE);
		high = dataMap.getDataArray(Constant.HIGH);
		low = dataMap.getDataArray(Constant.LOW);
		vol = dataMap.getDataArray(Constant.VOL);
		
		
		DataArray SHClose = null;
		DataMap overAllDataMap = null;
		String overAllStockCode = this.getStockCode001(statisticArray.getStockCode());
		if (overAllStockCode != null) {
			overAllDataMap = SharedStockDataHolder.getInstance().get(overAllStockCode);
			SHClose = overAllDataMap.getDataArray(Constant.CLOSE);
		}

		for (int i = 0; i < close.size(); i++) {
			boolean tobuy = false;

			if (i < PREVIOUS) {
				continue;
			}

			if (maxMa > 0 && i < maxMa ) {
				continue;
			}

			String keyDate = close.getDate(i);
			if (SHClose != null) {
				int overAllIndex = SHClose.getIndexByDate(keyDate);
				if (maxOverAllMa > 0 && overAllIndex < maxOverAllMa || overAllIndex >= SHClose.size() || overAllIndex < 0) {
					continue;
				}
			}

			if (hasAllotment(i, close, open, this.PREVIOUS + 1)) {
				continue;
			}
//
			if (skipBigChange && hasBigChange(i, close, open)) {
				continue;
			}
//
			if (SHClose != null && hasRecentBreakDay(i, close, SHClose)){
				continue;
			}

			if (i<close.size()-1){
				double nextLow = low.getValue(i + 1);
				double todayClose = close.getValue(i);
				if (nextLow > todayClose){
					continue;
				}
			}
			
			double[] closeList = new double[PREVIOUS];
			double[] volList = new double[PREVIOUS];
			double[] closeOpenList = new double[PREVIOUS];
			double[] openList = new double[PREVIOUS];
			double[] highList = new double[PREVIOUS];
			double[] lowList = new double[PREVIOUS];

			for (int j = i, k = 0; j > 0 && k < PREVIOUS; j--, k++) {
				double c = close.getValue(j) / close.getValue(j - 1);
				double v = vol.getValue(j) / vol.getValue(j - 1);
				double day = close.getValue(j) / open.getValue(j);
				double o = open.getValue(j) / open.getValue(j - 1);
				double h = high.getValue(j) / high.getValue(j - 1);
				double l = low.getValue(j) / low.getValue(j - 1);

				closeList[k] = Math.log10(c);
				volList[k] = Math.log10(v);
				closeOpenList[k] = Math.log10(day);
				openList[k] = Math.log10(o);
				highList[k] = Math.log10(h);
				lowList[k] = Math.log10(l);
				// System.out.println(" " + offset + " price:" + price +
				// ", vol=" + v + ", closeOpen" + day);

			}
			double value = 0;
			if (this.serializedList){
				value = calcWithDataArray(i, closeList, openList, highList, lowList, volList, dataMap, overAllDataMap);
			} else {
				value = calcWithData(closeList, openList, highList, lowList, volList);
			}


			if (value <= LIMIT) {
				tobuy = true;
			}

			if (tobuy) {
				statisticArray.setValue(i, 1);
			}

		}
	}
	
	protected void setSell(DataArray statisticArray, DataArray sellArray, DataMap dataMap) throws Exception {
		DataArray close = dataMap.getDataArray(Constant.CLOSE);
		
		for (int i = 0; i < close.size(); i++) {
			if (statisticArray.getValue(i) > 0 && i + this.GAIN < close.size()) {
				sellArray.setValue(i + this.GAIN, -1);
			}
		}
	}

	protected String getSellArrayStatisticName() {
		return this.myStatisticType + Constant.SELL_ARRAY_SUFFIX;
	}

	protected double calcWithData(double[] closeList, double[] openList, double[] highList, double[] lowList, double[] volList) {
		double value = 0;
		for (int j = 0; j < PREVIOUS; j++) {
			double temp;
			if (closeOffset != null && closeScale != null && j < closeOffset.length){
				temp = closeList[j] + closeOffset[j];
				value += temp * temp / closeScale[j] / closeScale[j];
			}
			if (openOffset != null && openScale != null && j < openOffset.length){
				temp = openList[j] + openOffset[j];
				value += temp * temp / openScale[j] / openScale[j];
			}
			if (highOffset != null && highScale != null && j < highOffset.length){
				temp = highList[j] + highOffset[j];
				value += temp * temp / highScale[j] / highScale[j];
			}
			if (lowOffset != null && lowScale != null && j < lowOffset.length){
				temp = lowList[j] + lowOffset[j];
				value += temp * temp / lowScale[j] / lowScale[j];
			}
			if (volOffset != null && volScale != null && j < volOffset.length){
				temp = volList[j] + volOffset[j];
				value += temp * temp / volScale[j] / volScale[j];
			}
		}
		return value;
	}

	protected double calcWithDataArray(int i, double[] closeList, double[] openList, double[] highList, double[] lowList, double[] volList, DataMap dataMap, DataMap overAllDataMap) throws Exception {
		double value = 0.0d;
		double[] dataArray = new double[offsetList.length];
		int idxDataArray = 0;
		for (int t=0;t< dayFields[0];t++) {
			dataArray[idxDataArray++] = closeList[t];
		}
		for (int t=0;t< dayFields[1];t++) {
			dataArray[idxDataArray++] = openList[t];
		}
		for (int t=0;t< dayFields[2];t++) {
			dataArray[idxDataArray++] = highList[t];
		}
		for (int t=0;t< dayFields[3];t++) {
			dataArray[idxDataArray++] = lowList[t];
		}
		for (int t=0;t< dayFields[4];t++) {
			dataArray[idxDataArray++] = volList[t];
		}

		DataArray close = dataMap.getDataArray(Constant.CLOSE);
		double keyDayClose = close.getValue(i);

		for (int maIndex = 0; maIndex < this.maFields.length;maIndex++) {
			int maType = this.maFields[maIndex];
			double maValue = this.getMAValue(dataMap, maType, i);
			double maCloseValue = keyDayClose/maValue;
			double logValue = Math.log10(maCloseValue);
			dataArray[idxDataArray++] = logValue;
		}

		if (this.overAllmaFields.length > 0 && overAllDataMap != null) {
            String keyDate = close.getDate(i);
            DataArray SHClose = overAllDataMap.getDataArray(Constant.CLOSE);
            int overAllIndex = SHClose.getIndexByDate(keyDate);
            double overAllKeyDayClose = SHClose.getValue(overAllIndex);

            for (int maIndex = 0; maIndex < this.overAllmaFields.length;maIndex++) {
                int maType = this.overAllmaFields[maIndex];
                double maValue = this.getMAValue(overAllDataMap, maType, overAllIndex);
                double maCloseValue = overAllKeyDayClose/maValue;
                double logValue = Math.log10(maCloseValue);
                dataArray[idxDataArray++] = logValue;
            }
		}

		if (mean != null) {
			double[] normalized = new double[dataArray.length];
			for(int t=0;t< dataArray.length;t++) {
				normalized[t] = (dataArray[t] - mean[t]) /stdV[t];
			}
			dataArray = normalized;
		}

		for (int j=0;j<this.offsetList.length;j++){
			if (value<=this.LIMIT){
				double temp = dataArray[j]+offsetList[j];
				value += (temp*temp)/(this.scaleSquareList[j]);
			}
		}

		return value;
	}

	protected double getMAValue(DataMap dataMap, int maType, int i) throws Exception {
		switch (maType){
			case 3:
				return dataMap.getDataArray(Constant.MA3).getValue(i);
			case 5:
				return dataMap.getDataArray(Constant.MA5).getValue(i);
			case 10:
				return dataMap.getDataArray(Constant.MA10).getValue(i);
			case 20:
				return dataMap.getDataArray(Constant.MA20).getValue(i);
			case 30:
				return dataMap.getDataArray(Constant.MA30).getValue(i);
			case 60:
				return dataMap.getDataArray(Constant.MA60).getValue(i);
			case 120:
				return dataMap.getDataArray(Constant.MA120).getValue(i);
			case 200:
				return dataMap.getDataArray(Constant.MA200).getValue(i);
			default:
				System.err.println("ma type " + maType + " is not supported!");
				throw new Exception("ma type " + maType + " is not supported!");
		}



	}


	public AbstractSPreGain setMeanAndStdV(double[] mean, double[] stdV) {
		this.mean = Arrays.copyOf(mean, mean.length);
		this.stdV = Arrays.copyOf(stdV, stdV.length);
		return this;
	}

	public AbstractSPreGain setConfigArray(double[] offsetList, double[] scaleList, int[] dayFields, int[] maFields, int[] overAllmaFields){
		this.serializedList = true;
		this.offsetList = Arrays.copyOf(offsetList, offsetList.length);
		this.scaleList = Arrays.copyOf(scaleList, scaleList.length);
		this.scaleSquareList = Arrays.stream(scaleList).map(s -> s*s).toArray();
		this.dayFields = Arrays.copyOf(dayFields, dayFields.length);
		this.maFields = Arrays.copyOf(maFields, maFields.length);
		if (this.maFields.length > 0) {
			this.maxMa = Arrays.stream(this.maFields).max().getAsInt();
		}

		if (overAllmaFields == null) {
			int[] emptyArray = {};
			overAllmaFields = emptyArray;
		}
		this.overAllmaFields = Arrays.copyOf(overAllmaFields, overAllmaFields.length);
		if (this.overAllmaFields.length > 0) {
			this.maxOverAllMa = Arrays.stream(this.overAllmaFields).max().getAsInt();
		}
		return this;
	}

	public void setSkipBigChange(boolean skipBigChange) {
		this.skipBigChange = skipBigChange;
	}

	public void setForceBuy(boolean forceBuy) {
		this.forceBuy = forceBuy;
	}

	protected boolean hasBigChange(int i, DataArray close, DataArray open) throws Exception{
		int datesForBigChange = 2;
		boolean hasBigChange = false;
		for (int k=0;k<datesForBigChange && !hasBigChange;k++){
			if (Math.abs(close.getValue(i-k) - open.getValue(i-k)) > open
					.getValue(i-k) * 0.0995f
					||  (i-k - 1 > 0  && Math.abs(close.getValue(i-k) - close.getValue(i-k - 1)) > close
					.getValue(i-k - 1) * 0.0995f)) {
				hasBigChange = true;
			}
		}
		return hasBigChange;
	}

	protected boolean hasRecentBreakDay(int i, DataArray close, DataArray SHClose) throws Exception{
		boolean hasRecentBreakDay = false;
		String today = close.getDate(i);
		int idxTodayOfSH = SHClose.getIndexByDate(today);
		int datesToCompare = this.PREVIOUS;
		int fromDateIdx = Math.max(idxTodayOfSH-datesToCompare, 0);
		if (!close.getDate(Math.max(i-datesToCompare, 0)).equals(SHClose.getDate(fromDateIdx))){
			hasRecentBreakDay = true;
		}
		return hasRecentBreakDay;
	}

}
