package com.stockstrategy.statistic.data.Pre30Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_16 extends AbstractSPreGain {
	
	private static final int PREVIOUS = 30;
    private static final int GAIN = 5;
	private static final String myStatisticType = Constant.SPre30Gain5_16;


	private static double[] closeOffset={-0.017608945058194537, 0.009649916770392878, -0.0023994231304545715, -0.01360961804553451, 0.00116104341751975, 0.002851242291887736, 0.006550568861590169, -0.0015811709768006014, 0.00662653200474423, 2.671759431562084E-4, 0.012196278360470532, 0.0019283643311358253, 0.004579597604752802, 0.010877353459072482, -0.017110358448832654, 0.008470920315488606, -0.005573313340958296, -0.010666458562495823, -0.015002340358445481, 0.009798388679391421, -0.006922195548073924, -0.002030010038622028, 0.004453036484258334, 0.002951340382713614, 0.007009088780597485, -0.009576607236513825, -0.013732282102309652, -0.009324194483926842, 0.009076238735311071, -0.001608270820899476};
	private static double[] openOffset={0.011804730077501064, -0.005345230329183264, -0.01333981329241858, 0.004141099141140259, 0.0013022333052446226, 0.005908859610520219, -3.964349722132287E-4, 0.00464774558615923, 0.0017390530752414742, 0.05579477725989707, 5.518355023324105E-4, 0.004300667292079642, 0.014747295482429099, -0.014049633514078206, 0.002380469023658506, -0.00571907212939232, -0.011352068769398531, -0.012376932873748919, -0.007286117864086221, 0.002198964099938142, -0.011011593855702384, 0.008941672835573648, -6.448323113430214E-4, 0.007146368130546756, -0.004501057411976083, -0.008710165299550448, -0.010251134821773516, 0.008504974299795335, -0.003471398868831763, 0.004593124370361718};
	private static double[] highOffset={-0.002075485345098237, 0.0027261139040949413, -0.0034642890760670673, -0.00216288895627752, 0.005620359970091627, -0.009906366180664554, 0.012132640807143491, -0.0032599949042266584, 0.006940478642594235, 0.004644905045763538, 0.010944070485381324, 2.7374377326315626E-4, 0.008154057963050914, -0.002226442463678015, -0.005790678880895428, 0.0020592474897060376, -0.01073729046733654, -0.011002096683622693, -0.011750619540549324, 0.0011480841866360547, -0.012092506920110312, 0.004495325169588838, -0.01151910869655132, 0.02219276953571053, -0.008156004617912691, -0.011708102599215224, -0.010921643834030168, 0.003972783878702427, 0.007440699738250541, 0.0013554385526309122};
	private static double[] lowOffset={0.030399347470774205, 2.6899628107004297E-4, -0.0033505372318207876, -0.001664550310399188, 0.0105930734974242, -0.0053288335050669456, 0.0039904827328046686, 0.0013383507722622451, 0.004039977801740014, 0.010821964787956877, 0.0025039469343999606, 0.0012574080963644921, 0.02040042718517504, -0.002731872989545714, -0.0058371879407638745, 0.006985356440062365, -0.011067233831266109, -0.015008023261231419, -0.011880934212995825, -0.0010521979269287759, 0.003429005523749652, -0.007872623099885408, 0.007211092915764871, 0.002918300107924662, 0.004145721685326421, -0.02085643767553772, -0.01299511086882586, 0.005503805573638007, 0.004047036688556084, 6.357701060791677E-4};
	private static double[] volOffset={-0.11349533424606889, 0.03397380673107237, -0.12132282490481378, -0.019596355353802084, -0.024859497197100597, 0.01850916647866123, 0.26500185841650864, -0.11188057622460663, 0.05892884372944343, -0.4115788994857367, 0.3645007978278621, -0.16426442219126022, -0.22701337016031065, 0.012358416081143896, -0.06980153015193118, -0.20274584775157536, 0.0790141475318135, -8.844399144463096E-4, 7.061111075680666E-4, 0.2553411105680839, -0.33727659343764077, 0.025029174541477912, -0.2335987284851454, 0.29428564832773685, -0.2175084663041124, 0.16133162508276844, -0.09467875368248513, -0.09983243648721025, -0.01235813511577946, 0.09687167029435756};
	private static double[] closeScale={0.07094543890212424, 0.054573414540095566, 0.04365873163207645, 0.08186012181014335, 0.027286707270047783, 0.11460417053420069, 0.054573414540095566, 0.04365873163207645, 0.09823214617217202, 0.010914682908019113, 0.07640278035613379, 0.07640278035613379, 0.10368948762618158, 0.10914682908019113, 0.0873174632641529, 0.11460417053420069, 0.06003075599410512, 0.054573414540095566, 0.038201390178066896, 0.005457341454009557, 0.10368948762618158, 0.09823214617217202, 0.04365873163207645, 0.04365873163207645, 0.06548809744811468, 0.038201390178066896, 0.09277480471816246, 0.021829365816038226, 0.038201390178066896, 0.04365873163207645};
	private static double[] openScale={0.15270163796569308, 0.07635081898284654, 0.06786739465141915, 0.10180109197712872, 0.15270163796569308, 0.016966848662854788, 0.1357347893028383, 0.11876794063998351, 0.08483424331427394, 0.008483424331427394, 0.00848342433142739, 0.1272513649714109, 0.05090054598856436, 0.08483424331427394, 0.008483424331427394, 0.08483424331427394, 0.07635081898284654, 0.059383970319991757, 0.1696684866285479, 0.1357347893028383, 0.08483424331427394, 0.05090054598856436, 0.17815191095997526, 0.033933697325709576, 0.008483424331427394, 0.02545027299428218, 0.02545027299428218, 0.1272513649714109, 0.05090054598856436, 0.05090054598856436};
	private static double[] highScale={0.11592984724551562, 0.0515243765535625, 0.03220273534597656, 0.019321641207585935, 0.05796492362275781, 0.045083829484367184, 0.07728656483034374, 0.103048753107125, 0.03220273534597656, 0.13525148845310156, 0.07084601776114843, 0.03864328241517187, 0.07084601776114843, 0.103048753107125, 0.07084601776114843, 0.05796492362275781, 0.012881094138390625, 0.05796492362275781, 0.045083829484367184, 0.1094893001763203, 0.1094893001763203, 0.03864328241517187, 0.12237039431471093, 0.05796492362275781, 0.06440547069195313, 0.103048753107125, 0.12237039431471093, 0.09660820603792969, 0.06440547069195313, 0.11592984724551562};
	private static double[] lowScale={0.008411518689852779, 0.15140733641735002, 0.14299581772749725, 0.008411518689852779, 0.05046911213911667, 0.1177612616579389, 0.15981885510720278, 0.10093822427823335, 0.16823037379705558, 0.16823037379705558, 0.08411518689852779, 0.06729214951882223, 0.016823037379705558, 0.14299581772749725, 0.1177612616579389, 0.17664189248690837, 0.07570366820867501, 0.16823037379705558, 0.16823037379705558, 0.17664189248690837, 0.17664189248690837, 0.15140733641735002, 0.10934974296808612, 0.08411518689852779, 0.042057593449263894, 0.16823037379705558, 0.14299581772749725, 0.15140733641735002, 0.05888063082896945, 0.07570366820867501};
	private static double[] volScale={0.24650475374741337, 5.1765998286956805, 0.9860190149896535, 3.697571306211201, 4.683590321200854, 4.683590321200854, 1.7255332762318936, 0.24650475374741337, 2.9580570449689603, 0.24650475374741337, 4.190580813706028, 0.49300950749482675, 0.24650475374741337, 2.9580570449689603, 4.190580813706028, 2.21854278372672, 2.21854278372672, 0.49300950749482675, 4.683590321200854, 4.930095074948268, 4.930095074948268, 4.190580813706028, 0.7395142612422401, 2.9580570449689603, 3.451066552463787, 3.204561798716374, 4.930095074948268, 3.697571306211201, 1.232523768737067, 0.7395142612422401};

	
	
	
	public S_16() {
		this(myStatisticType);
	}
	
	public S_16(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}


}
