package com.stockstrategy.simulator.aggregate;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.StockDataHolder;

public class Ma5upCounter extends AbstractAggregate {
	private static final String MA5UPCOUNTER = "Ma5upCounterAggregater";
	private int ma5up=0;

	public  Ma5upCounter() {
		super(MA5UPCOUNTER);
	}

	
	@Override
	public void reset() {
		ma5up=0;
	}

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return ma5up;
	}

	@Override
	public void update(String stockCode, String startDate, String endDate) {
		// TODO Auto-generated method stub
		try {
			DataArray ma5 = StockDataHolder.getInstance().get(stockCode)
			        .getDataArray(Constant.MA5);
			DataArray ma10 = StockDataHolder.getInstance().get(stockCode)
	                .getDataArray(Constant.MA10);
			if (ma5!=null && ma10!=null && ma5.size()>0 && ma10.size()>0){
				double ma5Last = ma5.getValue(ma5.size() - 1);
		        double ma10Last = ma10.getValue(ma10.size() - 1);
		        if (ma5Last > ma10Last) {
		            ma5up++;
		        }
			}
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}

}
