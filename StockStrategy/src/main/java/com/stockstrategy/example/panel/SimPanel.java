package com.stockstrategy.example.panel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Configurer;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.StockDataHolder;
import com.stockstrategy.example.panel.tester.AllStockTester;
import com.stockstrategy.example.panel.tester.StockDataReader;
import com.stockstrategy.example.panel.tester.StockFinder;
import com.stockstrategy.example.panel.tester.StockStrategyTester;
import com.stockstrategy.favoritestocks.DefaultStockAccountMgr;
import com.stockstrategy.favoritestocks.IStockParam;
import com.stockstrategy.favoritestocks.StockAccountLoader;
import com.stockstrategy.favoritestocks.StockParamBean;
import com.stockstrategy.file.StockLister;
import com.stockstrategy.simulator.DataAnalyzer;
import com.stockstrategy.simulator.Simulator;
import com.stockstrategy.statistic.data.ConfigBasedStrategyFactory;
import com.stockstrategy.statistic.data.StatisticManager;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategy.tools.FileUtils;
import com.stockstrategy.tools.Utils;

@SuppressWarnings("static-access")
public class SimPanel {
	public static final String TAGFAVORSTRATEGY = "FavorStrategy";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArgParser.loadInitConfigures(args, Constant.class);

		SimPanel panel = new SimPanel();

		ConfigBasedStrategyFactory.loadStrategyProperties();
		
		
		String lastUpdate = StockAccountLoader.getLastUpdatedDate();
		String today = Utils.today();
		if (lastUpdate.compareTo(today)<0){
			panel.Func3();
		}
		
