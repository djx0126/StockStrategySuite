/**
 * 
 */
package com.stockstrategy.data;

/**
 * @author Administrator
 *	class for Integer like 1,0, -1;
 * IntData(String date, int value);
 * while using setValue(float f), the value=(int)f ;
 * 
 * used by custom statistics to store buy or sell information;
 */
public class IntData implements IData {

	private String date;
	private int value;
	
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
	
	
	public IntData(String date, int value)
	{
		this.date = date;
		this.value = value;
	}
	@Override
	public void setValue(double f) {
		this.value = (int)f;
	}

}
