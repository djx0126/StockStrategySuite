package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class ATR extends AbstractStatisticData {

	public ATR() {
		super(Constant.ATR);
	}

	public ATR(String type) {
		super(type);
	}

	private int ma = 26;

	//MAX(MAX((HIGH-LOW),ABS(REF(CLOSE,1)-HIGH)),ABS(REF(CLOSE,1)-LOW));

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray atr = null;
		try {
			DataArray tr = dataMap.getDataArray(Constant.TR);
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			atr = new DataArray(stockCode, Constant.ATR, dataMap);

			for (int i = 0; i < tr.size() ;i++) {
				double sum = 0.0;
				for (int j = i; j > i-ma && j >=0; j--) {
					sum += tr.getValue(j);
				}

				int count = Math.min(i + 1, ma);

				atr.addRawData(close.getDate(i), sum/count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return atr;
	}

}
