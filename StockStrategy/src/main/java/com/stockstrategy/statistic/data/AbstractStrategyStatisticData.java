package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.SharedStockDataHolder;
import com.stockstrategy.data.StockDataHolder;
import com.stockstrategy.file.StockLister;

public  abstract class AbstractStrategyStatisticData extends AbstractStatisticData {
	private DataArray dataArray = null;

	private String description = null;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AbstractStrategyStatisticData(String type) {
		super(type);
	}

	public DataArray getDataArray()
	{
		return dataArray;
	}
	
	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		dataArray = actualGenerate(stockCode, statisticType,dataMap);
		
		patchAllotment( stockCode, dataArray );
		
		breakPatch( stockCode, dataArray );
		
		return dataArray;
	}
	
	protected abstract DataArray actualGenerate(String stockCode, String statisticType,
			DataMap dataMap);
	
	
	
	/**
	 *  停牌补丁
	 *  
	 *  如果最后一日与sh000001不符，认为是停牌，取消买入
	 */
	protected void breakPatch(String stockCode, DataArray buysellArray){ // 停牌补丁
		if (buysellArray == null || buysellArray.size()==0){
			return;
		}
		
		try {
			String lastDate = buysellArray.getDate(buysellArray.size()-1);

			String code001 = this.getStockCode001(stockCode);
			if (code001 != null) {
				DataMap sh000001 = SharedStockDataHolder.getInstance().get(code001);
				DataArray shCloseArray = sh000001.getDataArray(Constant.CLOSE);
				String shLastDate = shCloseArray.getDate(shCloseArray.size()-1);

				if (lastDate.compareTo(shLastDate)<0){
					buysellArray.setValue(buysellArray.size()-1, 0);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String getStockCode001(String stockCode) {
		return StockLister.getCompositeIndexCode(stockCode);
	}
	

	/*
	 * 当前一日收盘价大幅高于今日开盘价，视为送配股
	 * 操作方法是: 1.清除6日内买入点
	 * 			  2.6日前设定卖出点
	 * 
	 */
	protected void patchAllotment(String stockCode, DataArray buysellArray){//送配股补丁
		patchAllotment(stockCode, buysellArray, 6);
	}

	protected void patchAllotment(String stockCode, DataArray buysellArray, int daysToClearBayPoint){//送配股补丁
		try {
			StockDataHolder dataHolder = StockDataHolder.getInstance();
			DataMap stockData = dataHolder.get(stockCode);
			//DataArray close = StockDataHolder.getInstance().get(stockCode).getDataArray(Constant.CLOSE);
			DataArray close = stockData.getDataArray(Constant.CLOSE);
			DataArray open = StockDataHolder.getInstance().get(stockCode).getDataArray(Constant.OPEN);
			for (int i=0;i<buysellArray.size();i++){
				if (close !=null && i>1 ){
					double lastDayPrice = close.getValue(i-1);
					double todayPrice = open.getValue(i);
					if (lastDayPrice*0.82f > todayPrice || todayPrice > lastDayPrice * 1.15f){ //送配股, 清除前一个买入点
						//buysellArray.setValue(i-1, -1);
						int k = i;
						for (int j = 1;j<daysToClearBayPoint;j++){
							k = i-j;
							if (k >=0 && k < buysellArray.size()){
								buysellArray.setValue(k, 0);
							}
						}
						if (k>=0 ){
							buysellArray.setValue(k, -1);
						} 
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean hasAllotment(int i, DataArray close, DataArray open) throws Exception {
		return hasAllotment(i, close, open, 6);
	}

	protected boolean hasAllotment(int i, DataArray close, DataArray open, int daysForAllotment) throws Exception {
		boolean hasAllotment = false;

		if (i >= close.size() || i - daysForAllotment <0) {
			return false;
		};

		for (int k = i; k > i - daysForAllotment;k--) {
			double lastDayPrice = close.getValue(k-1);
			double todayPrice = open.getValue(k);
			if (lastDayPrice*0.82f > todayPrice || todayPrice > lastDayPrice * 1.15f) {
				hasAllotment = true;
				break;
			}
		}
		return hasAllotment;
	}

}
