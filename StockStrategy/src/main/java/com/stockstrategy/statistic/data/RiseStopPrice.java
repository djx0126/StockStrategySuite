package com.stockstrategy.statistic.data;


import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class RiseStopPrice extends AbstractStatisticData {
	
	// if high > possibleRiseLimit, set to possibleRiseLimit
	// otherwise, set to close
	
	public RiseStopPrice(String myStatisticType) {
		super(myStatisticType);
	}
	
	public RiseStopPrice() {
		super(Constant.RiseStopPrice);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray dLine = null;
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray high = dataMap.getDataArray(Constant.HIGH);
			dLine = new DataArray(stockCode, Constant.RiseStopPrice, dataMap);
			
			for(int i=0;i<close.size();i++){
				dLine.addRawData(close.getDate(i), close.getValue(i));
			}
			
			
			/// to find out if its limit by 5%
			boolean isST = false;
			double maxRise = 0;
			for(int i=1;i<close.size();i++){
				double rise = close.getValue(i)/close.getValue(i-1);
				if (rise > maxRise){
					maxRise = rise;
				}
			}
			if (maxRise <1.055){
				isST = true;
			}
			
			double riseLimit = isST? 1.045:1.095;
			
			
			for(int i=1;i<close.size();i++){
				double lastCloseValue = close.getValue(i-1);
				double possibleRiseLimit = riseLimit*lastCloseValue;
				double highValue = high.getValue(i);
				if (highValue>possibleRiseLimit){
					dLine.setValue(i, possibleRiseLimit);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dLine;
	}

}
