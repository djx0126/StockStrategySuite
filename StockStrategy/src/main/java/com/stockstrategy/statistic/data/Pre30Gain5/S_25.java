package com.stockstrategy.statistic.data.Pre30Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_25 extends AbstractSPreGain {
	
	private static final int PREVIOUS = 30;
    private static final int GAIN = 5;
	private static final String myStatisticType = Constant.SPre30Gain5_25;

	// 2014.11.3 created
	//9495.211(avgGain=5.973%,sum=943.686,lost=82.301,rate=12.466,count=158)


	private static double[] closeOffset={0.010613562270220613, -6.580220681619747E-4, 3.2888640867742206E-4, -0.025243783285977796, -0.005954542686682308, -0.010075308557190323, -0.008773924307505102, 0.00785744074740352, 0.015529147721658263, 0.003889230307427934, 0.03932942172587891, 0.0026603114235910744, -0.0019967597560753563, 0.0, -0.005608134284578172, 0.036897561949836476, -0.01288133637732988, 7.148881974773445E-4, 0.005037440691613969, 0.0010870956412141369, 0.006212143100285303, -0.005276558061705198, 0.013847701185259844, 7.612525733566536E-4, -0.003415177351812601, 0.006090118824887955, 0.0011514663799774962, -0.011379545743498703, -0.008159487318318124, -0.004386850248113693};
	private static double[] openOffset={-0.001318041927913143, -0.0016419470669767865, -0.022358329336789665, -0.01006012514362442, 0.007830981768295981, -0.004715513014083975, 0.0028231593363790934, 0.010834026513278432, 0.005193418920872211, 0.00558691487760649, 0.007675015790230413, -0.002015291235325575, -0.0013383507722621957, -0.0049824005189398985, 0.0307896641252254, 3.5467089100888095E-4, 0.001421587134794238, 0.016733803021062824, -7.244279943322211E-4, 0.010624350975261652, 0.016430651602996307, 0.010815964741477465, 0.011479817683430127, -0.0046284041820710475, 0.006182232250692644, -3.889785140490885E-4, -0.010752313560297709, -0.010862610235688906, -0.006608158492621956, -0.001091649879938835};
	private static double[] highOffset={0.0, -0.003930293628432402, -0.021623810843955777, -0.00585435977923598, -0.0021371300583727434, 6.095362150091535E-4, -0.0018260481642242603, -0.0018184024412197007, 0.02106834252218415, 9.534460073225764E-4, 0.013901196227153356, 0.003297620109980036, -0.005917805927444375, -0.0019548171724410525, -0.004848890882503729, 0.026851628066073736, 0.00863436952420173, 0.011415307472739654, 0.02221239437099866, 0.0028832934578658168, 0.002902563732564215, -7.274616138145802E-4, 0.011895743004799912, -0.005652502551011703, -0.003727872519243153, 0.013955951882764375, -0.0045755768125095245, -0.012708318419182352, -0.004762450309128512, -0.001091649879938835};
	private static double[] lowOffset={0.010473575605629427, -0.00980543011707104, -0.002998592915220589, -0.0201128231681583, 0.001906196835946656, -0.0019061968359466166, -0.0031585192448298056, 0.012774288036428919, -0.042764538435454545, -9.957080752199597E-4, 0.010400751177110342, 0.020460233942370662, -0.0020389449747123286, -0.005055871789888226, 0.03685129298800305, -3.5583327013432106E-4, 0.007534417897257678, 0.004364805402450017, 0.0029344333390494072, 0.007049787567551633, -0.0018663306844214516, 0.012852123570742653, 0.0085234480655952, 0.0031413842708912063, -0.004703596321943908, 0.009060440249368227, 0.0, -0.013327771859039429, -0.017404372624981887, 0.0029771803439146986};
	private static double[] volOffset={-0.2854316010175085, 0.13920021571975716, -0.19548992774890403, 0.287376732241698, 0.0837860948144727, 0.16786417485056535, -0.13015719753764202, -0.21665278191544549, 0.20311510234621724, -0.05472209147127056, 0.38422199127408313, -0.07805661100389301, -0.1207549738058559, -0.2436455187529039, -0.1988227443122932, 0.13846574955004892, 0.6130367647140682, -0.3496019759176212, 0.10755002669581151, -0.1794357335832009, 0.226372014259921, -0.21570133852329745, 0.6300281386747292, -0.34405159931375767, -0.15008297129596812, 0.06852143039468224, -0.05723391912644014, 0.053436254589860184, 0.3310591093456699, -0.37789649042025597};
	private static double[] closeScale={0.024819088496989057, 0.06949344779156935, 0.049638176993978114, 0.09927635398795623, 0.06452963009217154, 0.009927635398795622, 0.08438490088976279, 0.014891453098193433, 0.06452963009217154, 0.0893487185891606, 0.004963817699397811, 0.014891453098193433, 0.06949344779156935, 0.029782906196386866, 0.08438490088976279, 0.11416780708614965, 0.014891453098193433, 0.09431253628855842, 0.07942108319036498, 0.07445726549096716, 0.014891453098193433, 0.03971054159518249, 0.02978290619638687, 0.10424017168735403, 0.049638176993978114, 0.009927635398795622, 0.09431253628855842, 0.05956581239277373, 0.06452963009217154, 0.03971054159518249};
	private static double[] openScale={0.046892889125913245, 0.10941674129379757, 0.023446444562956623, 0.015630963041971084, 0.09378577825182649, 0.16412511194069634, 0.08597029673084094, 0.1328631858567542, 0.09378577825182649, 0.10941674129379757, 0.16412511194069634, 0.17975607498266744, 0.03126192608394216, 0.1172322228147831, 0.10941674129379757, 0.01563096304197108, 0.07033933368886987, 0.03126192608394216, 0.1563096304197108, 0.1563096304197108, 0.03126192608394216, 0.17975607498266744, 0.046892889125913245, 0.16412511194069634, 0.01563096304197108, 0.14067866737773974, 0.01563096304197108, 0.1563096304197108, 0.1172322228147831, 0.1719405934616819};
	private static double[] highScale={0.13077924534831556, 0.0871861635655437, 0.04359308178277185, 0.11832407912466646, 0.049820664894596405, 0.01868274933547365, 0.01868274933547365, 0.13077924534831556, 0.14323441157196468, 0.13077924534831556, 0.06850341423007006, 0.13700682846014012, 0.0747309973418946, 0.01868274933547365, 0.0747309973418946, 0.031137915559122754, 0.08095858045371916, 0.09341374667736826, 0.012455166223649101, 0.0747309973418946, 0.0871861635655437, 0.14323441157196468, 0.06850341423007006, 0.12455166223649101, 0.12455166223649101, 0.10586891290101735, 0.08095858045371916, 0.06227583111824551, 0.09964132978919281, 0.031137915559122754};
	private static double[] lowScale={0.0583528925780678, 0.15005029520074575, 0.025008382533457628, 0.18339480524535595, 0.07502514760037288, 0.10003353013383051, 0.10003353013383051, 0.04168063755576271, 0.008336127511152543, 0.1167057851561356, 0.14171416768959322, 0.016672255022305085, 0.16672255022305085, 0.08336127511152543, 0.07502514760037288, 0.18339480524535595, 0.1917309327565085, 0.1917309327565085, 0.0583528925780678, 0.1750586777342034, 0.1750586777342034, 0.008336127511152543, 0.12504191266728815, 0.1750586777342034, 0.16672255022305085, 0.1917309327565085, 0.12504191266728815, 0.04168063755576271, 0.06668902008922034, 0.16672255022305085};
	private static double[] volScale={1.8112769770206647, 6.943228411912548, 0.9056384885103324, 5.131951434891883, 3.6225539540413294, 6.037589923402216, 4.226312946381551, 3.3206744578712186, 3.6225539540413294, 6.641348915742437, 4.8300719387217725, 5.433830931061994, 0.6037589923402216, 6.641348915742437, 5.433830931061994, 6.037589923402216, 6.943228411912548, 2.716915465530997, 4.226312946381551, 6.037589923402216, 1.509397480850554, 6.037589923402216, 1.8112769770206647, 3.018794961701108, 1.509397480850554, 5.735710427232105, 4.8300719387217725, 0.9056384885103324, 2.716915465530997, 3.3206744578712186};

	
	
	
	public S_25() {
		this(myStatisticType);
	}
	
	public S_25(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}


}
