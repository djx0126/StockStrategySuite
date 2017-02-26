package com.stockstrategy.statistic.data.Pre30Gain10;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_04 extends AbstractSPreGain {
	
	private static final int PREVIOUS = 30;
    private static final int GAIN = 10;
	private static final String myStatisticType = Constant.SPre30Gain10_04;
	private static String START_DATE = "20140303";


	private static double[] closeOffset={-0.008499184787412255, -0.001680058118021617, 0.0028037184651781236, -0.0016844021705741522, 0.002810977333828443, 0.003966187217601382, 0.021546858093362962, -0.03288725822057223, -0.0010723325988836925, -5.351750300192892E-4, 0.0021446684665891503, -0.0016094934365698198, -0.009009482573724104, 0.010618976010293994, -0.003213044114615075, -0.0015976516432142376, 0.00967527721842408, 0.004370295816314819, 0.006639025825112113, 5.57860682875506E-4, -0.01804125722146707, 0.0021473194890855884, 0.0016174859736191843, -0.00643411000540996, 0.031448382858380794, 0.04569755028532729, 5.771356090583232E-4, -0.002303954909564312, 0.006948859955327825, -0.0029088821535877556};
	private static double[] openOffset={-5.585781887746963E-4, 0.0011178757315004901, 0.0022444209224522942, 0.0011265751632542319, 0.007394845672409134, 0.006939606751779885, -0.030390506565698555, -0.005938239564658324, -5.358353325062645E-4, -0.007614724463194729, -0.0037601488733067286, -0.006897919308749824, 0.008505426937652827, -0.00160750762890299, 0.008168556348719831, 0.004810695757829285, 0.00868617918509262, 0.007187723916929047, 0.002235755166254153, -0.018646819105962, 0.0016134798071816515, 0.0021606735209272, -0.003236992490422661, 0.023749925620968563, 0.006866458952917025, 0.0, 0.0011550391965321706, 0.007583575181126842, -0.007004901568658489, 0.0070049015686585205};
	private static double[] highOffset={-0.005553713558150317, 0.002212970301121146, 5.55008998095382E-4, 0.0, -0.022779294070601613, 0.004488871817207062, -0.01714179091175578, -0.007525092655739007, -0.0026562436437400313, 0.0010605487123936236, 0.0010631449174618483, -0.011553859876415403, -0.023448906994086246, 0.005232526933676936, 0.0026400961662084, -0.0031661949366655596, 0.011184166952513638, 0.001080335089187673, 0.007088453988239456, -0.010321430321830838, -0.003209087127704338, 0.0, 0.0, -0.010528866743625458, 0.04185846216100947, 0.009657698417118405, -0.006273568447774432, 0.0022708259941005347, 0.0057295617541798845, 0.0};
	private static double[] lowOffset={-0.008543773543293658, -0.0011265751632542353, 0.0011265751632542319, 0.002261955539877947, 0.005132890132531214, 0.006939606751779885, -0.002904990643407174, -0.028028723600243537, -0.0010843812922198969, -0.001081680464287731, 0.005435546321354979, -0.008128019185176019, 0.0021526412444037362, 0.00216336428363086, -0.0032410186378327684, 0.0048706433713409806, -0.028516915061884737, 0.009984220906600923, 0.007356303912319806, -0.006794837147752076, -0.013823397432404502, 0.006580345311321542, -5.521863196566337E-4, 0.015161140078273277, 0.006331774581324861, 0.005250097968490274, -0.005829543660723844, 0.0070049015686585205, -0.002932451937533729, 0.004701474676380413};
	private static double[] volOffset={-0.03995271514786384, 0.0434393969035351, 0.21390004261529352, -0.2830774525637542, -0.21950689295752726, 0.08670540119942279, -0.20598791879104406, 0.12701047119009823, 0.04247971511321274, 0.05187600433452342, 0.011065837639022974, -0.19977012873799518, -0.12677300316640586, 0.23746355221681276, 0.043603376063228984, -0.2614209505916472, 0.19177821247385773, 0.01033851737386549, 0.14763258056748285, -0.18975639915998047, 0.019370127102002296, -0.12466189549492351, -0.20085926103841253, -0.25106056080908873, 0.7072320196663446, 0.40075251433979336, 0.016466410123302813, -0.029806851169924178, 0.08160394648659115, -0.16705591512838894};
	private static double[] closeScale={0.1115785930916032, 0.04781939703925851, 0.12220512576699398, 0.0531326633769539, 0.07438572872773547, 0.12220512576699398, 0.00531326633769539, 0.04250613070156312, 0.031879598026172344, 0.1062653267539078, 0.02656633168847695, 0.06907246239004007, 0.12220512576699398, 0.07969899506543085, 0.06907246239004007, 0.09563879407851703, 0.07438572872773547, 0.05844592971464929, 0.11689185942929858, 0.05844592971464929, 0.02656633168847695, 0.02656633168847695, 0.07438572872773547, 0.04781939703925851, 0.12220512576699398, 0.010626532675390783, 0.09032552774082164, 0.11689185942929858, 0.06907246239004007, 0.031879598026172344};
	private static double[] openScale={0.16515009902618363, 0.08257504951309182, 0.008257504951309181, 0.016515009902618363, 0.05780253465916427, 0.14863508912356527, 0.14037758417225607, 0.1321200792209469, 0.18992261388011117, 0.049545029707855084, 0.08257504951309182, 0.181665108928802, 0.15689259407487444, 0.18992261388011117, 0.04128752475654591, 0.1321200792209469, 0.15689259407487444, 0.14037758417225607, 0.05780253465916427, 0.16515009902618363, 0.14037758417225607, 0.12386257426963772, 0.181665108928802, 0.12386257426963772, 0.06606003961047345, 0.08257504951309182, 0.07431754456178263, 0.024772514853927542, 0.10734756436701935, 0.14863508912356527};
	private static double[] highScale={0.06879546263594784, 0.125082659338087, 0.03752479780142609, 0.11257439340427829, 0.006254132966904349, 0.14384505823880003, 0.14384505823880003, 0.14384505823880003, 0.10006612747046958, 0.08130372856975654, 0.10006612747046958, 0.125082659338087, 0.006254132966904349, 0.03127066483452175, 0.07504959560285218, 0.04377893076833044, 0.0375247978014261, 0.14384505823880003, 0.10632026043737393, 0.012508265933808698, 0.14384505823880003, 0.10632026043737393, 0.04377893076833044, 0.05003306373523479, 0.006254132966904349, 0.09381199450356524, 0.09381199450356524, 0.08755786153666088, 0.11257439340427829, 0.06879546263594784};
	private static double[] lowScale={0.074114262529132, 0.10705393476430178, 0.09058409864671688, 0.12352377088188665, 0.016469836117584888, 0.10705393476430178, 0.05764442641154711, 0.032939672235169776, 0.17293327923464133, 0.08234918058792444, 0.024704754176377332, 0.13999360699947155, 0.08234918058792444, 0.1894031153522262, 0.16469836117584888, 0.15646344311705643, 0.016469836117584888, 0.08234918058792444, 0.05764442641154711, 0.15646344311705643, 0.032939672235169776, 0.06587934447033955, 0.17293327923464133, 0.12352377088188665, 0.05764442641154711, 0.04117459029396222, 0.18116819729343375, 0.18116819729343375, 0.15646344311705643, 0.032939672235169776};
	private static double[] volScale={5.541075271520226, 5.541075271520226, 2.16824684537748, 2.4091631615305333, 5.059242639214119, 2.16824684537748, 1.2045815807652667, 1.2045815807652667, 0.2409163161530533, 4.095577374601906, 0.9636652646122132, 1.4454978969183199, 5.059242639214119, 2.6500794776835863, 0.9636652646122132, 2.6500794776835863, 3.131912109989693, 2.16824684537748, 2.6500794776835863, 0.4818326323061066, 0.9636652646122132, 1.686414213071373, 3.6137447422957996, 1.2045815807652667, 3.372828426142746, 2.4091631615305333, 1.4454978969183199, 1.4454978969183199, 2.6500794776835863, 3.131912109989693};

	
	public S_04() {
		this(myStatisticType);
	}
	
	@Override
	public String getStartDate(){
		return START_DATE;
	}
	
	public S_04(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}


}
