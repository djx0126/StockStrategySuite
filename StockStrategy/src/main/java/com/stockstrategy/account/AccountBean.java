/**
 * 
 */
package com.stockstrategy.account;

import com.stockstrategy.constant.Constant;

/**
 * @author ejixdai
 * an account for money and money( store the number, not the type)
 */
public class AccountBean implements IAccount {
	private int money = Constant.ACCOUNTINITMONEY;
	private int stock = 0; // 100stock as 1 unit 
	/* (non-Javadoc)
	 * @see com.stock.account.IAccount#getMoney()
	 */
	public int getMoney() {
		return money;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IAccount#setMoney(int)
	 */
	public void setMoney(int money) {
		this.money = money;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IAccount#getStock()
	 */
	public int getStock() {
		return stock;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IAccount#setStock(int)
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}

	public void init() {
		money = Constant.ACCOUNTINITMONEY;
		stock = 0;
	}

}
