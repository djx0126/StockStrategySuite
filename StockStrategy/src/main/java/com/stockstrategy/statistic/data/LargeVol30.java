/**
 * 
 */
package com.stockstrategy.statistic.data;


import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

/**
 * @author Administrator
 * a statistic data describe the large/largest Vol of a stock 
 */
public class LargeVol30 extends AbstractStatisticData {
	private final int DIV = 3; //divide the actual value by DIV, the default is one
	
	public LargeVol30() {
		super(Constant.LargeVol30);// the type name of a statistic
	}

	/* (non-Javadoc)
	 * @see com.stock.statistic.data.AbstractStatisticData#generate(java.lang.String, java.lang.String, com.stock.data.DataMap)
	 */
	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray largeVol = null;
		try {
			DataArray mavol30 = dataMap.getDataArray(Constant.MAVOL30);
			if (mavol30 !=null){
				double currentMaxVol = 0f;
				double targetMaxVol = 0f;
				largeVol = new DataArray(stockCode, statisticType, dataMap);
				for (int i=0; i< mavol30.size();i++){
					if (mavol30.getValue(i)>targetMaxVol){
						targetMaxVol = mavol30.getValue(i);
					}
					if (targetMaxVol-currentMaxVol>10){
						currentMaxVol+=(targetMaxVol-currentMaxVol)/10;
					}
					largeVol.addRawData(mavol30.getDate(i), currentMaxVol/DIV);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return largeVol;
	}

}
