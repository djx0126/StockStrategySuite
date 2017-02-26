/**
 * 
 */
package com.stockstrategy.account;

/**
 * @author Administrator
 * a virtual account for one channel ----- means one stock
 */
public class AccountMgr implements IAccountMgr{
	private IProfile profile= null;
	private IAccount account = null;
	
	
	public int valueAtPrice(double price)
	{
		return (int) (price*100*account.getStock() + account.getMoney());
	}
	
	public void sell(String stockCode, double price, double atctionCode)
	{
		profile.sell(account, price, atctionCode);
	}
	
	public void buy(String stockCode, double price, double atctionCode)
	{
		profile.buy(account, price, atctionCode);
	}
	
	
	

	
	public IProfile getProfile() {
		return profile;
	}

	public void setProfile(IProfile profile) {
		this.profile = profile;
	}

	public AccountMgr()
	{
//		profile = new ProfileAll();
		profile = new ProfileBuyCode();
		account = new AccountBean();
	}
	
	public AccountMgr(IProfile profile)
	{
		this.profile = profile;
		account = new AccountBean();
	}
}
