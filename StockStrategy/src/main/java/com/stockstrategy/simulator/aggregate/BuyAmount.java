package com.stockstrategy.simulator.aggregate;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.StockDataHolder;

public class BuyAmount extends AbstractAggregate {
	public static final String BUYAMOUNT = "BuyAmount";
	private double buyAmount=0.0;
	private String statisticType=Constant.SLevel5;

	public  BuyAmount() {
		super(BUYAMOUNT);
	}
	
	public  BuyAmount(String statisticType) {
		super(BUYAMOUNT+"."+statisticType);
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
	            	buyAmount+=toBuy;
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reset() {
		buyAmount=0.0;
	}

	@Override
	public Object getResult() {
		return buyAmount;
	}

}
