/**
 * 
 */
package com.stockstrategy.statistic.data.Pre20Gain5;

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

	private static final int PREVIOUS = 20;
    private static final int GAIN = 5;
    private static final String myStatisticType = Constant.SPre20Gain5;
    
    private static String START_DATE = "20141224";

	private static final String[] strategies = { 
		Constant.SPre20Gain5_01, 
		Constant.SPre20Gain5_02, 
		Constant.SPre20Gain5_03,
		Constant.SPre20Gain5_04,
		Constant.SPre20Gain5_05,
		Constant.SPre20Gain5_06,
		Constant.SPre20Gain5_07,
		Constant.SPre20Gain5_08,
		Constant.SPre20Gain5_09,
		Constant.SPre20Gain5_10
	};

	
    public SAggregated() {
		super(myStatisticType, PREVIOUS, GAIN, strategies);
	}
    
    @Override
	public String getStartDate(){
		return START_DATE;
	}

}
