package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class MACDDIF extends AbstractStatisticData {

	public MACDDIF() {
		super(Constant.MACDDIF);
	}
	
	public MACDDIF(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray dif = null;
		DataArray ema12 = null;
		DataArray ema26 = null;
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			dif = new DataArray(stockCode, Constant.MACDDIF, dataMap);
			ema12 = new DataArray(stockCode, "EMA12", dataMap);
			ema26 = new DataArray(stockCode, "EMA26", dataMap);
			
			/*
			for (int i = 0; i < close.size() ;i++)
			{
				double tempEma12 = 0f;
				double tempEma26 = 0f;
				double tempDif = 0f;
				String tempDate = "";
				
				if (i-1<0){
					tempEma12=0f+close.getValue(i)*2/13 ;
					tempEma26=0f+close.getValue(i)*2/27;
					tempEma12 = 0f;
					tempEma26 = 0f;
					//tempDif = 0f;
				}else{
					tempEma12=  ema12.getValue(i-1)*11/13+close.getValue(i)*2/13   ;
					tempEma26=  ema26.getValue(i-1)*25/27+close.getValue(i)*2/27   ;
					
				}
				tempDif = tempEma12-tempEma26;
				
				tempDate = close.getDate(i);
				ema12.addRawData(tempDate, tempEma12);
				ema26.addRawData(tempDate, tempEma26);
				dif.addRawData(tempDate, tempDif);
			}*/
			double tempEma12 = 0f;
			double tempEma26 = 0f;
			double tempDif = 0f;
			for (int i = 0; i < close.size() ;i++)
			{
				String tempDate = "";
				tempDate = close.getDate(i);
				
				tempEma12=  tempEma12*11/13+close.getValue(i)*2/13   ;
				ema12.addRawData(tempDate, tempEma12);
				
				tempEma26=  tempEma26*25/27+close.getValue(i)*2/27   ;
				ema26.addRawData(tempDate, tempEma26);
				
				tempDif = tempEma12-tempEma26;
				dif.addRawData(tempDate, tempDif);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return dif;
	}

}
