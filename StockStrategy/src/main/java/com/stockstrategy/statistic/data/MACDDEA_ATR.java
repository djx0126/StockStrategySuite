package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class MACDDEA_ATR extends AbstractStatisticData {

	public MACDDEA_ATR() {
		super(Constant.MACDDEA_ATR);
	}

	public MACDDEA_ATR(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray dea_atr = null;
		try {
			DataArray dif_atr = dataMap.getDataArray(Constant.MACDDIF_ATR);
			dea_atr = new DataArray(stockCode, Constant.MACDDEA_ATR, dataMap);
			for (int i = 0; i < dif_atr.size() ;i++)
			{
				double tempDea = 0f;
				String tempDate = "";
				
				if (i-1<0){
					tempDea = 0f+dif_atr.getValue(i)*2/10   ;
				}else{
					tempDea = dea_atr.getValue(i-1)*8/10+dif_atr.getValue(i)*2/10   ;
				}
				
				tempDate = dif_atr.getDate(i);
				dea_atr.addRawData(tempDate, tempDea);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return dea_atr;
	}

	
	
}
