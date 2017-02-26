package com.djx.stockgainanalyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public abstract class ExecuteThread implements Callable<Map<String, Object>> {
	private CountDownLatch latch;
//	private double[][] params;
	
	protected double[][] offsetParams;
	protected double[][] scaleParams;
	
	private Map<String, Object> result = new HashMap<String, Object>();
	
	public ExecuteThread( CountDownLatch latch, int length, double[][] offsetParams, double[][] scaleParams){
		this.offsetParams = new double[offsetParams.length][length];
		this.scaleParams = new double[scaleParams.length][length];
		
		for (int i=0;i<offsetParams.length;i++){
			this.offsetParams[i] = new double[length];
			this.scaleParams[i] = new double[length];
			for (int j=0;j<length;j++){
				this.offsetParams[i][j] = offsetParams[i][j];
				this.scaleParams[i][j] = scaleParams[i][j];
			}
		}
		
		this.latch = latch;
	}
	
	public ExecuteThread( CountDownLatch latch, int length){
		this.latch = latch;
	}
	
	
	@Override
	public Map<String, Object> call() throws Exception {
		work(offsetParams, scaleParams, result);
		latch.countDown();
		return result;
	}
	
	public abstract Map<String, Object> work(double[][] offsetParams, double[][] scaleParams, Map<String, Object> result); 

}
