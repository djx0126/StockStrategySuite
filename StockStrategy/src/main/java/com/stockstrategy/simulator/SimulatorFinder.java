package com.stockstrategy.simulator;


public class SimulatorFinder extends SimulatorRunner{
	@Override
	protected void execute(String stockCode, String startDate, String endDate){
		Simulator.actualExecuteFind(stockCode, startDate, endDate);
		
	}

}
