package com.stockstrategy.simulator.aggregate;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.StockDataHolder;

public class BuyCounter extends AbstractAggregate {
	public static final String BUYCOUNTER = "BuyCounter";
	private int buyCount=0;
	private String statisticType=Constant.SLevel5;

	public  BuyCounter() {
		super(BUYCOUNTER);
	}
	
	public  BuyCounter(String statisticType) {
		super(BUYCOUNTER+"."+statisticType);
		this.statisticType = statisticType;
	}
	
	public  BuyCounter(String name, String statisticType) {
		super(name);
		this.statisticType = statisticType;
	}

	@Override
	public void update(String stockCode, String startDate, String endDate) {
		try {
			DataArray buySellArray = StockDataHolder.getInstance()
			        .get(stockCode).getDataArray(statisticType);
			//System.out.println("endDate="+endDate+","+buySellArray.getDate(buySellArray.size() - 1));
			if (buySellArray != null &&  buySellArray.size() > 0){
				double toBuy = buySellArray.getValue(buySellArray.size() - 1);
	            if (toBuy > 0) {
	            	buyCount++;
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reset() {
		buyCount=0;
	}

	@Override
	public Object getResult() {
		return buyCount;
	}

}
