/**
 * 
 */
package com.stockstrategy.account;

/**
 * @author Administrator
 * buy or sell as much as I can
 */
public class ProfileBuyCode implements IProfile {

	/* (non-Javadoc)
	 * @see com.stock.account.IProfile#buy(int, int, float)
	 */
	public int buy(IAccount account, double price, double atctionCode) {
		int money = account.getMoney();
		int stockInHand = account.getStock();
		int iprice = (int) Math.ceil(price *100);
		int stockToBuy = 0;
		if (Math.floor(money*atctionCode) >= iprice)
		{
			stockToBuy = (int)(Math.floor(money*atctionCode)) / iprice;
			account.setStock(stockInHand+stockToBuy);
			account.setMoney(money-stockToBuy*iprice);
		}
		return stockToBuy;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IProfile#sell(int, int, float)
	 */
	public int sell(IAccount account, double price, double atctionCode) {
		int money = account.getMoney();
		int stockInHand = account.getStock();
		int iprice = (int)(price *100);
		int stockToSell = (int) Math.floor(stockInHand);
		if (stockToSell >= 0)
		{
			
			account.setMoney(money+ (int)Math.floor(stockToSell*iprice));
			account.setStock(Math.max(0, stockInHand-stockToSell));
		}
		return stockToSell;
	}

}
