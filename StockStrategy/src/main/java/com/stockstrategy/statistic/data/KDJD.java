package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class KDJD extends AbstractStatisticData {

	public KDJD() {
		super(Constant.KDJD);
	}
	
	public KDJD(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray dLine = null;
		try {
			DataArray kLine = dataMap.getDataArray(Constant.KDJK);
			dLine = new DataArray(stockCode, Constant.KDJD, dataMap);
			for(int i=0;i<kLine.size();i++){
				double kCurrent = kLine.getValue(i);
				double lastD =50;
				if(i>1 ){
					lastD = dLine.getValue(i-1);
				}
				dLine.addRawData(kLine.getDate(i), lastD*2/3+kCurrent/3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dLine;
	}

	
	
}
