/**
 * 
 */
package com.stockstrategy.simulator;

import java.util.*;
import java.util.stream.Collectors;

import com.stockstrategy.account.AccountMgr;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.StockDataHolder;
import com.stockstrategy.statistic.data.AbstractStatisticData;
import com.stockstrategy.statistic.data.ConfigBasedStrategyFactory;
import com.stockstrategy.statistic.data.StatisticManager;
import com.stockstrategy.statistic.result.StatisticResult;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategy.statistic.result.StockStatisticResult;

/**
 * @author ejixdai calculate statistic data for a stock, store the array into
 *         the dataMap
 */
public abstract class DataAnalyzer {


    /****************************************************************
     * /*calculate data
     * 
     * @param stockCode
     * @param dataMap
     * @return
     ***************************************************************/

    public static DataMap calBasicStatics(String stockCode, DataMap dataMap) {
        dataMap = calculate(stockCode, dataMap, Constant.BASICSTATISTICSTYPES);
        return dataMap;
    }

    public static DataMap calAdvancedStatics(String stockCode, DataMap dataMap) {
        dataMap = calculate(stockCode, dataMap, Constant.ADVANCEDTATISTICSTYPES);
        return dataMap;
    }

    public static DataMap calCustomStatics(String stockCode, DataMap dataMap) {
        dataMap = calculate(stockCode, dataMap, Constant.CUSTOMSTATISTICSTYPES);
        return dataMap;
    }

    private static DataMap calculate(String stockCode, DataMap dataMap, String[] statisticsTypes) {
        for (String statisticType : statisticsTypes) {
            if (!dataMap.containArray(statisticType)){
                dataMap = calculateWithStatisticType(stockCode, dataMap, statisticType);
            }
        }
        return dataMap;
    }

    public static DataMap calculateWithStatisticType(String stockCode,
            DataMap dataMap, String statisticType) {
        AbstractStatisticData statistics = null;

        // try first with class
        try {
            // no-parameter-constructor is needed here!
            statistics = (AbstractStatisticData) Class.forName(
                    Constant.STATISTICCLASSPREFIX + statisticType)
                    .newInstance();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        } catch (InstantiationException e) {
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }

        // to try with configBased statistics
        if (statistics == null){
            statistics = ConfigBasedStrategyFactory.buildConfigBasedStrategy(statisticType);
        }

        if (statistics != null){
            DataArray dataArray = statistics.generate(stockCode, statisticType,
                    dataMap);
            if (dataArray != null) {
                dataMap.putDataArray(statisticType, dataArray);
                if (!StatisticManager.getInstance().contains(statisticType)) {
                    StatisticManager.getInstance().putStatistic(statisticType,
                            statistics);
                }
            }
        }else {
            System.err.println("No statistic '"+statisticType+"' found");
        }

        return dataMap;
    }

    /****************************************************************
     * /*Analyze data with strategy
     * 
     * @param stockCode
     * @return
     ***************************************************************/
    public static void analyze(String stockCode) {
        if (!StockDataHolder.getInstance().containStock(stockCode)) {
            return;
        }
        for (String statisticType : Constant.CUSTOMSTATISTICSTYPES) {
            analyze(stockCode, statisticType);
        }
    }

