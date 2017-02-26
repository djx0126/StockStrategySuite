package com.stockstrategy.statistic.data.Pre10Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_33 extends AbstractSPreGain { 
	
	private static int PREVIOUS = 10;
	private static int GAIN = 5;
	private static String myStatisticType = Constant.SPre10Gain5_33;
	private static String START_DATE = "20150409";  
	
	//14082.619(avgGain=9.050%,sum=3267.010,lost=250.269,rate=14.054,count=361,accuracy=88.089) time:31s scale:open9:U2
	//**********  end run with >>>>>>>> 6704.236(avgGain=8.127%,sum=1267.808,lost=110.394,rate=12.484,count=156,accuracy=82.692), end at Wed Apr 08 18:55:00 CST 2015 passed 940s
	private static double[] closeOffset={-0.0020741086581537037, 0.0339738513398731, -0.021864328860078438, 0.010794585431320636, 0.0, 8.931509202491328E-4, 0.021301072196768692, 0.021165824326984647, -0.009989822646372841, -0.02230048136887188};
	private static double[] openOffset={0.006986120860666078, 0.026377704247143377, 0.01066897825338686, 0.002708706173128963, -0.017746881055318704, -0.014250268381792464, 0.03915635435730419, 0.011184166952513638, -0.029207271085786208, -0.03242000956621533};
	private static double[] highOffset={-0.0018011201840335122, -0.02339831339734576, 0.0, 0.00860017176191757, -0.008600171761917531, 0.02115736682611621, 0.021301072196768692, 0.010694522161337067, -0.028723414274156436, 0.02198678515242527};
	private static double[] lowOffset={-0.019448997873512668, -0.02184260627964546, -0.005462895701502138, -0.014495499253588673, -0.01816049924087068, 0.006578626691055559, 0.04070749782403355, 0.03491373971740943, -0.0222383637568915, -0.04631203123460782};
	private static double[] volOffset={-0.2837535593517007, 0.21009388775806168, -0.17085693123367543, -0.16495115076210132, 0.3167338243090393, 0.030851846910513764, 0.1330134599210429, -0.26127396600524444, 0.2511807620309313, 0.07883219466027228};
	private static double[] closeScale={0.06130423051762571, 0.007663028814703213, 0.04597817288821928, 0.08429331696173535, 0.07663028814703213, 0.038315144073516064, 0.13027148984995462, 0.038315144073516064, 0.09195634577643856, 0.07663028814703213};
	private static double[] openScale={0.08245468458799483, 0.011779240655427833, 0.09423392524342267, 0.12957164720970615, 0.1766886098314175, 0.141350887865134, 0.070675443932567, 0.141350887865134, 0.18846785048684533, 0.141350887865134};
	private static double[] highScale={0.16929801378133602, 0.07695364262788001, 0.138516556730184, 0.046172185576728, 0.12312582820460802, 0.261642384934792, 0.24625165640921604, 0.21547019935806402, 0.030781457051152004, 0.030781457051152};
	private static double[] lowScale={0.02608529235087485, 0.1565117541052491, 0.03912793852631227, 0.013042646175437423, 0.20868233880699877, 0.05217058470174969, 0.07825587705262455, 0.07825587705262455, 0.14346910792981166, 0.013042646175437423};
	private static double[] volScale={1.6603372289356093, 6.226264608508535, 3.735758765105121, 0.8301686144678047, 1.6603372289356093, 4.981011686806828, 3.3206744578712186, 4.981011686806828, 2.490505843403414, 4.565927379572925};
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	
//	@Override
//	public String getStartDate(){
//		return START_DATE;
//	}
	
	public S_33() {
		this(myStatisticType);
	}
	
	public S_33(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}



}
