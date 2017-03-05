package com.stockstrategy.simulator.aggregate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.StockDataHolder;


public class BuySellDetailCollector extends AbstractAggregate {
	
	private Map<String, List<Transaction>> statisticsTransactionMap = new HashMap<>();
	
	
	public BuySellDetailCollector() {
		this("BuySellDetailCollector");
	}
	
	protected BuySellDetailCollector(String name) {
		super(name);
	}

	@Override
	public void update(String stockCode, String startDate, String endDate) {
		try {
			for(String statisticType:Constant.CUSTOMSTATISTICSTYPES){
				updateOneStatisticType(stockCode, statisticType, startDate, endDate);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void updateOneStatisticType(String stockCode, String statisticType, String stateDate, String endDate) throws Exception{
		List<Transaction> transactions;
		if (statisticsTransactionMap.containsKey(statisticType)){
			transactions = statisticsTransactionMap.get(statisticType);
		}else{
			transactions = new ArrayList<>();
			statisticsTransactionMap.put(statisticType, transactions);
		}
		
		DataArray buySellArray = StockDataHolder.getInstance().get(stockCode)
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
        
        boolean inHand = false;
        List<Transaction> inHandList = new ArrayList<>();
        double buyPrice = 0.0d;
        double sellPrice = 0.0d;
        String buyDate = "";
        String sellDate = "";
        for (int i = 0;i< buySellArray.size();i++){
        	
        	String date = buySellArray.getDate(i);
        	if (date.compareTo(stateDate)<0){
        		continue;
        	}else if (date.compareTo(endDate)>0){
        		break;
        	}else if (date.equals(endDate)){
        		if (inHand){
                    inHand = false;
        			sellDate = date;
            		sellPrice = sellPriceArray.getValue(i);
                    for (Transaction t:inHandList) {
                        t.sellDate = sellDate;
                        t.sellPrice = sellPrice;
                        transactions.add(t);
                    }
                }
        		break;
        	}
        	
        	double actionCode = buySellArray.getValue(i);
        	if (actionCode>0){
				boolean toSkip = false;
				buyPrice = buyPriceArray.getValue(i);
//				if (i<buySellArray.size()-1){
//					double nextDayOpen = openPriceArray.getValue(i + 1);
//					if (nextDayOpen > buyPrice){
//						buyPrice = nextDayOpen;
//
//						//toSkip
////						toSkip = true;
//					}
//				}
				if (!toSkip){
                    buyDate = date;
                    Transaction t = new Transaction(stockCode, statisticType, buyDate, sellDate, buyPrice, sellPrice, inHand);
                    inHand = true;
                    inHandList.add(t);
                }
        	}else if(inHand && actionCode<0){
                Transaction t = inHandList.remove(0);
                inHand = inHandList.size() > 0;
                t.sellDate = date;
                t.sellPrice = sellPriceArray.getValue(i);
                transactions.add(t);
        	}
        }
        
        if (inHand){
        	inHand = false;
        	sellDate = closePriceArray.getDate(closePriceArray.size()-1);
    		sellPrice = closePriceArray.getValue(closePriceArray.size()-1);
    		if (sellDate.compareTo(endDate)<0){ // ������ͣ��
    			int index = closePriceArray.getIndexByDate(sellDate);
    			sellDate = endDate;
    			sellPrice = closePriceArray.getValue(index);
    		}

            for (Transaction t:inHandList) {
                t.sellDate = sellDate;
                t.sellPrice = sellPrice;
                transactions.add(t);
            }
    	}
        
	}

	@Override
	public void reset() {
		statisticsTransactionMap = new HashMap<>();
	}

	@Override
	public Object getResult() {
		return statisticsTransactionMap;
	}
	
	public static class Transaction{
		String stockCode;
		String statisticType;
		String buyDate;
		String sellDate;
		double buyPrice;
		double sellPrice;
		boolean inHandBefore;
		
		public Transaction(String stockCode, String statisticType, String buyDate, String sellDate, double buyPrice, double sellPrice) {
			this.stockCode = stockCode;
			this.statisticType = statisticType;
			this.buyDate = buyDate;
			this.sellDate = sellDate;
			this.buyPrice = buyPrice;
			this.sellPrice = sellPrice;
		}

		public Transaction(String stockCode, String statisticType, String buyDate, String sellDate, double buyPrice, double sellPrice, boolean inHandBefore) {
			this.stockCode = stockCode;
			this.statisticType = statisticType;
			this.buyDate = buyDate;
			this.sellDate = sellDate;
			this.buyPrice = buyPrice;
			this.sellPrice = sellPrice;
			this.inHandBefore = inHandBefore;
		}

		public String getStockCode() {
			return stockCode;
		}

		public String getStatisticType() {
			return statisticType;
		}

		public String getBuyDate() {
			return buyDate;
		}

		public String getSellDate() {
			return sellDate;
		}

		public double getBuyPrice() {
			return buyPrice;
		}

		public double getSellPrice() {
			return sellPrice;
		}
		
		
	}

}
