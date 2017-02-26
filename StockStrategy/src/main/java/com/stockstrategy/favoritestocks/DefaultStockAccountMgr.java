/**
 * 
 */
package com.stockstrategy.favoritestocks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.tools.Utils;

/**
 * @author ejixdai
 * stock account with a text file
 * 
 * stock : addDate : buyDate : sellDate : buyPrice : currentPrice : toSell
 *   0        1          2          3           4            5        6
 * stock is unique and primary key
 * 
 * 
 * status: inservice / not inservice
 * status: toUpdate, updating, updated
 * 
 */
public class DefaultStockAccountMgr implements IStockAccountMgr {
	private static IStockAccountMgr instance = null;
	private HashMap<String, IStockParam> stockList=null ;
	
	private static boolean inService = false;
	
	
	
	/* (non-Javadoc)
	 * @see com.stock.account.IStockAccountMgr#init()
	 */
	public  void init() {
		//if (getService()){
			stockList=new HashMap<String, IStockParam>() ;
			File stockFile = new File(Constant.MGMT_DIR+Constant.SP+Constant.STOCK_LIST);
			if (!stockFile.exists()){
				try {
					stockFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			read();
			releaseService();
		//}
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IStockAccountMgr#clear()
	 */
	public  void clear() {
		if ( getService() ){
			stockList=null ;
			File stockFile = new File(Constant.MGMT_DIR+Constant.SP+Constant.STOCK_LIST);
			if (stockFile.exists()){
				stockFile.delete();
			}
			try {
				stockFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			releaseService();
		}
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IStockAccountMgr#getStockList()
	 */
	public String[] getStockList() {
		if ( stockList !=null ){
			return  stockList.keySet().toArray(new String[]{});
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IStockAccountMgr#getStockDetail(java.lang.String)
	 */
	public IStockParam getStockDetail(String stock) {
		if ( stockList!=null ){
			if ( stockList.containsKey(stock)){
				return stockList.get(stock);
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IStockAccountMgr#addStock(java.lang.String, com.stock.account.IStockParam)
	 */
	public  void addStock(String stock, IStockParam stockParam) {
		if ( getService() ){
			if(stockList!=null && stock!=null){
				if (!stockList.containsKey(stock)){
					stockList.put(stock, stockParam);
					write();
				}
			}
			releaseService();
		}
	}

	/* (non-Javadoc)
	 * @see com.stock.account.IStockAccountMgr#removeStock(java.lang.String)
	 */
	public  void removeStock(String stock) {
		if ( getService() ){
			//Log.d("removeStock", "removeStock1");
			if (stockList!=null){
				//Log.d("removeStock", "removeStock2");
				if (stockList.containsKey(stock)){
					//Log.d("removeStock", "removeStock3");
					stockList.remove(stock);
					//Log.d("removeStock", "removeStock4");
					write();
				}
			}
			releaseService();
		}
	}
	
	public  void setStock(String stock, IStockParam stockParam) {
		if ( getService() ){
			if(stockList!=null && stock!=null){
				if (!stockList.containsKey(stock)){
					stockList.put(stock, stockParam);
					write();
				}else{
					stockList.remove(stock);
					stockList.put(stock, stockParam);
					write();
				}
			}
			releaseService();
		}
	}
	
	protected  void read(){
		//load the list
		stockList = new HashMap<String, IStockParam>();
		BufferedReader  br = null;
		try {
			File stockFile = new File(Constant.MGMT_DIR+Constant.SP+Constant.STOCK_LIST);
			br = new BufferedReader(new FileReader(stockFile));
			String record = null;
			while( (record = br.readLine())!=null ){
				String[] data = record.split(":");
				for ( int i =0;i<data.length;i++){
					data[i]=data[i].trim();
				}
				IStockParam stockDetail = null;
				if (data[0]!=null && data[0]!=""){ //stock code
					stockDetail = new StockParamBean();
					if ( data.length >1 && data[1]!=null ){
						stockDetail.setAddDate(data[1]);
					}
					if (data.length >2 && data[2]!=null ){
						stockDetail.setBuyDate(data[2]);
					}
					if (data.length >3 && data[3]!=null ){
						stockDetail.setSellDate(data[3]);
					}
					if ( data.length >4 &&data[4]!=null ){
						if  (data[4].length()>0){
							stockDetail.setBuyPrice(Double.valueOf(data[4]));
						}else{
							stockDetail.setBuyPrice(new Double(0.0f));
						}
					}
					if ( data.length >5 &&data[5]!=null ){
						if  (data[5].length()>0){
							stockDetail.setCurrentPrice(Double.valueOf(data[5]));
						}else{
							stockDetail.setCurrentPrice(new Double(0.0f));
						}
					}
					if ( data.length >6 &&data[6]!=null ){
						if  (data[6].length()>0){
							if (data[6].equalsIgnoreCase(String.valueOf(IStockParam.TOBUY)) ){
								stockDetail.setSellBuy(new Integer(IStockParam.TOBUY));
							}else if (data[6].equalsIgnoreCase(String.valueOf(IStockParam.TOSELL)) ){
								stockDetail.setSellBuy(new Integer(IStockParam.TOSELL));
							}else{
								stockDetail.setSellBuy(new Integer(IStockParam.TOKEEP)); //String.valueOf(IStockParam.TOKEEP)
							}
						}else{
							stockDetail.setSellBuy(new Integer(IStockParam.TOKEEP));
						}
					}
					
					stockList.put(data[0], stockDetail);
				}
			}// end of while, all stock loaded
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected  void write(){
		if (stockList!=null){
			File stockFile = new File(Constant.MGMT_DIR+Constant.SP+Constant.STOCK_LIST);
			if (stockFile.exists()){
				stockFile.delete();
			}
			
			
			try {
				stockFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			BufferedWriter bw =null;
			try {
				bw = new BufferedWriter(new FileWriter(stockFile));
				for ( String stockCode : stockList.keySet() ){
					IStockParam stockDetail = stockList.get(stockCode);
					bw.write(stockCode + ":");
					bw.write(stockDetail.getAddDate() + ":");
					bw.write(stockDetail.getBuyDate() + ":");
					bw.write(stockDetail.getSellDate() + ":");
					bw.write(String.valueOf(stockDetail.getBuyPrice()) + ":");
					bw.write(String.valueOf(stockDetail.getCurrentPrice()) + ":" );
					if (stockDetail.getSellBuy()>0){
						bw.write(String.valueOf(Utils.format3(stockDetail.getSellBuy())) + "" );
					}else if (stockDetail.getSellBuy()<0){
						bw.write(String.valueOf(Utils.format3(stockDetail.getSellBuy())) + "" );
					}else{
						bw.write(String.valueOf(Utils.format3(stockDetail.getSellBuy())) + "" );
					}
					bw.newLine();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}

	public static IStockAccountMgr getInstance(){
		if (instance ==null){
			if (getService()){
				instance = new DefaultStockAccountMgr();
				instance.init();
				releaseService();
			}
		}
		return instance;
	}

	public boolean contains(String stock) {
		if (stockList.containsKey(stock)){
			return true;
		}
		return false;
	}
	
	public synchronized static boolean getService(){
		if ( !inService ){
			inService = true;
			return true;
		}else{
			return false;
		}
		
	}
	public synchronized static void releaseService(){
		inService = false;
	}
	
}
