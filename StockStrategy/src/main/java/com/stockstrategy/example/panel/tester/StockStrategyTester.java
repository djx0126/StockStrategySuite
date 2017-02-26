package com.stockstrategy.example.panel.tester;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.simulator.DataAnalyzer;
import com.stockstrategy.simulator.SimulatorRunner;
import com.stockstrategy.simulator.aggregate.*;
import com.stockstrategy.statistic.data.AbstractStatisticData;
import com.stockstrategy.statistic.data.StatisticManager;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategy.tools.Utils;

public class StockStrategyTester extends SimulatorRunner{
	protected AbstractAggregate stockCount = new StockCounter();
    protected AbstractAggregate buySellDetailCollector = new BuySellDetailCollector();
//	protected AbstractAggregate buyCounter = new BuyCounter();
//	protected AbstractAggregate buyAmount = new BuyAmount();
//	protected AbstractAggregate sellCounter = new SellCounter();
//	protected AbstractAggregate sellAmount = new SellAmount();
//	protected AbstractAggregate inHandCounter = new InhandCounter();
//	protected AbstractAggregate ma5upCounter = new Ma5upCounter();
	//protected AbstractAggregate buyCounterSBreak = new BuyCounter(Constant.SBreak);

    /**
     * @param args
     */
    /*
     * public static void main(String[] args) { TestApp app = new TestApp();
     * app.init(); app.run(); app.resultInit(); app.printResult();
     * app.printDetailResult(); }
     */


    public static List<String> doTest(String startDate, String endDate) {
        StockStrategyTester app = new StockStrategyTester();
        app.init(startDate, endDate);


        AggregateMgr.getInstance().addAggregate(app.buySellDetailCollector);


        app.run();
        app.resultInit();
        app.printDetailResult();
        app.printResult();

        AggregateMgr.getInstance().releaseAggregate(app.buySellDetailCollector);

        return app.resultList;
    }


    protected void printDetailResult() {
        System.out.println("===detail result=== " + resultList.size());
        for (int i = 0; i < resultList.size(); i++) {
            if (i < resultList.size()) {
                String statisticType = resultList.get(i);
                System.out.println(statisticType + ": ");

                List<String> stockList = null; // sorted on statistic's
                                                     // gain, String statistic
                                                     // Type
                LinkedList<Float> gainList = null;
                LinkedList<Float> gainDelayList = null;
                stockList = DataAnalyzer.sortStock(statisticType,
                        NUMSTOCKSPRINT);
                gainList = new LinkedList<Float>();
                for (int j = 0; j < stockList.size(); j++) {
                    if (j < NUMSTOCKSPRINT) {
                        String stock = stockList.get(j);
                        gainList.add(StatisticResultManager.getInstance()
                                .getResult(stock).getResult(statisticType)
                                .getGain());
                    } else {
                        break;
                    }
                }
                String gainText = "";
                int k = 0;
                for (int j = 0; j < stockList.size(); j++) {
                    if (j < NUMSTOCKSPRINT) {
                        String stock = stockList.get(j);
                        float gain = gainList.get(j);
                        if (gain > 100) {
                            String stockGainText = Utils.format3(gain) + " %";
                            gainText += stock + ": " + stockGainText + " / ";
                            if ((++k) % 5 == 0) {
                                gainText += "\n";
                            }
                        }
                    }
                }
                System.out.println("+++ " + gainText);

                stockList = DataAnalyzer.sortStockNeg(statisticType,
                        NUMSTOCKSPRINT);
                gainList = new LinkedList<Float>();
                gainDelayList = new LinkedList<Float>();
                for (int j = 0; j < stockList.size(); j++) {
                    if (j < NUMSTOCKSPRINT) {
                        String stock = stockList.get(j);
                        gainList.add(StatisticResultManager.getInstance()
                                .getResult(stock).getResult(statisticType)
                                .getGain());
                        gainDelayList.add(StatisticResultManager.getInstance()
                                .getResult(stock).getResult(statisticType)
                                .getGainDelay());
                    } else {
                        break;
                    }
                }
                String lostText = "";
                k = 0;
                for (int j = 0; j < stockList.size(); j++) {
                    if (j < NUMSTOCKSPRINT) {
                        String stock = stockList.get(j);
                        float gain = gainList.get(j);
                        if (gain < 100) {
                            String stockGainText = Utils.format3(gain) + " %";
                            lostText += stock + ": " + stockGainText + " / ";
                            if ((++k) % 5 == 0) {
                                lostText += "\n";
                            }
                        }
                    }
                }
                System.out.println("--- " + lostText);
            }
        }
    }

    protected void printResult() {
        Map<String, List<BuySellDetailCollector.Transaction>> statisticsTransactionMap = (Map<String, List<BuySellDetailCollector.Transaction>>) buySellDetailCollector.getResult();
        for (int i = 0; i < resultList.size(); i++) {
            if (i < resultList.size()) {
                String statisticType = resultList.get(i);
                float meanGain = meanGainList.get(i);
                float allLost = allLostList.get(i);

                String gainText = Utils.format3(meanGain) + "%";
                String allLostText = Utils.format3(allLost) + "%";

                int allGainCounter = 0;
                int allLostCounter = 0;
                for (String stock : StatisticResultManager.getInstance()
                        .getStatisticResults().keySet()) {
                	if (StatisticResultManager.getInstance()
                            .getResult(stock)!=null && StatisticResultManager.getInstance()
                                    .getResult(stock).getResult(statisticType)!=null){
                		float gain = StatisticResultManager.getInstance()
                                .getResult(stock).getResult(statisticType)
                                .getGain();
                        if (gain > 100f) {
                            allGainCounter++;
                        } else if (gain < 100f) {
                            allLostCounter++;
                        }
                	}
                }

                float netGain = allNetGainList.get(i);
                float rate = netGain / allLost;

                String rateText = Utils.format3(rate);

                String printText = statisticType + ": ";
                printText += gainText + " / " + allLostText;
                printText += " ( +" + String.valueOf(allGainCounter) + "/-"
                        + String.valueOf(allLostCounter) + " )";
                printText += "  rate: " + String.valueOf(rateText);


                List<BuySellDetailCollector.Transaction> transactions = statisticsTransactionMap.get(statisticType);

                AbstractStatisticData strategy = StatisticManager.getInstance().getStatistic(statisticType);
                String strategyStartDate = strategy.getStartDate();

                int transactionGainCounter = 0;
                int transactionLostCounter = 0;
                float transactionGain = 0.0f;
                float transactionLost = 0.0f;
                for (BuySellDetailCollector.Transaction t:transactions){
                    String buyDate = t.getBuyDate();
                    if (buyDate.compareTo(strategyStartDate)<=0){
                        continue;
                    }
                    double buyPrice = t.getBuyPrice();
                    double sellPrice = t.getSellPrice();
                    if (sellPrice > buyPrice){
                        transactionGainCounter++;
                        transactionGain+= 100*((sellPrice-buyPrice)/buyPrice);
                    }else if(sellPrice < buyPrice){
                        transactionLostCounter++;
                        transactionLost+= 100*((buyPrice - sellPrice)/buyPrice);
                    }
                }
                int transactionCounter = transactionGainCounter + transactionLostCounter;
                float transactionAccuracy = 100* (float)transactionGainCounter/(float)transactionCounter;
                printText += "  accuracy: " + Utils.format3(transactionAccuracy)+"\n";

                System.out.println(printText);

            }
        }
    }

}
