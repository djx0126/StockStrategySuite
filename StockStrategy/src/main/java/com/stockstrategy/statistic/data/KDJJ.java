package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class KDJJ extends AbstractStatisticData {

	public KDJJ() {
		super(Constant.KDJJ);
	}
	
	public KDJJ(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray jLine = null;
		try {
			DataArray kLine = dataMap.getDataArray(Constant.KDJK);
			DataArray dLine = dataMap.getDataArray(Constant.KDJD);
			jLine = new DataArray(stockCode, Constant.KDJJ, dataMap);
			for(int i=0;i<kLine.size();i++){
				double kCurrent = kLine.getValue(i);
				double dCurrent = dLine.getValue(i);
				jLine.addRawData(kLine.getDate(i), kCurrent*3-dCurrent*2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jLine;
	}

	
	
}
