package com.djx.stockgainanalyzer.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StockGainData implements IStockGain {
	private static AtomicInteger nextId=new AtomicInteger();   
    private int id;
    private String stockCode;
    private String keyDate;
    private double gain;
    private List<PreviousData> previousData;
    private RelativeMAData relativeMAData;
    private RelativeMAData relativeOverAllMAData;
    
    public StockGainData(String stockCode, String keyDate, double gain){
    	this.id = nextId.incrementAndGet();
        this.stockCode = stockCode;
        this.keyDate = keyDate;
        this.gain= gain;
        this.previousData = new ArrayList<>();
    }
    

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("StockGainData{id:");
    	sb.append(this.id);
    	sb.append(", stockCode:");
    	sb.append(this.stockCode);
    	sb.append(", keyDate:");
    	sb.append(this.keyDate);
    	sb.append(", Gain:");
    	sb.append(this.gain);
    	sb.append("}");
    	return sb.toString();
    }

    
    public int getId() {
        return this.id;
    }
    public void setId(int id){
    	this.id = id;
    }

    @Override
    public String getStockCode() {
        return stockCode;
    }

    public String getKeyDate() {
        return keyDate;
    }

    public void setKeyDate(String keyDate) {
        this.keyDate = keyDate;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }
    public void insertPreviousData(PreviousData data){
    	data.setStockDateKey(this.id);
        data.setOffset(this.previousData.size());
        this.previousData.add(data);
    }    
    public List<PreviousData> getPreviousData(){
        return this.previousData;
    }

    public void setRelativeMA(RelativeMAData relativeMA){
        this.relativeMAData = relativeMA;
    }

    public RelativeMAData getRelativeMA(){
        return this.relativeMAData;
    }

    public RelativeMAData getRelativeOverAllMAData() {
        return relativeOverAllMAData;
    }

    public void setRelativeOverAllMAData(RelativeMAData relativeOverAllMAData) {
        this.relativeOverAllMAData = relativeOverAllMAData;
    }
}
