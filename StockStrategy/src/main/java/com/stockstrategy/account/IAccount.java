package com.stockstrategy.account;

public interface IAccount {
	public void init();
	public int getMoney();
	public void setMoney(int money);
	public int getStock();
	public void setStock(int stock);
}
