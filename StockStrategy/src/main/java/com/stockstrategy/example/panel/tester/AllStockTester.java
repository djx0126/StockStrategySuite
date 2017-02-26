package com.stockstrategy.example.panel.tester;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.file.RemoteDataReader;
import com.stockstrategy.file.IDataReader;
import com.stockstrategy.simulator.SimulatorRunner;
import com.stockstrategy.simulator.aggregate.AbstractAggregate;
import com.stockstrategy.simulator.aggregate.AggregateMgr;
import com.stockstrategy.simulator.aggregate.BuyCounter;
import com.stockstrategy.simulator.aggregate.InhandCounter;
import com.stockstrategy.tools.FileUtils;
import com.stockstrategy.tools.Utils;

public class AllStockTester extends SimulatorRunner {
	protected List<AbstractAggregate> aggregates = Arrays.asList(
			new BuyCounter("test.conf_arr_test.30_10_Aggregated"),
			new InhandCounter("test.conf_arr_test.30_10_Aggregated"),
			new BuyCounter("test.conf_arr_test.60_15_Aggregated"),
			new InhandCounter("test.conf_arr_test.60_15_Aggregated"),
			new BuyCounter("Pre30Gain10.19"),
			new InhandCounter("Pre30Gain10.19"),
			new BuyCounter("Pre30Gain10.Aggregated"),
			new InhandCounter("Pre30Gain10.Aggregated"));
	protected static final String SP = ", ";
	protected static final String testStockCode="000001";
	protected static String outputFileName="allstockresults.txt";
	public static String dateString;
	protected static Map<AbstractAggregate, String> aggregaterResults = new HashMap<>();

	public static  void testForDays(String startDate , String endDate, String fileName) throws IOException{
		setOutputFile(fileName);
		testForDays(startDate , endDate);
	}
	
	public static void setOutputFile(String fileName){
		outputFileName = fileName;
	}
	
	public static  void testForDays(String startDate , String endDate) throws IOException{
		resetResults();
		
    	DataArray dataArray = getDateArray(startDate , endDate);
    	if (dataArray.size()>0){
    		try {
    			for (int i=0;i<dataArray.size();i++){
    				String tmpEndDate = dataArray.getDate(i);
        			String tmpStartDate = Utils.addMonth(tmpEndDate, -24);
        			dateString += tmpEndDate+SP;
        			//System.out.println("start date = "+tmpStartDate +"  end date = "+tmpEndDate);
        			doOneTest(tmpStartDate,tmpEndDate);
    			}
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		dateString = "date"+SP+Utils.removeLastSP(dateString, SP);
			aggregaterResults.forEach((a, str) -> aggregaterResults.put(a, a.getName()+SP+Utils.removeLastSP(str, SP) + "\n"));


    		StringBuilder sb = new StringBuilder();
    		sb.append(dateString);sb.append("\n");
			aggregaterResults.values().stream().forEach(sb::append);

    		File outFile = FileUtils.prepareFile(Constant.MGMT_DIR,outputFileName);
    		FileUtils.writeFile(outFile, sb.toString());
    	}
    }
	
	
	
    protected static void doOneTest(String startDate , String endDate){
    	AllStockTester tester = new AllStockTester();
    	AggregateMgr.getInstance().resetAggregates();
		tester.aggregates.stream().forEach(AggregateMgr.getInstance()::addAggregate);

    	tester.doSimulate(startDate, endDate);

		tester.aggregates.stream().forEach(a -> {
			String old = aggregaterResults.getOrDefault(a, "");
			String oneResult = String.valueOf(String.valueOf(a.getResult()))+SP;
			aggregaterResults.put(a, old + oneResult);
		});
    	AggregateMgr.getInstance().releaseAggregates();
    }
    
    protected static  DataArray getDateArray(String startDate, String endDate){
    	DataArray dateArray = null;
    	File stockFile;
		try {
			stockFile = FileUtils.tryGetFile(Constant.DATA_DIR_SH,Constant.PREFIX_SH+testStockCode+Constant.SUFFIX);
			if (stockFile!=null){
				//System.out.println(startDate+"~"+endDate);
				IDataReader fReader = new RemoteDataReader(testStockCode, startDate, endDate);
				DataMap dataMap = fReader.readDataMap();
				
				if (dataMap!=null){
					for (String type:Constant.rawTypes){
						if (dataMap.containArray(type)){
							try {
								DataArray tmpDataArray =dataMap.getDataArray(type);
								//System.out.println("size is :"+dataArray.size());
								if (tmpDataArray.size()>0){
									dateArray = tmpDataArray;
									break;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return dateArray;
    }
    
    protected static void resetResults(){
		dateString="";
		aggregaterResults.clear();
	}
   
}
