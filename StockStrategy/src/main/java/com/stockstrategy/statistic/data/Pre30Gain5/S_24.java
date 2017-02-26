package com.stockstrategy.statistic.data.Pre30Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_24 extends AbstractSPreGain {
	
	private static final int PREVIOUS = 30;
    private static final int GAIN = 5;
	private static final String myStatisticType = Constant.SPre30Gain5_24;
	private static String START_DATE = "20141103";

	// 2014.11.3 created
	//6900.502(avgGain=6.513%,sum=475.466,lost=32.033,rate=15.843,count=73)


	private static double[] closeOffset={-0.005349028551892359, 0.009508969188272766, 0.009721839609276946, -0.009897874328168925, -0.005049992736641625, 0.0042042388531822, -0.018201670697494315, 0.010663100825495781, 0.015210310748612874, -0.015625307896199793, 0.012198903678102386, 0.007747778000739948, 0.011887159731648084, 0.01869692791437057, -0.013307475314563944, -0.025450796337881037, 0.024583232966435067, -0.03500139638871193, 7.392635837566823E-4, 0.005030494258333405, 0.006799269707855069, -0.006799269707855051, 0.014142066393516487, 0.011475928403650824, 0.006307292131161299, -0.00853787988984824, 0.020966869112756643, 0.0011823026869008345, 0.0065098892827598785, -0.0014032144094381133};
	private static double[] openOffset={0.028824452369202898, 0.011027600798039867, -0.003423028415425318, -0.008023169506012727, 8.375981032248755E-4, -0.011989852365542328, 0.005746891746514299, 0.016851560437241203, -0.01643814347893701, 0.01216355698302777, 0.007292029277909806, 0.013620872540648522, 0.014061937475474585, -0.005041997845449019, -0.031630964753446726, 0.02082930167300009, -0.03541130154599719, 0.01246864055959803, 0.002537268065360253, 0.003406248691911496, -0.011390600478630395, 0.01958915011487512, 0.01102889338538113, 0.008117890222179555, 0.003402038688208914, 0.01602744543415074, -0.00556201175598572, 0.012688417390114792, -0.0013912275065467454, 0.0018559621489711157};
	private static double[] highOffset={0.004763789796063924, 0.017928396695917802, -0.002056597750354292, -0.005431363011634905, 0.0, -0.00781792324350105, -0.005269153075963339, 0.008954842652926412, -0.008954842652926403, 0.011843272657161993, 0.021075807191179595, 0.00769439483815509, 0.01402485485508118, 0.0, -0.022142745077260637, 0.0012717270636373099, -0.01828878571230972, -0.013260394270578266, 0.0040924290995088455, 0.017226401974510624, -0.0020869549140917565, 0.005447083721358623, 0.005516271782427669, 0.019654687996470015, -0.004887252905860998, -0.005269562913589777, 0.02147310174245202, 0.0027603555760165616, -0.004133981861590852, 0.007376729058754369};
	private static double[] lowOffset={0.00583513840062129, 0.01234236907302791, 0.009161750222296915, -0.01643940321454846, -0.008200545354730922, -0.0012779640861703679, -0.03765104620284413, 0.013652817226696768, 0.005232526933676936, -0.009115960993451121, 0.003017442782000531, 0.016307238725998784, 0.012298620483302974, 0.005112139606056925, 8.982308329706437E-4, -0.011281010409689058, 0.0013383507722622451, -0.02432684973298231, 0.004245336658516934, 0.012023479096178746, 0.0021531748710923787, -0.011834617399320038, 0.005328833505066869, 0.015000672660163613, -0.007792300009989168, 0.01244217828245838, 9.359798491447044E-4, 9.380014054644638E-4, 0.0018821025421836852, 0.0028385361304007067};
	private static double[] volOffset={0.08317227271390881, 0.35088493559115946, 0.29283024596962376, 0.11065193958673422, 5.172000134329772E-4, -0.24032633370976303, -0.18155155790302496, 0.06259556374472214, -0.4203686669851583, 0.15135606333988952, 0.09347993890239722, 0.09009903104296735, 0.0838844679343091, -0.10905119655065762, -0.2095866895325449, 0.08148397401274454, -0.16209122976188228, 0.14348870357614485, -0.11433888130924759, 0.14311041989920276, -0.044235869784283025, -0.02945725158323384, -0.079382620704882, 0.19804488091871714, -0.07816982385535752, -0.23554286120249404, 0.4881801777372744, -0.036887847609947025, -0.16903424007551374, 0.10566689625573213};
	private static double[] closeScale={0.10920398938675184, 0.04963817699397811, 0.05956581239277373, 0.05956581239277373, 0.10920398938675184, 0.05460199469337592, 0.06949344779156935, 0.019855270797591244, 0.10424017168735403, 0.024819088496989057, 0.07445726549096716, 0.08438490088976279, 0.024819088496989057, 0.034746723895784676, 0.06949344779156935, 0.08438490088976279, 0.004963817699397811, 0.07942108319036498, 0.024819088496989057, 0.049638176993978114, 0.019855270797591244, 0.0446743592945803, 0.034746723895784676, 0.03971054159518249, 0.029782906196386866, 0.03971054159518249, 0.09927635398795623, 0.05460199469337593, 0.10920398938675184, 0.029782906196386866};
	private static double[] openScale={0.00781548152098554, 0.1719405934616819, 0.1172322228147831, 0.16412511194069634, 0.054708370646898784, 0.0781548152098554, 0.023446444562956623, 0.046892889125913245, 0.07033933368886987, 0.07033933368886987, 0.0390774076049277, 0.046892889125913245, 0.17975607498266744, 0.1328631858567542, 0.10941674129379757, 0.1719405934616819, 0.17975607498266744, 0.09378577825182649, 0.0390774076049277, 0.07033933368886987, 0.17975607498266744, 0.17975607498266744, 0.1328631858567542, 0.046892889125913245, 0.023446444562956623, 0.09378577825182649, 0.03126192608394216, 0.01563096304197108, 0.16412511194069634, 0.09378577825182649};
	private static double[] highScale={0.08095858045371916, 0.09341374667736826, 0.09964132978919281, 0.06850341423007006, 0.012455166223649101, 0.09341374667736826, 0.12455166223649101, 0.0373654986709473, 0.09341374667736826, 0.01868274933547365, 0.0062275831118245506, 0.11209649601284191, 0.049820664894596405, 0.13077924534831556, 0.09964132978919281, 0.08095858045371916, 0.13077924534831556, 0.0124551662236491, 0.06227583111824551, 0.06850341423007006, 0.13077924534831556, 0.06850341423007006, 0.0373654986709473, 0.14323441157196468, 0.10586891290101735, 0.056048248006420956, 0.0871861635655437, 0.0747309973418946, 0.06850341423007006, 0.024910332447298202};
	private static double[] lowScale={0.03334451004461017, 0.14171416768959322, 0.06668902008922034, 0.18339480524535595, 0.008336127511152543, 0.1167057851561356, 0.008336127511152543, 0.13337804017844068, 0.025008382533457628, 0.025008382533457628, 0.12504191266728815, 0.06668902008922034, 0.15838642271189832, 0.14171416768959322, 0.016672255022305085, 0.1167057851561356, 0.1917309327565085, 0.15838642271189832, 0.050016765066915256, 0.07502514760037288, 0.1917309327565085, 0.008336127511152543, 0.06668902008922034, 0.06668902008922034, 0.09169740262267798, 0.04168063755576271, 0.06668902008922034, 0.12504191266728815, 0.10003353013383051, 0.0583528925780678};
	private static double[] volScale={2.4150359693608863, 2.4150359693608863, 0.6037589923402216, 1.2075179846804431, 3.018794961701108, 2.4150359693608863, 4.8300719387217725, 6.3394694195723265, 0.9056384885103324, 6.943228411912548, 6.037589923402216, 2.716915465530997, 1.8112769770206647, 3.92443345021144, 2.716915465530997, 3.018794961701108, 4.226312946381551, 2.716915465530997, 3.92443345021144, 6.641348915742437, 2.4150359693608863, 2.4150359693608863, 1.8112769770206647, 6.641348915742437, 3.3206744578712186, 6.641348915742437, 3.3206744578712186, 6.3394694195723265, 5.433830931061994, 6.037589923402216};

	
	
	
	public S_24() {
		this(myStatisticType);
	}
	
	public S_24(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}
	
//	@Override
//	public String getStartDate(){
//		return START_DATE;
//	}



}
