package com.stockstrategy.statistic.data.Pre10Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_28 extends AbstractSPreGain { 
	
	private static int PREVIOUS = 10;
	private static int GAIN = 5;
	private static String myStatisticType = Constant.SPre10Gain5_28;
	private static String START_DATE = "20150331"; 
	
	//7433.330(avgGain=7.338%,sum=2597.550,lost=225.898,rate=12.499,count=354,accuracy=88.701) time:33s scale:close7:U2//7460.760(avgGain=7.350%,sum=2594.617,lost=225.898,rate=12.486,count=353,accuracy=88.669) time:34s scale:open8:D2

	//**********  end run with >>>>>>>> 2673.187(avgGain=6.008%,sum=913.158,lost=106.977,rate=9.536,count=152,accuracy=82.895), end at Mon Mar 30 19:46:46 CST 2015 passed 547s
	private static double[] closeOffset={0.005288897462196214, 0.0036996440836204025, 0.009178515112547993, 0.059573424046397705, -0.028565816795417753, 0.01268720645972538, -0.0012855297454514498, -8.549106735128544E-4, 0.006223713230328486, 0.011368512446332869};
	private static double[] openOffset={0.0028747730248128434, 0.005807271577747428, 0.010567297049548667, -0.01140167671427392, 0.005030494258333405, 0.002961590993012045, 0.005984467475249665, -0.02141469902785969, 0.016705693502852715, -0.021189299069938147};
	private static double[] highOffset={0.0609197707374819, -0.0016174859736191123, 0.048338371024753676, 4.153940841827163E-4, -0.002073008622865307, 0.007509601778745537, -0.0012606525017439855, 0.0035854642205463173, 0.009437096006367225, -0.01347707380810737};
	private static double[] lowOffset={0.007452328400386572, 0.0029330177791598287, -0.0033782400139493844, -0.0030144507472725677, -0.028905019159850685, 0.005637451363781084, -0.002610996733593194, -0.00431707811168241, 0.011463483606145767, 0.006866458952917025};
	private static double[] volOffset={0.016202196063000236, -0.04408313711109107, 0.33805798185715424, -0.04507695480271874, -0.13359581995118694, 0.10632144033508281, -0.006045573517422536, -0.1564267289862564, 0.11302699792618259, -0.3146572019037009};
	private static double[] closeScale={0.107282403405845, 0.07663028814703213, 0.09961937459114177, 0.015326057629406427, 0.030652115258812854, 0.07663028814703213, 0.06130423051762571, 0.1149454322205482, 0.12260846103525141, 0.02298908644410964};
	private static double[] openScale={0.15313012852056185, 0.08245468458799483, 0.05889620327713917, 0.070675443932567, 0.16490936917598967, 0.15313012852056185, 0.20024709114227318, 0.12957164720970615, 0.16490936917598967, 0.09423392524342267};
	private static double[] highScale={0.015390728525576002, 0.138516556730184, 0.030781457051152004, 0.261642384934792, 0.15390728525576003, 0.21547019935806402, 0.23086092788364004, 0.10773509967903201, 0.16929801378133602, 0.15390728525576003};
	private static double[] lowScale={0.09129852322806196, 0.20868233880699877, 0.013042646175437423, 0.19563969263156133, 0.06521323087718711, 0.07825587705262455, 0.19563969263156133, 0.1173838155789368, 0.10434116940349938, 0.10434116940349938};
	private static double[] volScale={5.3960959940407305, 2.075421536169512, 4.150843072339024, 0.8301686144678047, 6.226264608508535, 5.811180301274632, 5.3960959940407305, 4.981011686806828, 6.641348915742437, 2.075421536169512};
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	
//	@Override
//	public String getStartDate(){
//		return START_DATE;
//	}
	
	public S_28() {
		this(myStatisticType);
	}
	
	public S_28(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}



}
