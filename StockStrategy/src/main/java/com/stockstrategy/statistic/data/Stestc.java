/**
 * 
 */
package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import android.graphics.Paint;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;
import com.stockstrategy.data.SharedStockDataHolder;

/**
 * @author Administrator
 * 
 *         when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 ,
 *         tigger sell , set value to -1
 * 
 * 
 * 
 */
public class Stestc extends AbstractStrategyStatisticData {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
	 * java.lang.String, com.stock.data.DataMap)
	 */
	private int PREVIOUS = 10;
	private int GAIN = 5;
	private final double LIMIT = PREVIOUS;
	private static String myStatisticType = Constant.SPre30Gain5I;
	private static String START_DATE = "20140301";

	//751761.762(avgGain=8.513%,sum=1413.144,lost=29.393,rate=49.077,count=166,accuracy=94.578) time:79s scale:high2:D2
//**********  end run with >>>>>>>> 347436.995(avgGain=7.816%,sum=531.498,lost=44.067,rate=13.061,count=68,accuracy=88.235), end at Fri Jun 26 01:07:03 CST 2015 passed 1270s
	private static double[] closeOffset={-0.0362978802129053, 0.03146309851940797, -0.01281197575996566, -0.003771032219343624, 0.013344406230878021, 0.00584698485595943, 0.028612124252080786, -0.017084890664726488, -0.00512447812115909, -0.004434847251146842};
	private static double[] openOffset={0.0019845936221724184, -0.018179403689749497, -0.007564100908581343, 0.059903134713245654, 0.0032482909986910067, 0.06941437432494092, -0.011845129199631198, -0.01026445372460242, -0.0031585192448298056, -0.001884143874551366};
	private static double[] highOffset={0.0, -0.02286288295950418, 0.004352307539691383, -0.008661430592952645, 0.011797144445811222, 0.009545317906230456, 0.0038776550632018216, -0.013422972969432225, -0.0043836873607540445, 6.23538487447294E-4};
	private static double[] lowOffset={-0.003574460358298152, -0.036838244357250464, -0.010340833338218842, 0.009034683535935192, 0.0, 0.009893240602109124, 0.007401014584304738, -0.018596488491658448, -0.007093311804956036, -0.00445434142625001};
	private static double[] volOffset={0.08377245832523121, -0.12035371756775906, 0.16184845223663144, -0.2996716892713005, 0.08326467079867855, 0.1786923397960671, 0.15699452530028934, -0.12620432722041522, 0.5740562016554989, 0.1613446372456577};
	private static double[] closeScale={0.12260846103525141, 0.007663028814703213, 0.04597817288821928, 0.07663028814703213, 0.04597817288821928, 0.030652115258812854, 0.038315144073516064, 0.038315144073516064, 0.022989086444109637, 0.06130423051762571};
	private static double[] openScale={0.1516929269748872, 0.12641077247907268, 0.10112861798325813, 0.012641077247907266, 0.012641077247907266, 0.025282154495814533, 0.20225723596651626, 0.16433400422279446, 0.17697508147070173, 0.1516929269748872};
	private static double[] highScale={0.17329242059651984, 0.20480013343225073, 0.09452313850719264, 0.12603085134292352, 0.06301542567146176, 0.2363078462679816, 0.01575385641786544, 0.2363078462679816, 0.18904627701438528, 0.06301542567146176};
	private static double[] lowScale={0.16272314165647925, 0.21696418887530566, 0.14916287985177265, 0.13560261804706603, 0.04068078541411981, 0.16272314165647925, 0.08136157082823962, 0.12204235624235943, 0.23052445068001226, 0.20340392707059907};
	private static double[] volScale={3.3206744578712186, 0.4150843072339023, 3.735758765105121, 0.4150843072339023, 4.981011686806828, 6.226264608508535, 4.150843072339024, 7.05643322297634, 1.245252921701707, 5.811180301274632};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public Stestc() {
		super(myStatisticType);
	}
	

	
	
	public int getPrevious(){
		return this.PREVIOUS;
	}
	public int getGain(){
		return this.GAIN;
	}
	public double getLimit(){
		return this.LIMIT;
	}
	

	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
			DataMap dataMap) {
		return null;
	}

}
