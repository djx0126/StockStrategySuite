/**
 * 
 */
package com.stockstrategy.statistic.data.Pre10Gain5;

//import android.graphics.Paint;

//import android.graphics.Paint;

import com.stockstrategy.constant.Constant;
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

	private static final int PREVIOUS = 10;
    private static final int GAIN = 5;
    private static final String myStatisticType = Constant.SPre20Gain5;
    


	private static final String[] strategies = { 
		//Constant.SPre10Gain5_01,
		Constant.SPre10Gain5_02, 
		//Constant.SPre10Gain5_03,
		//Constant.SPre10Gain5_04,
		Constant.SPre10Gain5_05,
		//Constant.SPre10Gain5_06,
//		Constant.SPre10Gain5_07,
		//Constant.SPre10Gain5_08,
//		Constant.SPre10Gain5_09,
		//Constant.SPre10Gain5_10,
		Constant.SPre10Gain5_11,
		Constant.SPre10Gain5_12,
		Constant.SPre10Gain5_13,
		//Constant.SPre10Gain5_14,
//		Constant.SPre10Gain5_15,
//		Constant.SPre10Gain5_16,
//		Constant.SPre10Gain5_17,
//		Constant.SPre10Gain5_18,
//		Constant.SPre10Gain5_19,
//		Constant.SPre10Gain5_20,
		//Constant.SPre10Gain5_21,
//		Constant.SPre10Gain5_22,
		//Constant.SPre10Gain5_23,
		//Constant.SPre10Gain5_24,
		//Constant.SPre10Gain5_25,
		//Constant.SPre10Gain5_26,
//		Constant.SPre10Gain5_27,
//		Constant.SPre10Gain5_28,
//		Constant.SPre10Gain5_29,
		//Constant.SPre10Gain5_30,
//		Constant.SPre10Gain5_31,
//		Constant.SPre10Gain5_32,
//		Constant.SPre10Gain5_33,
//		Constant.SPre10Gain5_34,
		//Constant.SPre10Gain5_35,
//		Constant.SPre10Gain5_36,
		Constant.SPre10Gain5_37
	};

	
    public SAggregated() {
		super(myStatisticType, PREVIOUS, GAIN, strategies);
	}
    

}
