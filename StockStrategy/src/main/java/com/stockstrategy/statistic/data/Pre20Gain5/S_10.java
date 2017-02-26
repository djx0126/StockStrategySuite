package com.stockstrategy.statistic.data.Pre20Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_10 extends AbstractSPreGain {
	
	private static int PREVIOUS = 20;
	private static int GAIN = 5;
	private static String myStatisticType = Constant.SPre20Gain5_10;
	private static String START_DATE = "20141224";
	
	//8320.573(avgGain=8.068%,sum=2025.097,lost=11.812,rate=172.440,count=251,accuracy=98.008) time:2s offset:close16:U2
		//4231.077(avgGain=7.533%,sum=738.197,lost=6.051,rate=122.997,count=98,accuracy=98.980),

		private static double[] closeOffset={0.025858937534179414, -0.010460289197542449, -0.002523752275421249, 0.011258936992560902, -0.006832699154862588, 0.010289787912454496, -0.025894861243030518, 0.002253951414922912, -0.025724290611560258, -0.004430631944410722, 0.00327045009174625, 0.007793739564197184, 0.01166705719727189, -0.015425163727532777, 0.0015981415712266725, -0.003190423765154458, -0.0030880563626072277, 0.016242964900490715, 0.017746225684154238, -2.1818361770291353E-4};
		private static double[] openOffset={-0.00707230921195868, 0.03382460168773875, 0.010419314726396539, -0.00214678876040666, -0.011038106499218912, -0.022352124564827786, 0.0, -0.027283021059049114, -0.00326900201515492, 0.0040417682010784085, 0.009975227347298823, 0.020665296239453658, -0.017488001907569134, 9.97690495979886E-4, -0.012338144342892534, 0.003218500932355449, 0.01708987596555768, 0.021084308260312, -0.004605033865816647, 0.006815188670933461};
		private static double[] highOffset={-0.006355642431180903, -0.011210900718374791, 0.0010259737393870849, 0.013987140308901858, -0.004432691759399271, -0.011603977987526534, -0.009305221439098094, -0.018608472500321164, -0.0026761188315249014, -1.9052182007824194E-4, -0.005489085916722526, 0.018050202358088047, 0.005723572945404391, -0.005919598442708188, 0.00196424795222041, 0.002368884865479429, -0.01478923395443033, 0.033806562459203424, 0.011525982390558688, 4.249457109130491E-4};
		private static double[] lowOffset={-0.0019597177084102954, -0.004753519397187101, 0.004319224879092541, 0.0041434384869549, 0.005290962682137576, -0.006822095657316625, -0.023377655457964806, -0.0028870584699129593, -0.014949285985323746, -0.00552494752145124, 0.004929615718970015, 0.020090082022446484, 0.007119741122947827, -0.020011407267238747, 8.072390463574275E-4, 0.010219165181686028, 0.006668760312014425, 0.021084308260312, 0.003986412830919916, 0.0024542779423101842};
		private static double[] volOffset={0.12391834775271943, 0.02076362284455005, -0.21240086975893244, 0.2006790793732284, -0.16673927351339843, -0.20660184807479887, 0.23056960784488092, -0.18482270221655753, 0.1762147659883352, 0.16005489463071046, -0.3053641242442547, -0.2743810000386276, -0.08478996569797154, 0.13776900756784152, -0.15024905852062587, 0.0562594799176389, -0.23510223646769987, 0.07097725505204172, 0.006422686457491311, 0.5274842728296292};
		private static double[] closeScale={0.011697674613487459, 0.07018604768092476, 0.058488373067437295, 0.031193798969299892, 0.04289147358278735, 0.07798449742324973, 0.027294574098137406, 0.03509302384046238, 0.04289147358278735, 0.03509302384046238, 0.04679069845394984, 0.0857829471655747, 0.05458914819627481, 0.01949612435581243, 0.031193798969299892, 0.027294574098137406, 0.04679069845394984, 0.06628682280976227, 0.058488373067437295, 0.031193798969299892};
		private static double[] openScale={0.15150535446529206, 0.007575267723264604, 0.08332794495591064, 0.11362901584896906, 0.015150535446529207, 0.037876338616323016, 0.07575267723264603, 0.1666558899118213, 0.1666558899118213, 0.11362901584896906, 0.02272580316979381, 0.053026874062852225, 0.037876338616323016, 0.10605374812570445, 0.053026874062852225, 0.12877955129549826, 0.053026874062852225, 0.015150535446529202, 0.1666558899118213, 0.053026874062852225};
		private static double[] highScale={0.020078978866126227, 0.1154541284802258, 0.015059234149594671, 0.05521719188184712, 0.030118468299189342, 0.1054146390471627, 0.08031591546450491, 0.07529617074797335, 0.10039489433063113, 0.04517770244878401, 0.11043438376369424, 0.09537514961409958, 0.015059234149594671, 0.08533566018103647, 0.08031591546450491, 0.08533566018103647, 0.09035540489756802, 0.050197447165315566, 0.015059234149594671, 0.1154541284802258};
		private static double[] lowScale={0.04187313972216596, 0.0179456313094997, 0.13758317337283102, 0.10169191075383163, 0.04785501682533253, 0.11963754206333133, 0.1256194191664979, 0.08374627944433193, 0.07776440234116537, 0.08374627944433193, 0.07776440234116537, 0.11963754206333133, 0.0717825252379988, 0.11365566496016476, 0.04785501682533253, 0.08374627944433193, 0.011963754206333133, 0.13160129626966446, 0.11365566496016476, 0.04187313972216596};
		private static double[] volScale={0.7417119794601934, 2.373478334272619, 1.7801087507044642, 2.9668479178407736, 2.22513593838058, 2.9668479178407736, 0.5933695835681547, 1.9284511465965029, 2.818505521948735, 0.8900543753522321, 2.22513593838058, 0.14834239589203868, 0.7417119794601934, 1.4834239589203868, 3.263532709624851, 0.5933695835681547, 1.6317663548124255, 1.4834239589203868, 0.29668479178407736, 0.14834239589203868};


	
	@Override
	public String getStartDate(){
		return START_DATE;
	}
	
	public S_10() {
		this(myStatisticType);
	}
	
	public S_10(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}



}
