package com.stockstrategy.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.StockDataHolder;
import com.stockstrategy.file.StockLister;
import com.stockstrategy.simulator.aggregate.AggregateMgr;
import com.stockstrategy.statistic.data.StatisticManager;
import com.stockstrategy.statistic.result.StatisticResultManager;

public class SimulatorRunner {
	// protected static ExecutorService threadPool =
	// Executors.newFixedThreadPool(Constant.THREADNUM);

	protected String stockCode = "null";
	protected String startDate = "-"; // modify start date here
	protected String endDate = "-";
	protected List<String> stockCodes = null;
	protected int stockExcuted = 0;
	protected int stockThreadFinished = 0;
	protected int lock = 0;
	protected int stockToExcute;
	public List<String> resultList = null; // sorted on statistic's
													// gain, String statistic
													// Type
	public LinkedList<Float> meanGainList = null;
	public LinkedList<Float> allNetGainList = null;
	public LinkedList<Float> meanGainDelayList = null;
	public LinkedList<Float> allLostList = null;
	public LinkedList<Float> allLostDelayList = null;

	protected final static int NUMSTOCKSPRINT = 50;

	
	/*
	 * public static void main(String[] args) { TestApp app = new TestApp();
	 * app.init(); app.run(); app.resultInit(); app.printResult();
	 * app.printDetailResult(); }
	 */

	public List<String> doSimulate(String startDate, String endDate) {
		init(startDate, endDate);
		run();
		resultInit();

		return resultList;
	}

	protected void run() {
		ExecutorService threadPool = Executors
				.newFixedThreadPool(Constant.THREADNUM);
		//System.out.println("Test app start run");
		stockThreadFinished = 0;
		for (stockExcuted = 0; stockExcuted < stockToExcute; stockExcuted++) {
			stockCode = stockCodes.get(stockExcuted);

			threadPool.submit(new ExecuteThread(stockCode, startDate, endDate));

		}
		threadPool.shutdown();

		while (stockThreadFinished < stockToExcute) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		stockCodes.clear();
		AllStockExecuted();
	}

	protected void AllStockExecuted() {
		System.out.print(" ==>\n");
	}

	protected void OneStockExecuted() {
		synchronized (this) {
			stockThreadFinished++;
			// if (stockThreadFinished>=stockToExcute){
			// SimulatorRunner.this.notifyAll();
			// }
		}
		
		if (stockThreadFinished % 10 == 0) {
			System.out.print("=");
		}
	}

	protected void resultInit() {
		// System.out.println("Test app result init");
		//stockCount = DataAnalyzer.stockCount;
		
		resultList = DataAnalyzer.sortStrategy();
		meanGainList = new LinkedList<Float>();
		allNetGainList = new LinkedList<Float>();
		meanGainDelayList = new LinkedList<Float>();
		allLostList = new LinkedList<Float>();
		allLostDelayList = new LinkedList<Float>();
		for (int i = 0; i < resultList.size(); i++) {
			String statisticType = resultList.get(i);
			meanGainList.add(DataAnalyzer.getMeanGain(statisticType));
			allNetGainList.add(DataAnalyzer.getAllNetGain(statisticType));
			meanGainDelayList.add(DataAnalyzer.getMeanGainDelay(statisticType));
			allLostList.add(DataAnalyzer.getAllLost(statisticType));
			allLostDelayList.add(DataAnalyzer.getAllLostDelay(statisticType));
		}
	}

	protected void execute(String stockCode, String startDate, String endDate) {
		Simulator.actualExecute(stockCode, startDate, endDate);
	}

	protected void init(String startDate, String endDate) {
	    // System.out.println("Test app init");
		StockDataHolder.getInstance().clear();
		StatisticManager.getInstance().clear();
		StatisticResultManager.getInstance().clear();
		//DataAnalyzer.resetAllResults();
		stockCode = "";

		Date date = new Date();
		System.out.println("StartDate=" + startDate + " EndDate=" + endDate
				+ "  " + date);
		if (startDate == null || startDate.length() == 0) {
			this.startDate = Constant.START;
		} else {
			this.startDate = startDate;
		}
		if (endDate == null || endDate.length() == 0) {
			this.endDate = Constant.END;
		} else {
			this.endDate = endDate;
		}
		
		Simulator.prepareSharedStockData(startDate, endDate);

        stockCodes = StockLister.getStockList();

		stockExcuted = 0;
		stockToExcute = stockCodes.size();
	}
	
	protected class ExecuteThread implements Runnable {
		protected String myStockCode;
		protected String myStartDate;
		protected String myEndDate;

		protected ExecuteThread(String stockCode, String startDate,
				String endDate) {
			this.myStockCode = stockCode;
			this.myStartDate = startDate;
			this.myEndDate = endDate;
		}

		@Override
		public void run() {
			execute(myStockCode, myStartDate, myEndDate);
			AggregateMgr.getInstance().updateAggregates(myStockCode, myStartDate, myEndDate);
			OneStockExecuted();
			StockDataHolder.getInstance().remove(myStockCode);
		}
	}
}
