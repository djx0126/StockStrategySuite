package com.stockstrategy.data;


/**
 * @author Administrator
 *	class for RawData like high, low, start, end, and quantity of the day;
 * RawData(String date, double value);
 * 
 */
public class RawData implements IData{

	private String date;
	private double value;
	
	@Override
	public String getDate() {
		return date;
	}
	@Override
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public double getValue() {
		return value;
	}
	@Override
	public void setValue(double data) {
		this.value = data;
	}
	
	public RawData(String date, double value)
	{
		this.date = date;
		this.value = value;
	}
	

}
