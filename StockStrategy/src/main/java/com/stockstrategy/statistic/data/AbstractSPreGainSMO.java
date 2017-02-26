/**
 * 
 */
package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import android.graphics.Paint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
public abstract class AbstractSPreGainSMO extends AbstractStrategyStatisticData {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
	 * java.lang.String, com.stock.data.DataMap)
	 */
	private int PREVIOUS = 30;
	private int GAIN = 5;
	private String myStatisticType = Constant.SPre30Gain5I;

	private String filename;
	private double kernalPowD;
	private Params params;
	
	

	public AbstractSPreGainSMO(String myStatisticType, int pre, int gain,
			String filename, double kernalPowD
			) {
		super(myStatisticType);
		this.myStatisticType = myStatisticType;
		this.PREVIOUS = pre;
		this.GAIN = gain;
		this.filename = filename;
		params = readParams(this.filename, this.PREVIOUS);
		this.kernalPowD = kernalPowD;
	}
	
	public int getPrevious(){
		return this.PREVIOUS;
	}
	public int getGain(){
		return this.GAIN;
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
				
				double[] x0 = transferToVector(closeList, openList, volList);
				if ( hypothesis(params.x, params.y, params.a, params.b, x0, kernalPowD)>0.0d){
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
	
	public static double kernal(double[] x1, double[] x2, double kernalPowD){
		return  Math.pow(innerProduct(x1, x2), kernalPowD);
	}
	
	public static double hypothesis(double[][] x, double[] y, double[] a, double b, double[] x0, double kernalPowD){
		double h = b;
		int m = y.length;
		for(int i=0;i<m;i++){
			if (a[i]>0){
				h+= a[i]*y[i] *kernal(x[i], x0, kernalPowD);
			}
			
		}
		return h;
	}
	
	public static double innerProduct(double[] x1, double[] x2){
		double result = 0.0d;
		for (int i=0;i<x1.length;i++){
			result += x1[i] * x2[i];
		}
		return result;
	}

	
	public static double[] transferToVector(double[] closeList, double[] openList, double[] volList){
		double[] vector = new double[closeList.length+openList.length+volList.length];
		int k=0;
		for (int i=0;i<closeList.length;i++){
			vector[k++] = closeList[i];
		}
		for (int i=0;i<openList.length;i++){
			vector[k++] = openList[i];
		}
		for (int i=0;i<volList.length;i++){
			vector[k++] = volList[i];
		}
		return vector;
	}
	
	private synchronized static Params readParams(String filename, int pre){
		File file = new File(filename);
		BufferedReader br = null;
		
		Params params = null;
		
		try {
			br = new BufferedReader(new FileReader(file));
			String line1 = br.readLine();
			int n = Integer.parseInt(line1);
			params = new Params(n, pre);
			line1 = br.readLine();
			params.b = Double.parseDouble(line1);
			for (int i=0;i<n;i++){
				String line = br.readLine();
				String[] strs = line.split(",");
				params.a[i] = Double.parseDouble(strs[0]);
				params.y[i] = Double.parseDouble(strs[1]);
				params.x[i] = new double[strs.length-2];
				for (int j=0;j<params.x[i].length;j++){
					params.x[i][j] = Double.parseDouble(strs[j+2]);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not exists: "+filename);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (br!=null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return params;
	} 
	
	private static class Params{
		private double[] a;
		private double[] y;
		private double b;
		private double[][] x;
		private Params(int n, int pre){
			a = new double[n];
			y = new double[n];
			x = new double[n][pre];
		}
	} 
	
	private static class OneParam{
		double value;
		
		public OneParam(){}
		public OneParam(double value){
			this.value = value;
		}
		
		public void set(double newValue){
			this.value = newValue;
		}
		public double get(){
			return this.value;
		}
		public double add(double toAdd){
			this.value+=toAdd;
			return this.value;
		}
		public double minus(double toMinus){
			this.value-=toMinus;
			return this.value;
		}
		
		@Override
		public String toString(){
			return String.valueOf(value);
		}
		
	}

}
