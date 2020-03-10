package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class TR extends AbstractStatisticData {

	public TR() {
		super(Constant.TR);
	}

	public TR(String type) {
		super(type);
	}

	//MAX(MAX((HIGH-LOW),ABS(REF(CLOSE,1)-HIGH)),ABS(REF(CLOSE,1)-LOW));

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray tr = null;
		try {
			DataArray high = dataMap.getDataArray(Constant.HIGH);
			DataArray low = dataMap.getDataArray(Constant.LOW);
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			tr = new DataArray(stockCode, Constant.TR, dataMap);

			for (int i = 0; i < close.size() ;i++) {
				double highValue = high.getValue(i);
				double lowValue = low.getValue(i);
				double value = highValue - low.getValue(i);
				if ( i > 0) {
					double max1 = Math.max(highValue - lowValue, Math.abs(close.getValue(i - 1) - highValue));
					value = Math.max(max1, Math.abs(close.getValue(i - 1) - lowValue));
				}

				tr.addRawData(close.getDate(i), value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return tr;
	}

	
	
}
