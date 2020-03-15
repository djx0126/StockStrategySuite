package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class MACDDIF_ATR extends AbstractStatisticData {

	public MACDDIF_ATR() {
		super(Constant.MACDDIF_ATR);
	}

	public MACDDIF_ATR(String type) {
		super(type);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray macddif_atr = null;
		try {
			DataArray dif = dataMap.getDataArray(Constant.MACDDIF);
			DataArray atr = dataMap.getDataArray(Constant.ATR);
			macddif_atr = new DataArray(stockCode, Constant.MACDDIF_ATR, dataMap);

			for (int i = 0; i < dif.size() ;i++)
			{
				double atr_abs = Math.abs(atr.getValue(i));
				macddif_atr.addRawData(dif.getDate(i), atr_abs > 0 ? dif.getValue(i) / atr_abs : 0.0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return macddif_atr;
	}

}
