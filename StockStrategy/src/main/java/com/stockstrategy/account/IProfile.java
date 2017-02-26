/**
 * 
 */
package com.stockstrategy.account;

/**
 * @author Administrator
 * the interface for a profile to decide how much to buy or sell
 * 
 * the return integer is the real amount to buy/sell
 */
public interface IProfile {
	public int buy(IAccount acount, double price, double atctionCode);
	public int sell(IAccount acount, double price, double atctionCode);
}
