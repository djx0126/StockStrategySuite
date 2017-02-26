package com.stockstrategy.statistic.data.Pre20Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_05 extends AbstractSPreGain {
	
	private static int PREVIOUS = 20;
	private static int GAIN = 5;
	private static String myStatisticType = Constant.SPre20Gain5_05;
	private static String START_DATE = "20141224";
	
	//10615.382(avgGain=7.753%,sum=4023.630,lost=35.299,rate=114.988,count=519,accuracy=98.266)
		//4709.457(avgGain=7.009%,sum=1310.765,lost=6.655,rate=197.972,count=187,accuracy=97.861)

		private static double[] closeOffset={0.031739245555533965, 0.010812869282217433, 0.006619349710458868, 7.417499359102965E-4, -7.417499359102623E-4, -0.001479710058047791, 0.004454341426249989, 0.004500501226676471, -0.011899223299707757, -0.011581872549815147, 7.148881974773445E-4, -0.025697176284554504, -0.005361728372108067, 0.02537247995650342, -0.007699217435012922, -0.0034550260337046315, -0.009530355391133493, 0.012292173804551393, 0.0048758513324625415, -0.014466340020563519};
		private static double[] openOffset={0.011505162405324749, 0.03404192236338022, 0.0044240907048290964, 7.417499359102965E-4, -7.417499359102623E-4, 0.003721483627760004, 0.0037536489671186994, -0.010429528218932175, -0.009465300498007125, -0.0021553120261348516, -0.011323631129216848, -0.018448441504866193, 0.022663984635943785, -0.007686828666290958, 6.932076202867445E-4, -0.008926524947970462, 0.006160308704818433, 0.027557019115353156, -0.010382035793721775, 0.0231811009696183};
		private static double[] highOffset={0.0014146412570017136, 0.00930922413477143, 0.00656376950238824, 0.0014721860941537374, -0.0022064119358181744, 0.0, 0.005165840640739311, -0.0036961455599637803, -0.010903363010324659, -0.01203501003834979, -0.021787363633448603, -0.005280241115432671, -0.00973067924544433, 0.031938023239875366, -0.004123079846706707, -0.007458792703108454, -6.717626736162849E-4, 0.004724306684813137, -0.004052544011196864, -0.0013424878755093038};
		private static double[] lowOffset={0.0036342846550940556, -0.0016843322545719618, 0.008187456366572572, -0.0029951462293594214, 0.0, 0.0022444209224522942, 0.007566297332364971, -0.003799626002902317, -0.009726208849640964, -0.006608158492621956, -0.0029049906434072226, -0.017728766960431602, 0.008419542825660174, 0.005705099865946171, -0.010636314845785028, 0.0, -0.005567954238942251, 0.009084527961779335, 0.0035452807646497546, -0.00846054968005168};
		private static double[] volOffset={-0.01149764170516038, -0.024517302518296168, 0.05255459905363468, 0.30666415815167775, -0.09928197953037828, -0.1715325128901445, 0.048547742368991405, -0.036726237453949116, -0.1060776905457921, -0.08253236251815498, -0.027816491707591397, -0.5120716318767595, -0.09054745831007807, 0.49879880821085715, 0.04141300301233846, 0.09901201906211687, -0.1110930267859627, 0.03552820187264969, -0.315897481116098, 0.40805497454447764};
		private static double[] closeScale={0.011697674613487459, 0.07018604768092476, 0.058488373067437295, 0.031193798969299892, 0.03899224871162486, 0.07798449742324973, 0.027294574098137406, 0.03509302384046238, 0.04679069845394984, 0.03509302384046238, 0.04679069845394984, 0.0857829471655747, 0.050689923325112324, 0.01949612435581243, 0.031193798969299892, 0.027294574098137406, 0.04679069845394984, 0.06628682280976227, 0.058488373067437295, 0.031193798969299892};
		private static double[] openScale={0.15150535446529206, 0.007575267723264604, 0.08332794495591064, 0.11362901584896906, 0.015150535446529207, 0.037876338616323016, 0.07575267723264603, 0.1666558899118213, 0.1666558899118213, 0.11362901584896906, 0.02272580316979381, 0.053026874062852225, 0.037876338616323016, 0.10605374812570445, 0.053026874062852225, 0.12877955129549826, 0.053026874062852225, 0.037876338616323016, 0.1666558899118213, 0.053026874062852225};
		private static double[] highScale={0.025098723582657786, 0.1154541284802258, 0.010039489433063114, 0.05521719188184712, 0.030118468299189342, 0.1054146390471627, 0.08031591546450491, 0.07529617074797335, 0.10039489433063113, 0.0351382130157209, 0.11043438376369424, 0.09537514961409958, 0.015059234149594671, 0.08533566018103647, 0.08031591546450491, 0.08533566018103647, 0.09035540489756802, 0.050197447165315566, 0.015059234149594671, 0.1154541284802258};
		private static double[] lowScale={0.04187313972216596, 0.0179456313094997, 0.13758317337283102, 0.10169191075383163, 0.04785501682533253, 0.11963754206333133, 0.1256194191664979, 0.08374627944433193, 0.07776440234116537, 0.08374627944433193, 0.07776440234116537, 0.11963754206333133, 0.0717825252379988, 0.11365566496016476, 0.04785501682533253, 0.08374627944433193, 0.0059818771031665665, 0.13160129626966446, 0.11365566496016476, 0.04187313972216596};
		private static double[] volScale={0.7417119794601934, 2.373478334272619, 1.7801087507044642, 2.9668479178407736, 2.22513593838058, 2.9668479178407736, 0.44502718767611604, 1.9284511465965029, 2.818505521948735, 0.5933695835681547, 2.22513593838058, 0.29668479178407736, 0.7417119794601934, 1.4834239589203868, 3.263532709624851, 0.5933695835681547, 1.6317663548124255, 1.4834239589203868, 0.29668479178407736, 0.14834239589203868};

	@Override
	public String getStartDate(){
		return START_DATE;
	}
	
	public S_05() {
		this(myStatisticType);
	}
	
	public S_05(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}



}
