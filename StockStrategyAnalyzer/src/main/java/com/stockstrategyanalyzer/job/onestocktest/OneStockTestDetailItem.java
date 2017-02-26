package com.stockstrategyanalyzer.job.onestocktest;

import com.stockstrategy.tools.Utils;

public class OneStockTestDetailItem {
	private long jobId;
	private String date;
	private double buySellCode;
	private double price;
	
	public OneStockTestDetailItem(long jobId, String date, double buySellCode, double price){
		this.jobId = jobId;
		this.date = date;
		this.buySellCode = buySellCode;
		this.price = price;
	}
	
	@Override
	public String toString(){
		String priceString = Utils.format3(price);
		String actionCodeString = "(" + (buySellCode > 0?"B":"S" ) +Utils.format3(buySellCode)+")"; 
		return "Date:"+date+" "+actionCodeString+" "+"price:"+priceString;
	}

	public long getJobId() {
		return jobId;
	}

	public String getDate() {
		return date;
	}

	public double getBuySellCode() {
		return buySellCode;
	}

	public double getPrice() {
		return price;
	}

//	public OneStockTestDetailItem setBuySellCode(double buySellCode) {
//		this.buySellCode = buySellCode;
//		return this;
//	}
//
//	public OneStockTestDetailItem setDate(String date) {
//		this.date = date;
//		return this;
//	}
//
//	public OneStockTestDetailItem setPrice(double price) {
//		this.price = price;
//		return this;
//	}
}
