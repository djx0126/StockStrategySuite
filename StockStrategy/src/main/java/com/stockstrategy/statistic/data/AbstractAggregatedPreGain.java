/**
 * 
 */
package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

/**
 * @author Administrator
 *
 *when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 , tigger sell , set value to -1
 *
 *
 *
 */
public abstract class AbstractAggregatedPreGain extends AbstractSPreGain {

	private String[] strategies;

	public AbstractAggregatedPreGain(String name, int pre, int gain, String[] strategies) {
		super(name, pre, gain, null, null, null, null, null, null, null, null, null, null);
		this.strategies = Arrays.copyOf(strategies, strategies.length);
	}
	
	@Override
	protected void setBuy(DataArray statisticArray, DataMap dataMap)  throws Exception{
		DataArray close = dataMap.getDataArray(Constant.CLOSE);
		List<DataArray> dataArrays = new ArrayList<DataArray>();
		for (String strategey: strategies){
			try{
				DataArray dataArray = dataMap.getDataArray(strategey);
				dataArrays.add(dataArray);
			}catch(Exception e){
				// do nothing
			}
		}
		
		for (int i = 0 ; i<close.size();i++)
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
			
		}
	}

	

}
