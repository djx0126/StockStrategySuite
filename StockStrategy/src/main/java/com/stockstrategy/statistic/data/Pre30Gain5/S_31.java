package com.stockstrategy.statistic.data.Pre30Gain5;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.AbstractSPreGain;

public class S_31 extends AbstractSPreGain {
	
	private static final int PREVIOUS = 30;
    private static final int GAIN = 5;
	private static final String myStatisticType = Constant.SPre30Gain5_31;
	private static String START_DATE = "20150203";

	//6527.389(avgGain=7.369%,sum=1960.264,lost=157.184,rate=13.471,count=266,accuracy=87.594) time:47s scale:close2:U
	//**********  end run with >>>>>>>> 1539.617(avgGain=5.333%,sum=549.332,lost=101.307,rate=6.422,count=103,accuracy=80.583), end at Sun Feb 01 21:44:35 CST 2015 passed 1652s
	private static double[] closeOffset={9.92673533677642E-4, 0.013935777943292559, -0.0026599980751743995, 0.04394528588610973, 0.004466836292594707, -0.006837699189084821, 0.0036996440836204025, 0.024834143903170566, -0.006865636588248801, 0.003939676171925088, 0.0, 0.014184897408884642, 0.0, -0.010629372521013196, -0.019907366267479316, -0.0016018256311403324, 0.0012008150007065319, -0.00418840634157516, 0.0072050506670415975, -0.001410375339879233, -0.021964491885504447, 0.01736238796741211, 0.012928790480856881, -0.0219892307302251, 0.006876554824168417, -0.007616686905849326, 0.011678533990673394, 0.005545571217057959, -0.002884319305108521, 0.011443151341907692};
	private static double[] openOffset={0.014886956212732448, -0.0012262440576887813, -0.00487064337134096, -0.008571355545199978, -0.001223940167256688, 0.006567905620615401, 0.031848537720658134, -0.014050713934613898, 0.009454962245464826, 2.07846130745364E-4, 0.015017278079416916, 0.0, -0.013768108046854095, -0.015764481203185108, -0.0034046778901634183, 0.002199976751792808, -0.0083406167678597, 0.013382401762722963, 0.004075998777876469, -0.03139417848389192, 0.018483405694013133, 0.013320484595646043, -0.015699100209715662, -3.951724402800513E-4, -1.9745146141586883E-4, 0.019583408135885112, -0.008656632887111094, -0.008485413489635977, 0.008067621748033046, 0.008646109184621591};
	private static double[] highOffset={0.08511983162109314, 0.015547768463950421, -0.003838868985039987, 0.0016122319467682053, 0.0024296258159346115, -0.0038406557753638305, 0.00874106863583069, -0.006723905316456913, 0.008369741419417816, -0.002056322411311837, 0.005782418039103403, 0.011160666315587472, -0.00991149628302468, -0.016567986486786, -0.004790061226861309, 0.0, -0.007282697655557274, 0.0019562847578620964, 0.0075153015179939674, -0.021977053298793656, -1.896068495095405E-4, 0.015632815504286763, 0.0025622167646004034, -0.010352461190852175, 0.0027107188969768396, 0.005866291110213894, 0.015429846665849262, -0.003454056191609758, 8.102511340954474E-4, 0.012756131533075327};
	private static double[] lowOffset={0.013146121893543751, 8.221384034047285E-4, 6.176267671473977E-4, 0.029879282611530025, 0.007900261083591242, -0.003760148873306777, 0.008824864954083187, -0.004644905045763525, -0.002095008799253753, 0.0027254872287388486, 0.010645005515291578, 0.004985716389448293, -0.0032580383998055983, -0.011951855485665982, -0.020155538910089792, 0.002620489855723326, -0.0014129974692324312, 0.004660074142215763, 0.002656243643740062, -0.0044857105836437336, -0.008834774012002326, 0.01496320799878433, 0.00621615309602944, -0.016784439227464563, -0.004394921867349226, 0.019513001326132113, -0.02093826174265918, -0.006291193767273939, 0.005447083721358623, 0.00701347167614475};
	private static double[] volOffset={-0.07799861249527096, 0.2499646629293655, -0.10171311507258908, 0.15818035716711365, 0.188196497390277, -0.33642470044484374, 0.3919238024003008, -0.19227994596517195, 0.2691766015173514, 0.04509645228000919, -0.1771567932426661, 0.07593785386877354, -0.05551849717571448, -0.131579682696419, 0.1606120245456199, -0.01851527108080745, 0.07607147614431481, -0.06539372609123456, -0.052831567686239304, 0.5841668842040957, -0.19991809466620303, 0.09310981664953148, -0.21707529515924945, 0.5174215911648019, 0.018105386672808494, 0.002594635161721835, 0.22626957371168213, 0.0831732902393595, -0.1391767384306744, 0.01484002572536673};
	private static double[] closeScale={0.061275207207183435, 0.09191281081077515, 0.08425340990987723, 0.007659400900897929, 0.045956405405387576, 0.007659400900897929, 0.1302098153152648, 0.10723161261257101, 0.053615806306285506, 0.06893460810808136, 0.09191281081077515, 0.12255041441436687, 0.053615806306285506, 0.11489101351346895, 0.053615806306285506, 0.11489101351346895, 0.1302098153152648, 0.08425340990987723, 0.03829700450448965, 0.09957221171167308, 0.10723161261257101, 0.0765940090089793, 0.12255041441436687, 0.09957221171167308, 0.09957221171167308, 0.0765940090089793, 0.09957221171167308, 0.030637603603591718, 0.09191281081077515, 0.09957221171167308};
	private static double[] openScale={0.17642288224027858, 0.07056915289611143, 0.035284576448055716, 0.011761525482685239, 0.0588076274134262, 0.1176152548268524, 0.023523050965370478, 0.16466135675759336, 0.19994593320564907, 0.1176152548268524, 0.1176152548268524, 0.08233067837879668, 0.12937678030953764, 0.1528998312749081, 0.10585372934416715, 0.07056915289611143, 0.1528998312749081, 0.08233067837879668, 0.18818440772296383, 0.17642288224027858, 0.12937678030953764, 0.18818440772296383, 0.08233067837879668, 0.035284576448055716, 0.14113830579222286, 0.035284576448055716, 0.08233067837879668, 0.19994593320564907, 0.19994593320564907, 0.10585372934416715};
	private static double[] highScale={0.014004101366088678, 0.15404511502697546, 0.22406562185741885, 0.028008202732177356, 0.07002050683044339, 0.09802870956262075, 0.08402460819653207, 0.15404511502697546, 0.11203281092870943, 0.08402460819653207, 0.1260369122947981, 0.22406562185741885, 0.22406562185741885, 0.11203281092870943, 0.042012304098266035, 0.042012304098266035, 0.16804921639306414, 0.18205331775915282, 0.23806972322350753, 0.08402460819653207, 0.042012304098266035, 0.23806972322350753, 0.22406562185741885, 0.014004101366088678, 0.14004101366088678, 0.05601640546435471, 0.05601640546435471, 0.15404511502697546, 0.21006152049133017, 0.1960574191252415};
	private static double[] lowScale={0.07757581369963422, 0.05541129549973872, 0.02216451819989549, 0.05541129549973872, 0.08865807279958196, 0.04432903639979098, 0.13298710919937295, 0.08865807279958196, 0.0997403318995297, 0.17731614559916392, 0.1219048500994252, 0.0997403318995297, 0.03324677729984324, 0.07757581369963422, 0.15515162739926844, 0.17731614559916392, 0.0997403318995297, 0.06649355459968648, 0.08865807279958196, 0.03324677729984324, 0.11082259099947744, 0.08865807279958196, 0.17731614559916392, 0.07757581369963422, 0.02216451819989549, 0.13298710919937295, 0.04432903639979098, 0.011082259099947749, 0.06649355459968648, 0.16623388649921617};
	private static double[] volScale={6.508694143464129, 4.881520607598096, 4.881520607598096, 4.067933839665081, 5.695107375531113, 6.101900759497621, 6.915487527430637, 0.40679338396650805, 4.067933839665081, 6.915487527430637, 6.508694143464129, 0.8135867679330161, 3.6611404556985723, 4.067933839665081, 3.2543470717320644, 4.881520607598096, 4.4747272236315885, 1.6271735358660322, 6.915487527430637, 5.695107375531113, 6.508694143464129, 3.2543470717320644, 3.6611404556985723, 5.288313991564604, 4.881520607598096, 1.6271735358660322, 1.220380151899524, 3.2543470717320644, 5.288313991564604, 4.067933839665081};
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	public S_31() {
		this(myStatisticType);
	}
	
	public S_31(String myStatisticType) {
		super(myStatisticType, PREVIOUS, GAIN, 
				closeOffset, volOffset, openOffset, highOffset, lowOffset,
				closeScale, volScale, openScale, highScale, lowScale);
	}
	
//	@Override
//	public String getStartDate(){
//		return START_DATE;
//	}


}
