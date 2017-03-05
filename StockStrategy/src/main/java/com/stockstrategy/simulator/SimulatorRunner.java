package com.stockstrategy.simulator;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.StockDataHolder;
import com.stockstrategy.file.StockLister;
import com.stockstrategy.simulator.aggregate.AggregateMgr;
import com.stockstrategy.statistic.data.StatisticManager;
import com.stockstrategy.statistic.result.StatisticResultManager;

public class SimulatorRunner {
	protected String stockCode = "null";
	protected String startDate = "-"; // modify start date here
	protected String endDate = "-";
	protected List<String> stockCodes = null;
	protected int stockExcuted = 0;
	protected int stockThreadFinished = 0;
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

	public List<String> doSimulate(String startDate, String endDate) {
		init(startDate, endDate);
		run();
		resultInit();

		return resultList;
	}

	protected void run() {
		stockThreadFinished = 0;
		stockCodes.stream().parallel().forEach(stockCode -> {
            if (Constant.debug == true) {
                System.out.println("executor started for code: " + stockCode);
            }

			execute(stockCode, startDate, endDate);
            if (Constant.debug == true) {
                System.out.println("calc done for code: " + stockCode);
            }

			AggregateMgr.getInstance().updateAggregates(stockCode, startDate, endDate);
            if (Constant.debug == true) {
                System.out.println("aggregator done for code: " + stockCode);
            }

            StockDataHolder.getInstance().remove(stockCode);
            OneStockExecuted();
            if (Constant.debug == true) {
                System.out.println("executor done for code: " + stockCode);
            }
		});

		while (stockThreadFinished < stockToExcute) {
			try {
			    System.out.println("waiting runner executing: " + stockThreadFinished + "/" + stockToExcute + ", time: " + new Date());
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
		synchronized (SimulatorRunner.class) {
			stockThreadFinished++;
		}
		
		if (stockThreadFinished % 10 == 0) {
			System.out.print("=");
		}
	}

	protected void resultInit() {
		resultList = DataAnalyzer.sortStrategy();
		meanGainList = new LinkedList<>();
		allNetGainList = new LinkedList<>();
		meanGainDelayList = new LinkedList<>();
		allLostList = new LinkedList<>();
		allLostDelayList = new LinkedList<>();
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
