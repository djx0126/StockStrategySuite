package com.stockstrategy.account;

public interface IAccountMgr {
	public void sell(String stockCode, double price , double atctionCode);
	public void buy(String stockCode, double price, double atctionCode);
}
