package com.stockstrategy.statistic.data.Pre30Gain10;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_12 extends AbstractSPreGain {
	
	private static final int PREVIOUS = 30;
    private static final int GAIN = 10;
	private static final String myStatisticType = Constant.SPre30Gain10_12;


	// created 2014.11.10
	// 10342.578(avgGain=7.155%,sum=615.295,lost=133.992,rate=5.592,count=86)
	
	private static double[] closeOffset={0.006521299515277759, 0.011283119049892158, 0.001400286882595696, -0.019046808888344764, -0.010608294943074952, 0.007853359222577188, 0.0, 0.009118338676354682, -0.011075505043670527, -0.012169359439912545, -0.006632880473684914, 0.004435834361957387, 0.003185548379464141, -0.001823173495379146, 0.03139508116008275, -0.004242519691393453, -3.981066205100333E-4, 0.015803110033461895, 0.0021512058709659003, -0.023096764424526163, 0.02028578709069778, 0.0056402673526926616, 0.005207228864578271, -0.010270251022001802, -5.772451952690986E-4, -8.240100241080596E-5, -0.02011697448254855, 0.015158857926867787, 0.010370230196246302, 0.006207907837034347};
	private static double[] openOffset={8.887063469382409E-4, 0.012719972937005936, -0.01914218951409278, -0.032094107890143275, 0.006942619132645182, -5.500880810211247E-4, 0.007685426270977382, -0.009409752413077704, -0.010051771940118526, -0.008851879361298387, 0.0072497371631099206, 0.00706310288164842, -0.004619378582541527, 0.02490017211812425, -0.002022800498514731, -0.0016114841565793583, 0.013892070955694334, 0.0036691764339415694, -0.008623159601853074, 0.011505159392031075, 0.005642422654947, 0.0016735838895661336, -0.008221902255725454, 0.0023923585950015574, -8.264407477822179E-4, -0.007286853741277978, 1.624137946764619E-4, 0.012608238807056716, 0.004538826298008154, 0.0016931580194449755};
	private static double[] highOffset={-0.009679141775517361, 0.024778177754923383, -0.01097495220238371, -0.019249717180037505, 0.003060040716270754, 0.0036233174842477817, 0.003497692585434301, 0.0010157112239285828, -0.012110585559884392, -0.012447827937090948, -0.005873894875022386, 0.012203202091360058, 0.004903072715510596, -0.005352885787211388, 0.014940349792936524, 0.00295704247805977, 0.008197453194262995, 0.013577859298691928, -0.005952124927882353, -0.026314647940389943, 0.03234887788447354, 0.00172780072923827, -9.058957112122554E-4, -0.004664158907534086, 6.516047462478609E-4, -0.0017895658589837664, -0.01944630884314043, 0.02880197660896334, 0.011807109180146194, 0.003840383765197555};
	private static double[] lowOffset={-0.006044444317101574, 0.011757293684152736, 0.004271371539549687, -0.02771298786703247, -0.0011828272819655293, -3.9356095298952426E-4, 0.01253128076613081, 0.004313275881599084, -0.019198462041207176, -0.005443332446384856, -0.0029266275519108705, 0.012300253626728154, 0.0017596816581153616, 0.019506097500866328, 0.002358313064951971, -0.005992597720046112, 0.015389352782421472, 0.005197177999356019, 0.027678455804145075, -0.007923018737049658, 0.00809138256234229, -0.011643944209715791, 0.025491805095001877, -0.006332781818889728, -0.004751901802092127, -0.0011592233282283722, -0.005015098492295429, 0.010591055184677461, 0.012232063286909256, -0.0037742598206128865};
	private static double[] volOffset={-0.22410148632602128, 0.5161898091544289, -0.6442469398648403, 0.14470325072209975, 0.1315560669407606, 0.1636832984358488, -0.09798815674592559, -0.21951025952612524, 0.005356217720136159, -0.1432035445907689, -0.09664233167897562, 0.15874516775039108, 0.07039092711057096, -0.19960529917929906, -0.07363794524346225, 0.17326435234348106, -0.10819867328575869, 0.39111421056289014, -0.5569961478783241, -6.344311534038682E-4, 0.5858055864806058, 0.011760711749433926, -0.0598832208171638, -0.004801886311807574, 0.09713532791941547, -0.24499758922700915, -0.303490956372541, 0.15145096734448568, 0.19580065448861667, 0.053058812194385574};
	private static double[] closeScale={0.08451578304502409, 0.0745727497456095, 0.08451578304502409, 0.0745727497456095, 0.044743649847365695, 0.099430332994146, 0.0944588163444387, 0.099430332994146, 0.11434488294326789, 0.029829099898243797, 0.0795442663953168, 0.0696012330959022, 0.08948729969473139, 0.0696012330959022, 0.0049715166497073, 0.1044018496438533, 0.0248575832485365, 0.049715166497073, 0.10937336629356059, 0.08451578304502409, 0.054686683146780296, 0.099430332994146, 0.0795442663953168, 0.029829099898243797, 0.0198860665988292, 0.009943033299414598, 0.0049715166497073, 0.0099430332994146, 0.014914549949121898, 0.099430332994146};
	private static double[] openScale={0.1968910252335317, 0.10272575229575566, 0.0684838348638371, 0.008560479357979638, 0.1540886284436335, 0.059923355505857465, 0.18833054587555204, 0.1968910252335317, 0.017120958715959277, 0.08560479357979639, 0.1540886284436335, 0.11984671101171493, 0.017120958715959277, 0.14552814908565384, 0.11984671101171493, 0.03424191743191855, 0.11984671101171493, 0.1369676697276742, 0.10272575229575566, 0.025681438073938915, 0.08560479357979639, 0.10272575229575566, 0.0684838348638371, 0.025681438073938915, 0.18833054587555204, 0.1968910252335317, 0.16264910780161312, 0.16264910780161312, 0.1112862316537353, 0.059923355505857465};
	private static double[] highScale={0.13065474847857714, 0.04355158282619238, 0.024886618757824216, 0.10576812972075292, 0.024886618757824216, 0.09332482034184081, 0.04977323751564843, 0.11198978441020897, 0.08710316565238475, 0.08710316565238475, 0.0808815109629287, 0.03732992813673632, 0.11821143909966503, 0.055994892205104486, 0.04355158282619238, 0.09332482034184081, 0.11198978441020897, 0.14309805785748925, 0.11198978441020897, 0.06221654689456054, 0.13687640316803318, 0.04355158282619238, 0.03110827344728027, 0.01866496406836816, 0.04355158282619238, 0.03732992813673632, 0.14309805785748925, 0.024886618757824216, 0.06843820158401659, 0.13687640316803318};
	private static double[] lowScale={0.21000606158074817, 0.1552218716031617, 0.09130698329597746, 0.1552218716031617, 0.21000606158074817, 0.14609117327356394, 0.06391488830718423, 0.02739209498879324, 0.04565349164798873, 0.07304558663678197, 0.12782977661436845, 0.07304558663678197, 0.02739209498879324, 0.16435256993275943, 0.1552218716031617, 0.09130698329597746, 0.13696047494396618, 0.12782977661436845, 0.009130698329597746, 0.07304558663678197, 0.018261396659195493, 0.02739209498879324, 0.02739209498879324, 0.018261396659195493, 0.14609117327356394, 0.018261396659195493, 0.12782977661436845, 0.16435256993275943, 0.12782977661436845, 0.07304558663678197};
	private static double[] volScale={2.716915465530997, 5.131951434891883, 6.3394694195723265, 0.3018794961701108, 6.641348915742437, 2.1131564731907755, 4.8300719387217725, 5.131951434891883, 6.641348915742437, 2.4150359693608863, 3.3206744578712186, 4.226312946381551, 2.716915465530997, 2.4150359693608863, 2.716915465530997, 2.1131564731907755, 2.4150359693608863, 3.018794961701108, 6.943228411912548, 2.1131564731907755, 0.6037589923402216, 0.6037589923402216, 3.3206744578712186, 3.3206744578712186, 1.8112769770206647, 6.641348915742437, 3.6225539540413294, 2.4150359693608863, 3.6225539540413294, 3.92443345021144};

	
	public S_12() {
		this(myStatisticType);
	}
	
	public S_12(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}


}
