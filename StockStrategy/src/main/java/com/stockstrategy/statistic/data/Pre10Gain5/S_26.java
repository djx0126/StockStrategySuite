package com.stockstrategy.statistic.data.Pre10Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_26 extends AbstractSPreGain { 
	
	private static int PREVIOUS = 10;
	private static int GAIN = 5;
	private static String myStatisticType = Constant.SPre10Gain5_26;
	private static String START_DATE = "20150310";  //  using only for count > 1 !!!
	
	//8222.875(avgGain=6.705%,sum=4988.677,lost=450.978,rate=12.062,count=744,accuracy=85.215) time:30s scale:low1:D
	//**********  end run with >>>>>>>> 4841.872(avgGain=6.524%,sum=1983.349,lost=185.014,rate=11.720,count=304,accuracy=84.868), end at Tue Mar 10 03:15:21 CST 2015 passed 529s
	private static double[] closeOffset={0.009150514871556692, 0.045735669892792125, -0.0020963571609157734, 0.036735763477664315, -0.013744643378034575, 0.011655010724778009, 0.011125342806756242, 0.012098159760203571, 0.007994441888982384, -0.004883684190311645};
	private static double[] openOffset={0.012820153716710588, -0.01770915050378887, 0.003851084727466302, -0.016898066137149923, 0.011655010724778009, -0.008259414991275218, 0.0037660774440008296, 0.01114529762401441, -0.008385672885740618, 0.00627402618793637};
	private static double[] highOffset={0.018710963719603613, 0.00377202473931353, 0.0038050735650251716, -0.01233095558014681, -0.01904084087801915, 0.005083489354071408, -0.007435573535242938, 0.009828386651133225, 0.0041328890663844126, -0.004132889066384359};
	private static double[] lowOffset={0.024964304506383407, -0.009309224134771405, 0.006423535897283219, -0.012753446069686843, 0.0013986951925648054, 0.0035165737228371094, -0.009084527961779365, 0.012629808726429126, -0.003898222256568133, 0.0017675827489201175};
	private static double[] volOffset={0.14636007694264544, 0.13372134406308633, -0.06146815184788538, -0.06509088512355923, -0.035439649770060914, 0.17558242863880127, -0.2810681829394936, 0.14627712831966233, 0.1959402150818292, -0.08319702823249545};
	private static double[] closeScale={0.1149454322205482, 0.007663028814703213, 0.107282403405845, 0.007663028814703213, 0.09195634577643856, 0.1149454322205482, 0.09961937459114177, 0.02298908644410964, 0.015326057629406427, 0.09195634577643856};
	private static double[] openScale={0.1060131658988505, 0.011779240655427833, 0.047116962621711334, 0.1766886098314175, 0.16490936917598967, 0.070675443932567, 0.08245468458799483, 0.12957164720970615, 0.0353377219662835, 0.05889620327713917};
	private static double[] highScale={0.138516556730184, 0.24625165640921604, 0.184688742306912, 0.184688742306912, 0.015390728525576002, 0.138516556730184, 0.138516556730184, 0.261642384934792, 0.21547019935806402, 0.16929801378133602};
	private static double[] lowScale={0.026085292350874846, 0.1695544002806865, 0.19563969263156133, 0.1565117541052491, 0.1565117541052491, 0.05217058470174969, 0.10434116940349938, 0.1173838155789368, 0.1565117541052491, 0.09129852322806196};
	private static double[] volScale={6.641348915742437, 2.075421536169512, 1.6603372289356093, 3.3206744578712186, 0.4150843072339023, 6.641348915742437, 2.490505843403414, 5.3960959940407305, 4.565927379572925, 2.075421536169512};
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	
//	@Override
//	public String getStartDate(){
//		return START_DATE;
//	}
	
	public S_26() {
		this(myStatisticType);
	}
	
	public S_26(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}



}
