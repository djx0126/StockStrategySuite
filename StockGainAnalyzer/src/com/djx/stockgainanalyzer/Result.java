package com.djx.stockgainanalyzer;

public class Result {
//	private static final double MIN = -1000000000.0d;
//	private static final double E = 1e-4;
	private double sum;
	private double lost;
	private double avgGain;
	private double rate;
	private int count;
	private double accuracy;
	
	
	public double getTargetResult(){
		
//		return avgGain*avgGain*avgGain*Math.pow(count, 0.75f);  //origin
//		return avgGain*avgGain*avgGain*Math.pow(count, 0.25f);
//		return avgGain*avgGain*Math.pow(avgGain, 1.2f)*Math.pow(count, 0.6f);
//		double value = avgGain*avgGain*Math.pow(Math.abs(avgGain), 1.5f)*Math.pow(count, 0.6f);
//		return (avgGain > 0? value:-value)-Math.pow(Math.log1p(lost), 3.0f);
		//return avgGain*avgGain*avgGain*Math.pow(count, 0.5f)-Math.pow(Math.log1p(lost), 1.0f);
//		return avgGain*Math.pow(count, 0.3f)-Math.pow(Math.log1p(lost), 1.0f);
		
		double value;// = avgGain*avgGain*avgGain*Math.pow(count, 0.75f)-Math.pow(Math.log1p(lost), 2.5f);
		
		value = avgGain*avgGain*avgGain*Math.pow(count, 0.25f);
		
		return value >0 ?value*this.accuracy/100 : value;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%.3f", this.getTargetResult()));
		sb.append("(");
		sb.append("avgGain=").append(String.format("%.3f", avgGain)).append("%");
		sb.append(",sum=").append(String.format("%.3f", sum));
		sb.append(",lost=").append(String.format("%.3f", lost));
		sb.append(",rate=").append(String.format("%.3f", rate));
		sb.append(",count=").append(String.format("%d", count));
		sb.append(",accuracy=").append(String.format("%.3f", accuracy));
		sb.append(")");
		return sb.toString();
	}
	
	public boolean isValid(){
		return (getTargetResult()>1000 ||accuracy>=80) && this.count>100;
//		return getTargetResult()>5000;
	}
	
	
	
	public Result(){
		
	}



	public double getSum() {
		return sum;
	}



	public void setSum(double sum) {
		this.sum = sum;
	}



	public double getAvgGain() {
		return avgGain;
	}



	public void setAvgGain(double avgGain) {
		this.avgGain = avgGain;
	}



	public double getRate() {
		return rate;
	}



	public void setRate(double rate) {
		this.rate = rate;
	}



	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}

	public double getLost() {
		return lost;
	}

	public void setLost(double lost) {
		this.lost = lost;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
}
