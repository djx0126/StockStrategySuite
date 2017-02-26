/**
 * 
 */
package com.stockstrategy.statistic.data.Pre30Gain10;

//import android.graphics.Paint;

//import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;
import com.stockstrategy.statistic.data.AbstractStrategyStatisticData;

/**
 * @author Administrator
 *
 *when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 , tigger sell , set value to -1
 *
 *
 *
 */
public class SPre30Gain10RealBuy extends AbstractStrategyStatisticData {

	/* (non-Javadoc)
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String, java.lang.String, com.stock.data.DataMap)
	 * 
	 */
	private static final int PREVIOUS = 30;
    private static final int GAIN = 10;
    private static final double LIMIT = PREVIOUS;
	private static final String myStatisticType = Constant.SPre30Gain10RealBuy;




	
    public static int keyDateCount = 0;
    
    public SPre30Gain10RealBuy() {
		super(myStatisticType);
	}

	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray statisticArray = null;
		DataArray ma5 = null;
		DataArray close = null;
		DataArray low = null;
		
		
		List<DataArray> dataArrays = new ArrayList<DataArray>();
		String[] strategies = { 
//				Constant.SPre30Gain10_01, 
				Constant.SPre30Gain10_02, Constant.SPre30Gain10_03,Constant.SPre30Gain10_04,Constant.SPre30Gain10_05
		};
		
		if (!dataMap.containArray(Constant.MA5) || !dataMap.containArray(Constant.MA10))
		{
			return null;
		}
		try {
			ma5 = dataMap.getDataArray(Constant.MA5);
			close = dataMap.getDataArray(Constant.CLOSE);
			low = dataMap.getDataArray(Constant.LOW);
			
			for (String strategey: strategies){
				dataArrays.add(dataMap.getDataArray(strategey));
			}
			

			
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			
			for (int i = 0; i<ma5.size();i++)
			{
				RawData data = new RawData(ma5.getDate(i),0);
				statisticArray.addData(data);
			}
			
			//buy: 1.cross ma5 ma10
			// * 		2.ma10 < ma30
			// * 		3.VolMa20 < VolMa60
			for (int i = 0 ; i<ma5.size();i++)
			{
				statisticArray.setValue(i, 0);
				boolean tobuy = false;
				
				for (DataArray dataArray:dataArrays){
					if (dataArray.getValue(i)>0){
						tobuy = true;
						break;
					}
				}

                
				if (tobuy){
					statisticArray.setValue(i, 1);
				}
				
				// if tomorrow's low is higher than today's close, no way to buy it
				if (i<ma5.size()-1 && tobuy){
					double todayClose = close.getValue(i);
					double tomorrowLow = low.getValue(i+1);
					if (tomorrowLow > todayClose){
						statisticArray.setValue(i, 0);
					}
				}
				
			}
			//sell: cross ma5 ma10
			int gainLeft = -1;
			for (int i = 0 ; i<ma5.size();i++)
			{
				if (gainLeft>0){
					gainLeft--;
				}
				if (statisticArray.getValue(i)>0){
					gainLeft = GAIN;
				}
				if(gainLeft==0){
					gainLeft=-1;
					statisticArray.setValue(i, -1);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return statisticArray;
	}

}
