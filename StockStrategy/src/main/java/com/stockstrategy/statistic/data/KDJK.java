package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class KDJK extends AbstractStatisticData {

	public KDJK() {
		super(Constant.KDJK);
	}
	
	public KDJK(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray kLine = null;
		try {
			DataArray rsv = dataMap.getDataArray(Constant.KDJRSV);
			kLine = new DataArray(stockCode, Constant.KDJK, dataMap);
			for(int i=0;i<rsv.size();i++){
				double rsvCurrent = rsv.getValue(i);
				double lastK =50;
				if(i>1 ){
					lastK = kLine.getValue(i-1);
				}
				kLine.addRawData(rsv.getDate(i), lastK*2/3+rsvCurrent/3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kLine;
	}

	
	
}
