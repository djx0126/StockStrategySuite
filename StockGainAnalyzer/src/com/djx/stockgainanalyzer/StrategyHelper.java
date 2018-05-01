package com.djx.stockgainanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.stockstrategy.constant.Configurer;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.example.panel.tester.StockStrategyTester;
import com.stockstrategy.statistic.data.GainDataStrategy;
import com.stockstrategy.tools.Utils;

public class StrategyHelper {

	public static String generateData(String strategyName){
		//using strategy to generate data and save to db
		Configurer.addStrategy(strategyName);

		String endDate = "20170901";// Utils.today();
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

		String startDate = "20070101"; //Utils.addMonth(endDate, -12);

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
		return endDate;
	}

	@SuppressWarnings("static-access")
	public static void generateData(){
		generateData(GainDataStrategy.NAME);
	}
	
	
}
