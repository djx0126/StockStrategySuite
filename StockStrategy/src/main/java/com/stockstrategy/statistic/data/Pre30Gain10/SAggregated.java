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
import com.stockstrategy.data.SharedStockDataHolder;
import com.stockstrategy.statistic.data.AbstractAggregatedPreGain;

/**
 * @author Administrator
 *
 *when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 , tigger sell , set value to -1
 *
 *
 *
 */
public class SAggregated extends AbstractAggregatedPreGain {

	private static final int PREVIOUS = 30;
    private static final int GAIN = 10;
    private static final String myStatisticType = Constant.SPre30Gain10;

	private static final String[] strategies = { 
//		Constant.SPre30Gain10_01, 
		Constant.SPre30Gain10_02, 
		Constant.SPre30Gain10_03,
		Constant.SPre30Gain10_04,
		Constant.SPre30Gain10_05,
		Constant.SPre30Gain10_06,
		Constant.SPre30Gain10_07,
		Constant.SPre30Gain10_08,
		Constant.SPre30Gain10_09,
		Constant.SPre30Gain10_10,
		Constant.SPre30Gain10_11,
		Constant.SPre30Gain10_12,
		Constant.SPre30Gain10_13,
		Constant.SPre30Gain10_14,
		Constant.SPre30Gain10_15,
		Constant.SPre30Gain10_16,
		Constant.SPre30Gain10_17,
		Constant.SPre30Gain10_18//,
		//Constant.SPre30Gain10_19
	};

	
    public SAggregated() {
		super(myStatisticType, PREVIOUS, GAIN, strategies);
	}

}
