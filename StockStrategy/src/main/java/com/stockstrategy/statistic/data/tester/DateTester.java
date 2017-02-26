package com.stockstrategy.statistic.data.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.tools.FileUtils;

public class DateTester {
	protected static boolean prepared = false;
	protected static final String FILENAME = "20120908-20130908.txt";
	public static DateTester myTester = new DateTester();
	protected Map<Long, HisDataType> historyData = new HashMap<Long, HisDataType>();
	
	public static HisDataType getOneDayData(String keyDay){
		HisDataType oneDayData = null;
		//System.out.println("DateTester getOneDayData:"+Integer.valueOf(keyDay));
		if (myTester.historyData.containsKey(Long.valueOf(keyDay))){
			oneDayData = myTester.historyData.get(Long.valueOf(keyDay));
			//System.out.println("DateTester getOneDayData OK:"+Long.valueOf(keyDay));
		}
		return oneDayData;
	}
	
	public synchronized static void prepare(){
		if (!prepared){
			//System.out.println("DateTester prepare");
			myTester.historyData.clear();
			myTester.readFile();
			prepared = true;
		}
	}
	
	public void readFile(){
		//System.out.println("DateTester readDataMap");
		BufferedReader br = null;
		try {
			File file = FileUtils.getFile(Constant.MGMT_DIR, FILENAME);
			if (file !=null){
				br = new BufferedReader(new FileReader(file));
				String[] lineDate = br.readLine().split(",");
				String[] lineCounter = br.readLine().split(",");
				String[] lineToBuySLevel5 = br.readLine().split(",");
				String[] lineToBuySBreak = br.readLine().split(",");
				String[] lineToSell = br.readLine().split(",");
				String[] lineInHand = br.readLine().split(",");
				String[] lineMa5up = br.readLine().split(",");
				
				
				for (int i=1;i<lineDate.length;i++){
					HisDataType aDayData = new HisDataType();
					aDayData.date = lineDate[i].trim();
					aDayData.counter = lineCounter[i].trim();
					aDayData.toBuySLevel5 = lineToBuySLevel5[i].trim();
					aDayData.toBuySBreak = lineToBuySBreak[i].trim();
					aDayData.toSell = lineToSell[i].trim();
					aDayData.inHand = lineInHand[i].trim();
					aDayData.ma5up = lineMa5up[i].trim();
					
					//System.out.println("DateTester historyData:"+aDayData.date);
					historyData.put(Long.parseLong(aDayData.date), aDayData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("DateTester readDataMap finished: "+historyData.size());
	}
	
	
	
	public class HisDataType{
		public String date;
		public String counter;
		public String toBuySLevel5;
		public String toBuySBreak;
		public String toSell;
		public String inHand;
		public String ma5up;
		
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getCounter() {
			return counter;
		}
		public void setCounter(String counter) {
			this.counter = counter;
		}
		public String getToBuySLevel5() {
			return toBuySLevel5;
		}
		public void setToBuySLevel5(String toBuySLevel5) {
			this.toBuySLevel5 = toBuySLevel5;
		}
		public String getToBuySBreak() {
			return toBuySBreak;
		}
		public void setToBuySBreak(String toBuySBreak) {
			this.toBuySBreak = toBuySBreak;
		}
		public String getToSell() {
			return toSell;
		}
		public void setToSell(String toSell) {
			this.toSell = toSell;
		}
		public String getInHand() {
			return inHand;
		}
		public void setInHand(String inHand) {
			this.inHand = inHand;
		}
		public String getMa5up() {
			return ma5up;
		}
		public void setMa5up(String ma5up) {
			this.ma5up = ma5up;
		}
		
		
	}
}
