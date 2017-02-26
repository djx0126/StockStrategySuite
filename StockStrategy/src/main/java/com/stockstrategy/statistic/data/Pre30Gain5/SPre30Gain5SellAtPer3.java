/**
 * 
 */
package com.stockstrategy.statistic.data.Pre30Gain5;

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
public class SPre30Gain5SellAtPer3 extends AbstractStrategyStatisticData {

	/* (non-Javadoc)
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String, java.lang.String, com.stock.data.DataMap)
	 * 
	 */
	private static final int PREVIOUS = 30;
    private static final int GAIN = 5;
    private static final double LIMIT = PREVIOUS;
	private static final String myStatisticType = Constant.SPre30Gain5SellAtPer3;




	
    public static int keyDateCount = 0;
    
    public SPre30Gain5SellAtPer3() {
		super(myStatisticType);
	}

	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray statisticArray = null;
		DataArray ma5 = null;
		
		List<DataArray> dataArrays = new ArrayList<DataArray>();
		String[] strategies = { 
				Constant.SPre30Gain5
				};
		
		if (!dataMap.containArray(Constant.MA5) || !dataMap.containArray(Constant.MA10))
		{
			return null;
		}
		try {
			ma5 = dataMap.getDataArray(Constant.MA5);
			
			for (String strategey: strategies){
				dataArrays.add(dataMap.getDataArray(strategey));
			}
			
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap, Constant.Percentage3OneDayOrClose);
			
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
				
//				if (P30G5I.getValue(i)>0 || P30G5II.getValue(i)>0 || P30G5III.getValue(i)>0){
//					tobuy = true;
//            	}
                
				if (tobuy){
					statisticArray.setValue(i, 1);
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
			
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray high = dataMap.getDataArray(Constant.HIGH);
			int inHand = 0;
			for(int i=0;i<close.size();i++){
				if (inHand == 0){
					if (statisticArray.getValue(i)>0){
						inHand = 1;
					}
				}else if (inHand == 1){
					if (i > 0){
						double lastClose = close.getValue(i-1);
						double highToday = high.getValue(i);
						double target = lastClose * 1.03d;
						if (highToday >= target){
							statisticArray.setValue(i, -1);
						}
					}
					if (statisticArray.getValue(i)<0){
						inHand = 0;
					}
					
				}
				
				
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return statisticArray;
	}

}
