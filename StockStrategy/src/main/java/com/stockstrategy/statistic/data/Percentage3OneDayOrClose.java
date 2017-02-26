package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class Percentage3OneDayOrClose extends AbstractStatisticData {
	
	public Percentage3OneDayOrClose(String myStatisticType) {
		super(myStatisticType);
	}
	
	public Percentage3OneDayOrClose() {
		super(Constant.Percentage3OneDayOrClose);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray dLine = null;
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray high = dataMap.getDataArray(Constant.HIGH);
			dLine = new DataArray(stockCode, Constant.Percentage3OneDayOrClose, dataMap);
			for(int i=0;i<close.size();i++){
				double value = close.getValue(i);
				if (i > 0){
					double lastClose = close.getValue(i-1);
					double highToday = high.getValue(i);
					double target = lastClose * 1.03d;
					if (highToday >= target){
						value = target;
					}
				}
				dLine.addRawData(close.getDate(i), value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dLine;
	}

}
