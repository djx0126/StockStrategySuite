package com.stockstrategyanalyzer.job.strategytest;

import java.util.List;

import com.stockstrategy.simulator.aggregate.BuySellDetailCollector.Transaction;
import com.stockstrategy.tools.Utils;

public class OneStrategyResult {
	private String strategy;
	
	private float meanGain;
	private float allLost;
	private float netGain;
	private int gainCounter;
	private int lostCounter;
	private int transactionGainCounter;
	private int transactionLostCounter;
	private float transactionGain;
	private float transactionLost;
	private List<Transaction> transactions;
	
	


	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}


	public OneStrategyResult(String strategy){
		this.strategy = strategy;
	}
	
	
	public String getStrategy() {
		return strategy;
	}

	public float getMeanGain() {
		return meanGain;
	}

	public OneStrategyResult setMeanGain(float meanGain) {
		this.meanGain = meanGain;
		return this;
	}

	public float getAllLost() {
		return allLost;
	}

	public OneStrategyResult setAllLost(float allLost) {
		this.allLost = allLost;
		return this;
	}

	public float getNetGain() {
		return netGain;
	}

	public OneStrategyResult setNetGain(float netGain) {
		this.netGain = netGain;
		return this;
	}

	public int getGainCounter() {
		return gainCounter;
	}

	public OneStrategyResult setGainCounter(int gainCounter) {
		this.gainCounter = gainCounter;
		return this;
	}

	public int getLostCounter() {
		return lostCounter;
	}

	public OneStrategyResult setLostCounter(int lostCounter) {
		this.lostCounter = lostCounter;
		return this;
	}
	
	public int getTransactionGainCounter() {
		return transactionGainCounter;
	}

	public OneStrategyResult setTransactionGainCounter(int transactionGainCounter) {
		this.transactionGainCounter = transactionGainCounter;
		return this;
	}
	
	public int getTransactionLostCounter() {
		return transactionLostCounter;
	}

	public OneStrategyResult setTransactionLostCounter(int transactionLostCounter) {
		this.transactionLostCounter = transactionLostCounter;
		return this;
	}
	
	public float getTransactionGain() {
		return transactionGain;
	}


	public OneStrategyResult setTransactionGain(float transactionGain) {
		this.transactionGain = transactionGain;
		return this;
	}


	public float getTransactionLost() {
		return transactionLost;
	}


	public OneStrategyResult setTransactionLost(float transactionLost) {
		this.transactionLost = transactionLost;
		return this;
	}
	

	public List<Transaction> getTransactions() {
		return transactions;
	}


	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}


	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(strategy + ": ");
		sb.append(Utils.format3(meanGain) + "%" + " / " + Utils.format3(allLost) + "%");
		sb.append(" ( +" + String.valueOf(gainCounter) + "/-" + String.valueOf(lostCounter) + " )");
		
		float rate = netGain / allLost;
        String rateText = Utils.format3(rate);
        
        sb.append("  rate: " + String.valueOf(rateText));
        
        int transactionCounter = this.transactionGainCounter + this.transactionLostCounter;
        float transactionAccuracy = 100* (float)this.transactionGainCounter/(float)transactionCounter;
        
        sb.append("  accuracy: " + Utils.format3(transactionAccuracy)+"\n");
       
        return sb.toString();
	}
}
