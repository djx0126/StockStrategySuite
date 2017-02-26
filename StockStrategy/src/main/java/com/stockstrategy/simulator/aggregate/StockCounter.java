package com.stockstrategy.simulator.aggregate;

public class StockCounter extends AbstractAggregate {
	public static final String STOCKCOUNTER= "StockCounterAggregater";
	private int stockCount=0;

	public StockCounter() {
		super(STOCKCOUNTER);
	}

	
	@Override
	public void reset() {
		stockCount=0;
	}

	@Override
	public Object getResult() {
		// TODO Auto-generated method stub
		return stockCount;
	}

	@Override
	public void update(String stockCode, String startDate, String endDate) {
		// TODO Auto-generated method stub
		stockCount++;
	}

}
