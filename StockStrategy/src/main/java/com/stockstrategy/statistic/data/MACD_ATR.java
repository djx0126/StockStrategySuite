package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class MACD_ATR extends AbstractStatisticData {

	public MACD_ATR() {
		super(Constant.MACD_ATR);
	}

	public MACD_ATR(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray macd = null;
		try {
			DataArray dif = dataMap.getDataArray(Constant.MACDDIF_ATR);
			DataArray dea = dataMap.getDataArray(Constant.MACDDEA_ATR);
			macd = new DataArray(stockCode, Constant.MACD_ATR, dataMap);
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
