/**
 * 
 */
package com.stockstrategy.favoritestocks;

/**
 * @author ejixdai
 *	store the detail buy sell info for one stock
 */
public class StockParamBean implements IStockParam {
	private String addDate ="";
	private String buyDate = "";
	private String sellDate = "";
	private double buyPrice = 0f;
	private double currentPrice = 0f;
	private double toSellBuy = 0;
	
	
	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public String getAddDate() {
		return this.addDate;
	}
	
	/* (non-Javadoc)
	 * @see com.stock.account.IStockParam#setABuyDate(java.lang.String)
	 */
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IStockParam#getBuyDate()
	 */
	public String getBuyDate() {
		return buyDate;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IStockParam#setSellDate(java.lang.String)
	 */
	public void setSellDate(String sellDate) {
		this.sellDate = sellDate;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IStockParam#getSellDate()
	 */
	public String getSellDate() {
		return sellDate;
	}

	public StockParamBean(){
		
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double price) {
		this.buyPrice = price;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double price) {
		this.currentPrice = price;
	}

	public void setSellBuy(double toSellBuy) {
		this.toSellBuy = toSellBuy;
	}

	public double getSellBuy() {
		return this.toSellBuy;
	}

	
}