		boolean toQuit= false;
		while (!toQuit){
			panel.printMenu();
			BufferedInputStream input = new BufferedInputStream(System.in);
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			try {
				int i = br.read();
				switch (i){
				case '1': case 'R':	case 'r':
					panel.Func1();
					break;
				case '2': case 'L':	case 'l':
					panel.Func2();
					break;
				case '3': case 'U':	case 'u':
					panel.Func3();
					break;
				case '4': case 'A': case 'a':
					panel.Func4();
					break;
				case '7': case 'F': case 'f':
					panel.Func7();
					break;
				case '8': case 'T': case 't':
					panel.Func8();
					break;
				case '9': case 'Q':	case 'q':
					System.out.println("Bye~");
					toQuit=true;
					break;
				case '0': 
					System.out.println("Do test~");
					
					String stockCode = "000001";
					String endDate = Utils.today();
					String startDate = Utils.addMonth(endDate, -32);
					
					//startDate="20070101";
					//endDate="20120531";

					AllStockTester.testForDays(startDate, endDate, startDate+"-"+endDate+".txt");
					StockDataReader.readStockData(stockCode, startDate, endDate, stockCode+"-"+startDate+"-"+endDate+".txt");
					
//					generateJS(stockCode, startDate, endDate);
					
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	
	
	private static void generateJS(String stockCode, String startDate,String endDate) throws IOException{
		File raw000001 = FileUtils.getFile(Constant.MGMT_DIR, stockCode+"-"+startDate+"-"+endDate+".txt");
		BufferedReader fileReader = new BufferedReader(new FileReader(raw000001));
		String date = fileReader.readLine();
		String value000001 = fileReader.readLine(); 
		String vol000001 = fileReader.readLine();
		String[] vols = vol000001.split(",");
		StringBuilder volBuilder = new StringBuilder();
		for (String s: vols){
			volBuilder.append(",");
			volBuilder.append(s.trim().substring(0, (s.trim().indexOf(".")-3)>0?(s.trim().indexOf(".")-3):s.trim().length()));
		}
		vol000001 = volBuilder.substring(1);
		fileReader.close();
		String var000001 = convertJSVar( value000001);
		String varVol000001 = convertJSVar( vol000001);
		
		File rawAll = FileUtils.getFile(Constant.MGMT_DIR, startDate+"-"+endDate+".txt");
		BufferedReader fileReaderAll = new BufferedReader(new FileReader(rawAll));
		date = fileReaderAll.readLine();
		String counter = fileReaderAll.readLine(); 
		String toBuySLevel5 = fileReaderAll.readLine(); 
		String toSell = fileReaderAll.readLine();
		String inHand = fileReaderAll.readLine(); 
		fileReaderAll.close();
		String varDate = convertJSVar( date);
		String varCounter = convertJSVar( counter);
		String varToBuySLevel5 = convertJSVar( toBuySLevel5);
		String varToSell = convertJSVar( toSell);
		String varInHand = convertJSVar( inHand);

		//File JSvarFile = FileUtils.prepareFile(Constant.RESULTS_DIR, "JSvar"+"-"+startDate+"-"+endDate+".txt");
		File JSvarFile = FileUtils.prepareFile(Constant.getResultsDir(), "jsResults.js");
		BufferedWriter bw = new BufferedWriter(new FileWriter(JSvarFile));
		bw.write(varDate);bw.newLine();
		bw.write(var000001);bw.newLine();
		bw.write(varCounter);bw.newLine();
		bw.write(varToBuySLevel5);bw.newLine();
		bw.write(varToSell);bw.newLine();
		bw.write(varInHand);bw.newLine();
		bw.write(varVol000001);bw.newLine();
		bw.flush();bw.close();
	}
	
	private static String convertJSVar(String rawValue){
		String[] values = rawValue.split(",");
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("var ");
		strBuilder.append(values[0]);
		strBuilder.append(" = ");
		strBuilder.append("[");
		for (int day=1;day<values.length;day++){
			strBuilder.append("[");
			strBuilder.append(day);
			strBuilder.append(",");
			strBuilder.append(values[day]);
			strBuilder.append("]");
			if (day<values.length-1){
				strBuilder.append(",");
			}
		}
		strBuilder.append("]");
		strBuilder.append(";");
		return strBuilder.toString();
	}
	
	private void printMenu(){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("================================================================\n");
		strBuilder.append("Please select the function:\n");
		strBuilder.append("1. Run simulator on all stocks (R)\n");
		strBuilder.append("2. List favorite stocks' info (L)\n");
		strBuilder.append("3. Update favorite stocks (U)\n");
		strBuilder.append("4. Add/Remove stock (A)\n");
		strBuilder.append("7. Find stocks to buy (F)\n");
		strBuilder.append("8. Test on one stock (T)\n");
		strBuilder.append("9. Quit (Q)\n");
		System.out.println(strBuilder.toString());
		System.out.println("Choose:");
	}
	
	//Run simulator on all stocks (R)
	private void Func1(){//
		String endDate = Utils.today();
		System.out.print("Enter the end date ["+endDate+"]: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = "";
		try {
			code = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (code.length()!=0){
			endDate=code;
		}
		//String startDate = String.valueOf(Integer.parseInt(endDate.substring(0, 4))-1)+endDate.substring(4);
		String startDate = Utils.addMonth(endDate, -12);
		
		System.out.print("Enter the start date ["+startDate+"]: ");
		try {
			code = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (code.length()!=0){
			startDate=code;
		}
		StockStrategyTester.doTest(startDate,endDate);
		
	}
	
	
	//List favorite stocks
	private void Func2(){
		String[] stocks = DefaultStockAccountMgr.getInstance().getStockList();
		Utils.sortStingArray(stocks);
		//System.out.println(Arrays.toString(stocks));
		String text = "";
		text += "stock   \t" + "buy date\t" + "buy price\t" + "now price\t" + "sell date\t" + "buy/sell";
		System.out.println (text);
		for (String stock : stocks){
			text = stock+"\t";
			IStockParam stockDetail = DefaultStockAccountMgr.getInstance().getStockDetail(stock);
			
			
			String bDateText = stockDetail.getBuyDate();
			if (bDateText.equals("")){
				bDateText = "        ";
			}
			double bPrice = stockDetail.getBuyPrice();
			String bPriceText = Utils.format3(bPrice);
			if (bPriceText.equals("0")){
				bPriceText = "       ";
			}
			double cPrice = stockDetail.getCurrentPrice();
			String cPriceText = Utils.format3(cPrice);
			if (cPriceText.equals("0")){
				cPriceText = "       ";
			}
			String sDateText = stockDetail.getSellDate();
			if (sDateText.compareTo(bDateText)<0){
				sDateText = "        ";
			}
			
			text += bDateText + "\t" + 
					bPriceText  +"\t\t" + 
					cPriceText + "\t\t" + 
					sDateText +"\t" ;
			if (stockDetail.getSellBuy()>0){
				text += "(B +"+Utils.format3(stockDetail.getSellBuy())+")";
			}else if (stockDetail.getSellBuy()<0){
				text += "(S "+Utils.format3(stockDetail.getSellBuy())+")";
			}
			/*
			switch (){
			case IStockParam.TOBUY:
				text += "(B)";
				break;
			case IStockParam.TOSELL:
				text += "(S)";
				break;
			default:
			}*/ 
			System.out.println(text);
		}
		String lastUpdate = StockAccountLoader.getLastUpdatedDate();
		System.out.println("Last update: "+ lastUpdate);
	}
	
	//Update favorite stocks (U)
	private void Func3(){
		StockAccountLoader updater = new StockAccountLoader(DefaultStockAccountMgr.getInstance());
		System.out.println("Updating...");
		updater.run();
		String lastUpdate = StockAccountLoader.getLastUpdatedDate();
		System.out.println("Last update: "+ lastUpdate);
	}
	
	//Add/Remove stock (A)
	private void Func4(){
		System.out.print("Input the stock code: ");
		//BufferedInputStream bis = new BufferedInputStream(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = "";
		try {
			code = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (code.equals("")){
			System.err.println("Input stock code error!");
		}
		if (code.length()!=6 || !code.matches("[0-9]{6}")){
			System.out.println("Wrong stock code format!");
		}
		if (DefaultStockAccountMgr.getInstance().contains(code)){
			// already include, to remove
			System.out.println("Already included as favorite, to remove? y/n");
			try {
				int input = br.read();
				if (input=='Y' || input=='y'){
					DefaultStockAccountMgr.getInstance().removeStock(code);
					System.out.println("Stock "+code +" removed.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			// not include, to add
			System.out.println("To add "+code +" into favorites? y/n");
			try {
				int input = br.read();
				if (input=='Y' || input=='y'){
					IStockParam stockDetail = new StockParamBean();
					String today = Utils.today();
					stockDetail.setAddDate(today);
					DefaultStockAccountMgr.getInstance().addStock(code, stockDetail);
					System.out.println("Stock "+code +" added.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//7. Find stocks to buy (F)
	private void Func7(){
		System.out.print("Input the end date["+ Utils.today()+"]: ");
		String endDate = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			endDate = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (endDate==null || endDate.isEmpty()){
			endDate = Utils.today();
		}
		StockFinder.doFind(endDate);
	}
	
	//Test on one stock (T)
	private void Func8(){
		StockDataHolder.getInstance().clear();
		StatisticManager.getInstance().clear();
		StatisticResultManager.getInstance().clear();
		String statisticType=null,  statisticTypeConfig;
		statisticTypeConfig = Configurer.getInstance().getConfig(TAGFAVORSTRATEGY);
		if (statisticTypeConfig==null || statisticTypeConfig.isEmpty()){
			statisticTypeConfig = Constant.CUSTOMSTATISTICSTYPES[0];
		}
		System.out.print("Input the strategy ["+statisticTypeConfig+"]: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			statisticType = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (statisticType==null || statisticType.isEmpty()){
			statisticType=statisticTypeConfig;
		}else if (statisticType!=statisticTypeConfig){
			Configurer.getInstance().setConfig(TAGFAVORSTRATEGY, statisticType);
		}
		
		
		//statisticType=Constant.Steste;
		String stockCode = "";
		System.out.print("Input the stock code: ");
		try {
			stockCode = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("Input the start date: ");
		String startDate = Constant.START;
		try {
			startDate = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("Input the end date: ");
		String endDate = Constant.END;
		try {
			endDate = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Simulator.prepareSharedStockData(startDate, endDate);
		Simulator.actualExecute(stockCode, startDate, endDate);
		DataAnalyzer.analyze(stockCode);
		
		
		try {
			DataArray buySellArray = StockDataHolder.getInstance().get(stockCode).getDataArray(statisticType);
			DataArray close = StockDataHolder.getInstance().get(stockCode).getDataArray(Constant.CLOSE);
			//double account=0.0f;
			for (int i=0;i<=buySellArray.size()-1;i++){
				String date = buySellArray.getDate(i);
				double actionCode = buySellArray.getValue(i);
				String actionText;
				if (actionCode>0){
					actionText="(B"+Utils.format3(actionCode)+")";
				}else if (actionCode<0){
					actionText="(S"+Utils.format3(actionCode)+")";
				}else{
					continue;
				}
				
				
				//if (account>0 || (account+actionCode)>0){
					String price = Utils.format3(close.getValue(i));
					System.out.println("Date:"+date+" "+actionText+" "+"price:"+price);
				//}
				//account+=actionCode;
				//if (account<0){
				//	account=0.0f;
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String gain = Utils.format3(StatisticResultManager.getInstance().getResult(stockCode).getResult(statisticType).getGain());
		String lost = Utils.format3(StatisticResultManager.getInstance().getResult(stockCode).getResult(statisticType).getLost());
		System.out.println("Stock:"+stockCode+"  Strategy:"+statisticType+"  startdate="+startDate+"  endDate="+endDate);
		System.out.println("Gain="+gain+"  lost="+lost);
		StockDataHolder.getInstance().remove(stockCode);
		StatisticManager.getInstance().clear();
		StatisticResultManager.getInstance().clear();
	}
	
}
