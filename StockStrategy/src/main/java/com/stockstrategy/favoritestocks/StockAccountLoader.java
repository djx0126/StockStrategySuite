package com.stockstrategy.favoritestocks;

import com.stockstrategy.constant.Configurer;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.StockDataHolder;
import com.stockstrategy.simulator.Simulator;
import com.stockstrategy.tools.Utils;

public class StockAccountLoader implements Runnable {
	//private String stockCode = "";
	private String startDate = "-";
	private String endDate = "";
	private String statisticType = null;
	private IStockAccountMgr myStockAccountMgr = null;
	
	public  static final int STOUPDATE = 0;
	public static final int SUPDATING = 1;
	public static final int SUPDATED = 2;
	private static int status = STOUPDATE;
	private static String lastUpdatedDate ;
	
	private static final String LASTUPDATEDATECONFIGNAME = "lastUpdateDate";
	private static final String TAGFAVORSTRATEGY = "FavorStrategy";
	
	public static String getLastUpdatedDate(){
		loadLastUpdatedDate();
		return lastUpdatedDate;
	}
	
	private static void loadLastUpdatedDate(){
//		String date = Configurer.getInstance().getConfig(LASTUPDATEDATECONFIGNAME);
//		if (date!=null && !date.equals("")){
//			lastUpdatedDate=date;
//		}else{
//			lastUpdatedDate="19900101"; // an early date
//		}
		lastUpdatedDate=Configurer.getInstance().getConfig(LASTUPDATEDATECONFIGNAME,"19900101");
	}
	
	public static int getUpdateStatus(){
		String today=Utils.today();
		if (status != SUPDATING && getLastUpdatedDate().compareTo(today)<0){
			status=STOUPDATE;
		}
		return status;
	}
	
	public static void excute( IStockAccountMgr _myStockAccountMgr){
		if (getUpdateStatus() == STOUPDATE){
			status = SUPDATING;
			new Thread(new StockAccountLoader(_myStockAccountMgr)).start();
		}
	}
	
	public StockAccountLoader(IStockAccountMgr _myStockAccountMgr){
		this.myStockAccountMgr = _myStockAccountMgr;
		String today=Utils.today();
		endDate = today;
		statisticType = Configurer.getInstance().getConfig(TAGFAVORSTRATEGY,Constant.CUSTOMSTATISTICSTYPES[0]);
	}
	
	public void run(){
		StockDataHolder.getInstance().clear();
		String [] stockList =null;
		stockList = DefaultStockAccountMgr.getInstance().getStockList();
		String lastDataDate = endDate;
		for ( String stock : stockList ){
			IStockParam stockDetail = myStockAccountMgr.getStockDetail(stock);
			if (stockDetail==null){
				continue;
			}
			Simulator.actualExecuteFind(stock, startDate, endDate);
			
			DataMap dataMap = StockDataHolder.getInstance().get(stock);
			if (dataMap == null){
				continue;
			}
			try {
				DataArray close = dataMap.getDataArray(Constant.CLOSE);
				DataArray buySell = dataMap.getDataArray(statisticType);
				
				if( close==null || buySell==null ){
					continue;
				}
				
				// buy date and buy price
				int lastBuyIndex = close.size()-1;
				int i = close.size()-1;
				boolean hasBuyIndex = false;
				while ( i >=0){
					if ( buySell.getValue(i)>0 ){
						lastBuyIndex = i;
						hasBuyIndex = true;
					}
					if (hasBuyIndex && buySell.getValue(i)<0){
						break;
					}
					i--;
				}
				stockDetail.setBuyDate(close.getDate(lastBuyIndex));
				stockDetail.setBuyPrice(close.getValue(lastBuyIndex));
				
				//current price
				stockDetail.setCurrentPrice(close.getValue(close.size()-1));
				
				//sell date
				int lastSellIndex = close.size()-1;
				while ( lastSellIndex >=0){
					if ( buySell.getValue(lastSellIndex)<0 ){
						break;
					}
					lastSellIndex--;
				}
				stockDetail.setSellDate(close.getDate(lastSellIndex));
				
				//to sell
				if( buySell.getValue(buySell.size()-1)<0 ){
					stockDetail.setSellBuy(buySell.getValue(buySell.size()-1));
				}else if (buySell.getValue(buySell.size()-1)>0){
					stockDetail.setSellBuy(buySell.getValue(buySell.size()-1));
				}else{
					stockDetail.setSellBuy(buySell.getValue(buySell.size()-1));
				}
				
				//set stock to data base
				myStockAccountMgr.setStock(stock, stockDetail);
				
				lastDataDate=close.getDate(close.size()-1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			StockDataHolder.getInstance().remove(stock);
		}// end of for stock:stockList
		
		//System.out.println(LASTUPDATEDATECONFIGNAME+"/"+lastDataDate);
		if(Configurer.getInstance().setConfig(LASTUPDATEDATECONFIGNAME, lastDataDate)){
			lastUpdatedDate=lastDataDate;
			status = SUPDATED;
		}else{
			status = STOUPDATE;
		}
		
	}//end of run function
	
	
}
