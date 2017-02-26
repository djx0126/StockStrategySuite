package com.stockstrategy.example.panel.tester;

import java.io.File;
import java.io.IOException;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.file.RemoteDataReader;
import com.stockstrategy.file.IDataReader;
import com.stockstrategy.tools.FileUtils;
import com.stockstrategy.tools.Utils;

public class StockDataReader {
	private static final String SP = ", ";
	private static String outputFileName = null;
	
	public static void readStockData( String stockCode, String startDate, String endDate, String fileName) throws IOException{
		setOutputFile(fileName);
		readStockData(  stockCode,  startDate,  endDate);
	}
	
	public static void setOutputFile(String fileName){
		outputFileName = fileName;
	}
	
	public static void readStockData( String stockCode, String startDate, String endDate) throws IOException{
		File outFile = FileUtils.prepareFile(Constant.MGMT_DIR,outputFileName);
		if (outFile!=null){
			//output file is OK
			//System.out.println("output file "+outFile.getName()+" is OK");
			File stockFile = FileUtils.tryGetFile(Constant.DATA_DIR_SH,Constant.PREFIX_SH+stockCode+Constant.SUFFIX);
			if (stockFile==null){
				stockFile = FileUtils.tryGetFile(Constant.DATA_DIR_SZ,Constant.PREFIX_SZ+stockCode+Constant.SUFFIX);
			}
			if (stockFile!=null){
				//stock file is OK
				//System.out.println("stock file "+stockFile.getName()+" is OK");
				IDataReader fReader = new RemoteDataReader(stockCode, startDate, endDate);
				DataMap dataMap = fReader.readDataMap();
				if(dataMap==null){
					System.out.println("dataMap is null");
				}
				
				String date = "date"+SP+parseDate(dataMap);
				String close = "close"+SP+parseData( dataMap,  Constant.CLOSE);
				String vol = "vol"+SP+parseData( dataMap,  Constant.VOL);
				FileUtils.writeFile(outFile, date+"\n"+close+"\n"+vol);
			}
		}
		
	}
	
	private static String parseDate(DataMap dataMap){
		StringBuilder sb = new StringBuilder();
		String result = null;
		for (String type:Constant.rawTypes){
			//System.out.println("tyep is :"+type);
			if (dataMap.containArray(type)){
				try {
					DataArray dataArray =dataMap.getDataArray(type);
					//System.out.println("size is :"+dataArray.size());
					if (dataArray.size()>0){
						for (int i = 0 ;i < dataArray.size();i++){
							sb.append(dataArray.getDate(i));
							sb.append(SP);
						}
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		result = sb.toString();
		return Utils.removeLastSP(result, SP);
	}
	
	private static String parseData(DataMap dataMap, String statisticType){
		StringBuilder sb = new StringBuilder();
		String result = null;
		try {
			DataArray dataArray = dataMap.getDataArray(statisticType);
			for (int i = 0 ;i < dataArray.size();i++){
				double value = dataArray.getValue(i);
				sb.append(Utils.format3(value));
				sb.append(SP);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result = sb.toString();
		return Utils.removeLastSP(result, SP);
	}
	

	
	
	
	
}