    /****************************************************************
     * /*Analyze data with strategy
     * 
     * @param stockCode
     * @return
     ***************************************************************/
    public static void analyze(String stockCode, String statisticType) {
        if (!StockDataHolder.getInstance().containStock(stockCode)) {
            return;
        }
        if (!StockDataHolder.getInstance().get(stockCode)
                .containArray(statisticType)) {
            return;
        }
        StockStatisticResult stockResult = null;
        if (StatisticResultManager.getInstance().contains(stockCode)) {
            stockResult = StatisticResultManager.getInstance().getResult(
                    stockCode);
        } else {
            stockResult = new StockStatisticResult(stockCode);
            StatisticResultManager.getInstance().add(stockCode, stockResult);
        }
        if (stockResult == null) {
            return;
        }
        DataArray buySellArray = null;

        try {
            buySellArray = StockDataHolder.getInstance().get(stockCode)
                    .getDataArray(statisticType);
            
            String buyStatisticType = buySellArray.getBuyStatisticType();
            DataArray buyPriceArray = StockDataHolder.getInstance().get(stockCode)
                    .getDataArray(buyStatisticType);
            String sellStatisticType = buySellArray.getSellStatisticType();
            DataArray sellPriceArray = StockDataHolder.getInstance().get(stockCode)
                    .getDataArray(sellStatisticType);
            
            DataArray closePriceArray = StockDataHolder.getInstance().get(stockCode)
                    .getDataArray(Constant.CLOSE);

            DataArray openPriceArray = StockDataHolder.getInstance().get(stockCode)
                    .getDataArray(Constant.OPEN);

            if (buySellArray != null && buyPriceArray != null && sellPriceArray!=null
                    && buySellArray.size() > 0) {
                StatisticResult result = new StatisticResult();
                float gain = gainWithDelay(buySellArray, buyPriceArray, sellPriceArray, closePriceArray, openPriceArray,
                        Constant.DELAY0);
                

                result.setGain(gain);
                if (gain < 100f) {
                    result.setLost(100f - gain);
                } else {
                    result.setLost(0f);
                }
                if (gain > 100f) {
                    result.setNetGain(gain - 100f);
                } else {
                    result.setNetGain(0f);
                }
                

                stockResult.add(statisticType, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************************************************************
     * /*for stock that to buy today, Analyze data with strategy
     * 
     * @param stockCode
     * @return
     ***************************************************************/
    private static void findAnalyze(String stockCode, String statisticType,
            String endDate) {
        if (!StockDataHolder.getInstance().containStock(stockCode)) {
            return;
        }
        if (!StockDataHolder.getInstance().get(stockCode)
                .containArray(statisticType)) {
            return;
        }
        try {
            DataArray buySellArray = StockDataHolder.getInstance()
                    .get(stockCode).getDataArray(statisticType);
            int keyDate = buySellArray.size() - 1;
            if (keyDate == -1) {
                return;
            }
            for (int i = buySellArray.size() - 1; i >= 0; i--) {
                if (Integer.valueOf(buySellArray.getDate(i)) <= Integer
                        .valueOf(endDate)) {
                    keyDate = i;
                    break;
                }
            }
            // System.out.println("key date"+keyDate+"buy sell:"+Utils.format3(buySellArray.getValue(keyDate)));
            if (buySellArray.getValue(keyDate) > 0.0f) {
                // System.out.println("stock code to buy :"+buySellArray.getStockCode()+buySellArray.getStatisticType()+buySellArray.getDate(keyDate)+" enddate:"+endDate);
                analyze(stockCode, statisticType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****************************************************************
     * /*for stock that to buy today, Analyze data with all strategy
     * 
     * @param stockCode
     * @return
     ***************************************************************/
    public static void findAnalyze(String stockCode, String endDate) {
        if (!StockDataHolder.getInstance().containStock(stockCode)) {
            return;
        }
        for (String strategy : Constant.CUSTOMSTATISTICSTYPES) {
            if (!StockDataHolder.getInstance().get(stockCode)
                    .containArray(strategy)) {
                continue;
            }
            findAnalyze(stockCode, strategy, endDate);
        }
    }

    private static float gainWithDelay(DataArray buySellArray, DataArray buyPrice, DataArray sellPrice, DataArray closePrice, DataArray openPrice,
            int delay) throws Exception {


    	float gain = 100f;
        // new account
        AccountMgr account = new AccountMgr();
        if (closePrice.size() > delay) {
            for (int i = delay; i < closePrice.size(); i++) {
            	int keyDay = i-delay;
                boolean toSkip = false;
                if (buySellArray.getValue(keyDay) > 0) {
                    // to buy
                    double toBuyPrice = buyPrice.getValue(i);
//                    if (i<closePrice.size()-1){
//                        double nextDayOpen = openPrice.getValue(i + 1);
//                        if (nextDayOpen > toBuyPrice){
//                            toBuyPrice = nextDayOpen;
////                            toSkip = true;
//                        }
//                    }

                    if (!toSkip){
                        account.buy(buySellArray.getStockCode(), toBuyPrice,
                                buySellArray.getValue(keyDay));
                    }
                } else if (buySellArray.getValue(keyDay) < 0) {
                    // to sell
                    account.sell(buySellArray.getStockCode(),
                    		sellPrice.getValue(i), buySellArray.getValue(keyDay));
                }
            }

            // Calculate gain
            int finalValue = account
                    .valueAtPrice(closePrice.getValue(closePrice.size() - 1));
            gain = 100f * finalValue / Constant.ACCOUNTINITMONEY;
        }
        return gain;
    }

    /****************************************************************
     * /*sort statistic result data for all stockCodes
     * 
     * sort on gain by default, save the statistic type name into a LinkedList
     * 
     * @return
     * 
     * 
     ***************************************************************/
    public static List<String> sortStrategy() { // sort on gain by default
        Map<String, Double> strategyGain = new HashMap<>();
        Arrays.stream(Constant.CUSTOMSTATISTICSTYPES).forEach(s -> {
            double gain = getMeanGain(s);
            strategyGain.put(s, gain);
        });

        Set<String> treeSet = new TreeSet<>((s1, s2) -> {
            Double g1 = strategyGain.get(s1);
            Double g2 = strategyGain.get(s2);
            int gainCompare = g1.compareTo(g2);
            if (gainCompare != 0) {
                return gainCompare;
            } else {
                return s1.compareTo(s2);
            }
        });

        Arrays.stream(Constant.CUSTOMSTATISTICSTYPES).forEach(treeSet::add);

        return treeSet.stream().collect(Collectors.toList());
    }

    /****************************************************************
     * /*sort stockCode result data for a statistic type
     * 
     * sort on gain by default, save the statistic type name into a LinkedList
     * length an int > 0 , default is 20
     * 
     * @return
     * 
     * 
     ***************************************************************/
    public static List<String> sortStock(String statisticType, int length) { // sort on gain by default
        if (length <= 0)
            return new ArrayList<>();

//        Map<String, Double> stockGain = new HashMap<>();
//        Map<String, Integer> stockIndex = new HashMap<>();
//        Set<String> treeSet = new TreeSet<>((s1, s2) -> {
//            Double g1 = stockGain.get(s1);
//            Double g2 = stockGain.get(s2);
//            Integer i1 = stockIndex.get(s1);
//            Integer i2 = stockIndex.get(s2);
//            int gainCompare = g1.compareTo(g2);
//            if (gainCompare != 0) {
//                return -gainCompare; // desc
//            } else {
//                return i1.compareTo(i2);
//            }
//        });
//
//        int i = 0;
//        for (String stockCode : StatisticResultManager.getInstance().getStatisticResults().keySet()) {
//            StockStatisticResult stockResult = StatisticResultManager.getInstance().getResult(stockCode);
//            if (!stockResult.contains(statisticType)) {
//                continue;
//            }
//            stockIndex.put(stockCode, i++);
//            double gain = stockResult.getResult(statisticType).getGain();
//            stockGain.put(stockCode, gain);
//            treeSet.add(stockCode);
//        }
//        List<String> list = treeSet.stream().collect(Collectors.toList());
//        return list.subList(0, Math.min(list.size(), length));

        LinkedList<String> list = new LinkedList<>(); // String for stock code
        for (String stockCode : StatisticResultManager.getInstance()
                .getStatisticResults().keySet()) {
            StockStatisticResult stockResult = StatisticResultManager
                    .getInstance().getResult(stockCode);
            if (!stockResult.contains(statisticType)) {
                // System.out.println("[sortStock] stockCode:"+stockCode+" statisticType:"+statisticType);
                continue;
            }
            float gain = stockResult.getResult(statisticType).getGain();
            if (list.size() == 0) {
                list.addFirst(stockCode);
            } else {
                boolean inserted = false;
                for (int i = 0; i < list.size(); i++) {
                    if (gain > StatisticResultManager.getInstance()
                            .getResult(list.get(i)).getResult(statisticType)
                            .getGain()) {
                        list.add(i, stockCode);
                        inserted = true;
                        break;
                    }
                }
                if (!inserted) {
                    list.addLast(stockCode);
                    inserted = true;
                }
                while (list.size() > length) {
                    list.removeLast();
                }
            }
        }

        return list;
    }

    public static LinkedList<String> sortStockNeg(String statisticType,
            int length) { // sort on gain by default,
        if (length <= 0)
            return null;
        LinkedList<String> list = new LinkedList<>(); // String for
                                                            // statistic Type
        for (String stockCode : StatisticResultManager.getInstance()
                .getStatisticResults().keySet()) {
            StockStatisticResult stockResult = StatisticResultManager
                    .getInstance().getResult(stockCode);
            if (!stockResult.contains(statisticType)) {
                // System.out.println("[sortStock] stockCode:"+stockCode+" statisticType:"+statisticType);
                continue;
            }
            float gain = stockResult.getResult(statisticType).getGain();
            if (list.size() == 0) {
                list.addFirst(stockCode);
            } else {
                boolean inserted = false;
                for (int i = 0; i < list.size(); i++) {
                    if (gain < StatisticResultManager.getInstance()
                            .getResult(list.get(i)).getResult(statisticType)
                            .getGain()) {
                        list.add(i, stockCode);
                        inserted = true;
                        break;
                    }
                }
                if (!inserted) {
                    list.addLast(stockCode);
                    inserted = true;
                }
                while (list.size() > length) {
                    list.removeLast();
                }
            }
        }
        return list;
    }

    public static float getMeanGain(String statisticType) {
        HashMap<String, StockStatisticResult> statisticResults = StatisticResultManager
                .getInstance().getStatisticResults();
        float allGain = 0f;
        float tempGain = 0f;
        int gainCounter = 0;
        int loseCounter = 0;
        for (String stock : statisticResults.keySet()) {
        	if (statisticResults.get(stock) != null && statisticResults.get(stock).getResult(statisticType)!=null){
        		tempGain = statisticResults.get(stock).getResult(statisticType)
                        .getGain();
                if (tempGain > 100f) {
                    gainCounter++;
                    allGain += tempGain;
                } else if (tempGain < 100f) {
                    loseCounter++;
                    allGain += tempGain;
                }
        	}
        }
        return (gainCounter + loseCounter)>0?allGain / (gainCounter + loseCounter):0;
    }

    public static float getMeanGainDelay(String statisticType) {
        HashMap<String, StockStatisticResult> statisticResults = StatisticResultManager
                .getInstance().getStatisticResults();
        float allGainDelay = 0f;
        float tempGainDelay = 0f;
        int gainCounter = 0;
        int loseCounter = 0;
        for (String stock : statisticResults.keySet()) {
        	if (statisticResults.get(stock)!=null && statisticResults.get(stock).getResult(statisticType)!=null){
        		tempGainDelay = statisticResults.get(stock)
                        .getResult(statisticType).getGainDelay();
                if (tempGainDelay > 100f) {
                    gainCounter++;
                    allGainDelay += tempGainDelay;
                } else if (tempGainDelay < 100f) {
                    loseCounter++;
                    allGainDelay += tempGainDelay;
                }
        	}
        }
        return (gainCounter + loseCounter)>0?allGainDelay / (gainCounter + loseCounter):0;
    }

    public static float getAllLost(String statisticType) {
        HashMap<String, StockStatisticResult> statisticResults = StatisticResultManager
                .getInstance().getStatisticResults();
        float allLost = 0f;
        for (String stock : statisticResults.keySet()) {
        	if (statisticResults.get(stock)!=null && statisticResults.get(stock).getResult(statisticType)!=null){
        		allLost += statisticResults.get(stock).getResult(statisticType)
                        .getLost();
        	}
        }
        return allLost;
    }

    public static float getAllNetGain(String statisticType) {
        HashMap<String, StockStatisticResult> statisticResults = StatisticResultManager
                .getInstance().getStatisticResults();
        float allNetGain = 0f;
        for (String stock : statisticResults.keySet()) {
        	if (statisticResults.get(stock)!=null && statisticResults.get(stock).getResult(statisticType)!=null){
        		allNetGain += statisticResults.get(stock).getResult(statisticType)
                        .getNetGain();
        	}
        }
        return allNetGain;
    }

    public static float getAllLostDelay(String statisticType) {
        HashMap<String, StockStatisticResult> statisticResults = StatisticResultManager
                .getInstance().getStatisticResults();
        float allLostDelay = 0f;
        for (String stock : statisticResults.keySet()) {
        	if (statisticResults.get(stock)!=null && statisticResults.get(stock).getResult(statisticType)!=null){
        		allLostDelay += statisticResults.get(stock)
                        .getResult(statisticType).getLostDelay();
        	}
        }
        return allLostDelay;
    }

    private DataAnalyzer() {

    }

}
