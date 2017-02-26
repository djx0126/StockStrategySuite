/**
 * 
 */
package com.stockstrategy.favoritestocks;

/**
 * @author ejixdai
 *	stock account manager, interface to a database store stocks in hand
 *
 */
public interface IStockAccountMgr {
	public void init();
	public void clear();
	public String[] getStockList();
	public boolean contains(String stock);
	public IStockParam getStockDetail(String stock);
	public void addStock(String stock, IStockParam stockParam);// addDate is the date add to the database
	public void removeStock(String stock);
	public void setStock(String stock, IStockParam stockParam);
	
}


