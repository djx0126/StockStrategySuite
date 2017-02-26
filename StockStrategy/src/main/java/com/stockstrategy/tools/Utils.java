/**
 * 
 */
package com.stockstrategy.tools;

import java.util.Calendar;

/**
 * @author ejixdai
 *  some useful functions here
 */
public class Utils {
	public static String[] sortStingArray(String[] array){
		for ( int i=0; i<array.length-1;i++){
			for (int j=array.length-2;j>=i;j-- ){
				if (array[j].compareTo(array[j+1])>0){
					String tempString = array[j];
					array[j] = array[j+1];
					array[j+1] = tempString;
				}
			}
		}
		return array;
	}
	
	public static String today(){
		//String today = (String) DateFormat.format("yyyyMMdd", new Date());
		//endDate = "20101231";
		StringBuilder sb = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		sb.append(cal.get(Calendar.YEAR));
		sb.append(cal.get(Calendar.MONTH)+1<10 ? "0"+String.valueOf(cal.get(Calendar.MONTH)+1) : String.valueOf(cal.get(Calendar.MONTH)+1) );
		sb.append(cal.get(Calendar.DATE)<10 ? "0"+cal.get(Calendar.DATE) : cal.get(Calendar.DATE));
		return sb.toString();
	}
	
	
	public static String format3(double number){
		return String.format("%.3f", number);
	}
	
	public static String addMonth(String date, int monthsToAdd){
		String newDate="";
		
		String yearText = date.substring(0, 4);
		String monthText = date.substring(4,6);
		String dayText = date.substring(6);
		
		int year = Integer.parseInt(yearText);
		int month = Integer.parseInt(monthText);
		//int day = Integer.parseInt(dayText);
		
		int allMonths = year*12+month+monthsToAdd;
		year = allMonths/12;
		month = allMonths%12;
		if(month==0){
			month = 12;
			year--;
		}
		yearText = String.valueOf(year);
		monthText = String.valueOf(month);
		if (month<10){
			monthText = "0"+monthText;
		}
		
		
		newDate=yearText+monthText+dayText;
		return newDate;
	}
	
	public static String removeLastSP(String source , String sp){
		String result = source;
		result = result.substring(0, result.lastIndexOf(sp));
		return result;
	}
}
