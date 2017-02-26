package com.stockstrategy.simulator.aggregate;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.StockDataHolder;

public class InhandCounter extends AbstractAggregate {
	public static final String INHANDCOUNTER = "InhandCounter";
	private int inhandCount=0;
	private String statisticType=Constant.SLevel5;

	public  InhandCounter() {
		super(INHANDCOUNTER);
	}
	
	public  InhandCounter(String statisticType) {
		super(INHANDCOUNTER+"."+statisticType);
		this.statisticType = statisticType;
	}

	@Override
	public void update(String stockCode, String startDate, String endDate) {
		try {
			DataArray buySellArray = StockDataHolder.getInstance()
			        .get(stockCode).getDataArray(statisticType);
			if (buySellArray != null &&  buySellArray.size() > 0){
				double tmpAccount = 0;
	            for (int i = 0; i < buySellArray.size(); i++) {
	                double toBuyTmp = buySellArray.getValue(i);
	                if (tmpAccount < 1 && toBuyTmp > 0) {
	                    tmpAccount += toBuyTmp;
	                } else if (tmpAccount > 0 && toBuyTmp < 0) {
	                    tmpAccount += toBuyTmp;
	                }
	                if (tmpAccount < 0) {
	                    tmpAccount = 0;
	                }
	                if (tmpAccount > 1) {
	                    tmpAccount = 1;
	                }
	            }
	            if (tmpAccount > 0) {
	            	inhandCount++;
	            }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reset() {
		inhandCount=0;
	}

	@Override
	public Object getResult() {
		return inhandCount;
	}

}
