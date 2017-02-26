package com.stockstrategy.statistic.data.Pre10Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_05 extends AbstractSPreGain {
	
	private static int PREVIOUS = 10;
	private static int GAIN = 5;
	private static String myStatisticType = Constant.SPre10Gain5_05;
	private static String START_DATE = "20150106";
	
	//1575.901(avgGain=5.071%,sum=740.407,lost=166.212,rate=5.455,count=146,accuracy=78.082)
	//1250.575(avgGain=6.486%,sum=136.213,lost=5.451,rate=25.986,count=21,accuracy=85.714)

	private static double[] closeOffset={-0.003817998057018339, -0.009400680496261913, -0.012828195807719553, 0.0018095629592685314, -0.017765032208826713, 0.004373596699742958, -0.0017547275665504847, 0.015146163075634215, 0.004557171740478152, -0.016186761093275225};
	private static double[] openOffset={-0.006489045744133788, -0.017021846870131044, 0.002717179761712857, -0.010768183416394813, -0.0035308688951332496, -0.0035023938360706055, 0.014182304520868001, 0.0036191416267306606, -0.012537206155241436, -0.014752679569398441};
	private static double[] highOffset={-0.008290072154380957, -0.008134785881770478, -0.0035671205213566003, -0.003538060153609795, -0.014723256820706347, 0.00860017176191757, -0.0025979807199086255, 0.013148163053216803, -0.006185376930858141, -0.015512166178247553};
	private static double[] lowOffset={-0.0038433390817602174, 0.031391149460773246, -0.010219165181686082, 0.0362314601771133, -0.014358150046867203, -0.017719613341050825, 0.007149041789664057, 0.007268696564764526, 0.0036804837383676015, -0.017211001187902714};
	private static double[] volOffset={-0.0245533788014619, -0.18023611282927024, 0.033531098583099446, -0.02077956928048664, -0.05934135216721372, -0.006311400964410174, -0.21342295870742492, 0.05323238294928789, 0.040402498420353296, -0.13049400386613116};
	private static double[] closeScale={0.044584894921909604, 0.044584894921909604, 0.09474290170905791, 0.044584894921909604, 0.0501580067871483, 0.1003160135742966, 0.055731118652387, 0.111462237304774, 0.09474290170905791, 0.12260846103525141};
	private static double[] openScale={0.18846785048684533, 0.008566720476674786, 0.1456342481034714, 0.008566720476674788, 0.11136736619677225, 0.19703457096352012, 0.059967043336723515, 0.19703457096352012, 0.16276768905682099, 0.07710048429007309};
	private static double[] highScale={0.24625165640921604, 0.01119325710950982, 0.06715954265705892, 0.20147862797117674, 0.07835279976656874, 0.22386514219019638, 0.07835279976656874, 0.1790921137521571, 0.08954605687607856, 0.13431908531411785};
	private static double[] lowScale={0.15176897367781728, 0.00948556085486358, 0.02845668256459074, 0.00948556085486358, 0.17074009538754442, 0.00948556085486358, 0.17074009538754442, 0.21816789966186234, 0.0948556085486358, 0.03794224341945432};
	private static double[] volScale={3.3206744578712186, 1.2075179846804431, 0.6037589923402216, 2.4150359693608863, 6.641348915742437, 1.2075179846804431, 5.735710427232105, 4.528192442551662, 0.9056384885103324, 2.4150359693608863};

	
//	@Override
//	public String getStartDate(){
//		return START_DATE;
//	}
	
	public S_05() {
		this(myStatisticType);
	}
	
	public S_05(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}



}
