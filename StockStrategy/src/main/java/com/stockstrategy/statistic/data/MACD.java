package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class MACD extends AbstractStatisticData {

	public MACD() {
		super(Constant.MACD);
	}
	
	public MACD(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray macd = null;
		try {
			DataArray dif = dataMap.getDataArray(Constant.MACDDIF);
			DataArray dea = dataMap.getDataArray(Constant.MACDDEA);
			macd = new DataArray(stockCode, Constant.MACD, dataMap);
			for (int i = 0; i < dif.size() ;i++)
			{
				double tempMacd = 0f;
				String tempDate = "";
				
				tempMacd=2f*(dif.getValue(i)-dea.getValue(i));
				
				tempDate = dif.getDate(i);
				macd.addRawData(tempDate, tempMacd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return macd;
	}

}
