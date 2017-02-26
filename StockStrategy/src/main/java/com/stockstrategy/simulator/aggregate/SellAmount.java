package com.stockstrategy.simulator.aggregate;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.StockDataHolder;

public class SellAmount extends AbstractAggregate {
	public static final String SELLAMOUNT = "SellAmount";
	private double sellAmount=0.0;
	private String statisticType=Constant.SLevel5;

	public  SellAmount() {
		super(SELLAMOUNT);
	}
	
	public  SellAmount(String statisticType) {
		super(SELLAMOUNT+"."+statisticType);
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
	            if (toBuy < 0) {
	            	sellAmount+=toBuy;
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reset() {
		sellAmount=0.0;
	}

	@Override
	public Object getResult() {
		return sellAmount;
	}

}
