package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class MACDDEA extends AbstractStatisticData {

	public MACDDEA() {
		super(Constant.MACDDEA);
	}
	
	public MACDDEA(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray dea = null;
		try {
			DataArray dif = dataMap.getDataArray(Constant.MACDDIF);
			dea = new DataArray(stockCode, Constant.MACDDEA, dataMap);
			for (int i = 0; i < dif.size() ;i++)
			{
				double tempDea = 0f;
				String tempDate = "";
				
				if (i-1<0){
					tempDea = 0f+dif.getValue(i)*2/10   ;
				}else{
					tempDea = dea.getValue(i-1)*8/10+dif.getValue(i)*2/10   ;
				}
				
				tempDate = dif.getDate(i);
				dea.addRawData(tempDate, tempDea);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return dea;
	}

	
	
}
