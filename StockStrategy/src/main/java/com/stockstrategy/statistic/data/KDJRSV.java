package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class KDJRSV extends AbstractStatisticData {

	public KDJRSV() {
		super(Constant.KDJRSV);
	}
	
	public KDJRSV(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray rsv = null;
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray high  = dataMap.getDataArray(Constant.HIGH);
			DataArray low   = dataMap.getDataArray(Constant.LOW);
			rsv = new DataArray(stockCode, Constant.KDJRSV, dataMap);
			for(int i=0;i<close.size();i++){
				double close9 = close.getValue(i);
				double high9=0.0f;
				double low9=1000000.0f;
				for (int j=i;j>i-9;j--){
					if(j>=0 && j<close.size()){
						if (high.getValue(j)>high9){
							high9=high.getValue(j);
						}
						if(low.getValue(j)<low9){
							low9=low.getValue(j);
						}
					}
				}
				if (high9>low9){
					double rsv9 = 100*(close9-low9)/(high9-low9);
					rsv.addRawData(close.getDate(i), rsv9);
				}else {
					rsv.addRawData(close.getDate(i), 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsv;
	}

	
	
}
