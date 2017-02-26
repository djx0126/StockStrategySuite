package com.djx.stockgainanalyzer;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.djx.stockgainanalyzer.data.Field;
import com.djx.stockgainanalyzer.data.PreviousData;
import com.djx.stockgainanalyzer.data.StockGainData;

public class Utils {


	public static void transeferToLog(List<StockGainData> allData, Field[] PARAMS){
    	for (StockGainData data:allData){
    		data.setGain(Math.log10(data.getGain()));
    		for (PreviousData pData: data.getPreviousData()){
    			for (int i=0;i<PARAMS.length;i++){
    				double value = pData.getValue(PARAMS[i]);
    				double logValue = Math.log10(value);
					pData.setValue(PARAMS[i], logValue);
    			}
    		}
    	}
    }

	public static double adjustSmallGain(double gain){
    	return (gain-1.00000000000d)*100.0d;
    }

	public static double[] cloneArray(double[] src){
		return Arrays.copyOf(src, src.length);
	}
	
	public static String doubleToStr(double d){
		return String.format("%1$.20f", d);
	}

	public static void transformValueToLog(double[] values) {
		for (int i=0;i<values.length;i++) {
			double value = values[i];
			double logValue = Math.log10(value);
			values[i] = logValue;
		}
	}

	public static double[] toNegative(double[] array){
		double[] negativeArray = new double[array.length];
		for (int i=0;i<array.length;i++) {
			negativeArray[i] = -array[i];
		}
		return negativeArray;
	}

	public static String getArrayString(String varName, double[] doubleArray) {
		return varName+"="+Arrays.toString(doubleArray).replace('[', '{').replace(']', '}');
	}

	public static String getFieldArrayDefString(String varName, double[] doubleArray) {
		String defStr = "private static double[] "+ getArrayString(varName, doubleArray)+";";
		return defStr;
	}

	public static String getArrayString(String varName, int[] intArray) {
		return varName+"="+Arrays.toString(intArray).replace('[', '{').replace(']', '}');
	}

	public static String getFieldArrayDefString(String varName, int[] intArray) {
		String defStr = "private static double[] "+ getArrayString(varName, intArray)+";";
		return defStr;
	}

	public static String todayStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(new Date());
	}

	public static String nowStr() {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		return formatter.format(new Date());
	}

}
