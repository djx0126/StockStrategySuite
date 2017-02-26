/**
 * 
 */
package com.stockstrategy.favoritestocks;

/**
 * @author ejixdai
 *	the interface for a stock, include such terms:
 *
 *
 */
public interface IStockParam {
	public void setAddDate(String addDate);
	public String getAddDate();
	public void setBuyDate(String buyDate);
	public String getBuyDate();
	public void setSellDate(String sellDate);//lase sell date
	public String getSellDate();
	public void setBuyPrice(double price);
	public double getBuyPrice();	
	public double getCurrentPrice();
	public void setCurrentPrice(double price);
	public void setSellBuy(double sellBuy);// 
	public double getSellBuy();
	
	public static final int TOSELL = -1; 
	public static final int TOBUY = 1;
	public static final int TOKEEP = 0;
}
