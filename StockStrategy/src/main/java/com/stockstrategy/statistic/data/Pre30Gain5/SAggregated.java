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
    private static final int GAIN = 5;
    private static final String myStatisticType = Constant.SPre30Gain5;


	private static final String[] strategies = { 
		//Constant.SPre30Gain5I, 
		//Constant.SPre30Gain5II,  // removed 2014.11.10
		//Constant.SPre30Gain5III,  // removed 2014.12.08 
		//Constant.SPre30Gain5IV,// removed 2014.12.08 
		//Constant.SPre30Gain5V, // removed 2014.12.08 
		//Constant.SPre30Gain5VI, // removed 2014.12.08 
		//Constant.SPre30Gain5VII, // removed 2014.12.08 
		//Constant.SPre30Gain5VIII,// removed 2014.12.08 
		Constant.SPre30Gain5IX, // removed 2014.12.08 , add 2015.01.01
		//Constant.SPre30Gain5X, // removed 2014.12.08 
		//Constant.SPre30Gain5_11, // removed 2014.12.08 
		//Constant.SPre30Gain5_12, // removed 2014.12.08 
		//Constant.SPre30Gain5_13,// removed 2014.12.08 
		//Constant.SPre30Gain5_14,// removed 2014.12.08 
		//Constant.SPre30Gain5_15,// removed 2014.12.08 
//		Constant.SPre30Gain5_16,//////  removed 2014.11
		//Constant.SPre30Gain5_17,
		//Constant.SPre30Gain5_18,// removed 2014.12.08 
		//Constant.SPre30Gain5_19,
		//Constant.SPre30Gain5_20,
		//Constant.SPre30Gain5_21,// removed 2014.12.08 
		//Constant.SPre30Gain5_22,// removed 2014.12.08 
		//Constant.SPre30Gain5_23,// removed 2014.12.08 
		Constant.SPre30Gain5_24,// removed 2014.12.08 , move back 2014.12.30
		//Constant.SPre30Gain5_25,
		//Constant.SPre30Gain5_26,// removed 2014.12.08 
		//Constant.SPre30Gain5_27,// removed 2014.12.08 
		//Constant.SPre30Gain5_28,// removed 2014.12.08 
		//Constant.SPre30Gain5_29// removed 2014.12.08 
		Constant.SPre30Gain5_30,
//		Constant.SPre30Gain5_31,
		Constant.SPre30Gain5_32,
		Constant.SPre30Gain5_33
		//Constant.SPre30Gain5_34
		};

	
    public SAggregated() {
		super(myStatisticType, PREVIOUS, GAIN, strategies);
	}

}
