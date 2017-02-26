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
public class Stestd extends AbstractStrategyStatisticData {

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

//2241106.261(avgGain=12.702%,sum=1740.169,lost=52.064,rate=34.424,count=137,accuracy=93.431) time:81s offset:open8:U2//2268214.872(avgGain=12.771%,sum=1736.823,lost=52.064,rate=34.360,count=136,accuracy=93.382) time:83s offset:close3:U2

	//**********  end run with >>>>>>>> 1237812.167(avgGain=11.755%,sum=728.828,lost=7.869,rate=93.614,count=62,accuracy=96.774), end at Fri Jun 26 02:20:52 CST 2015 passed 1445s
	private static double[] closeOffset={0.011664832336486394, 0.03525062263189955, 0.005542575452556391, -0.005351791849429218, -0.005705546007952051, -0.006961712897275361, 0.0010637959560291371, -0.0044969596465899726, 0.03796524341847666, -0.017043951388895695};
	private static double[] openOffset={0.007825337511956621, 0.004231892017509617, 0.013222785642626378, -0.037096337776717496, -0.0034883278458212987, -0.0010677189936161985, 0.01436487115497285, 0.0041533100911190345, 0.010975865519875557, -0.0058224385804055675};
	private static double[] highOffset={0.010310145486177901, -0.005185667365018837, 0.011491324154264237, 0.0061300637768397104, -0.012696479794515282, -0.004924908136588661, 0.013879750789515087, 0.006434110005409903, -0.005901559991525414, -0.00818202804743665};
	private static double[] lowOffset={0.007700929365391359, -2.678350266222486E-4, 0.014151197465903487, 0.02203275655858692, 0.017459094683403082, -0.005395031886706139, 0.011958801389094444, 0.005825819790680221, 0.0070389625452574826, -0.05017348436303032};
	private static double[] volOffset={0.2589675402290014, -0.1955040840454431, 0.048980942245526245, -0.02520380843893249, 0.29375642095259125, -0.3600997751214731, 0.05825219687008356, 0.04654007076132973, -0.2858428228307285, 0.2749165456233675};
	private static double[] closeScale={0.06130423051762571, 0.007663028814703213, 0.09195634577643856, 0.12260846103525141, 0.12260846103525141, 0.08429331696173535, 0.0536412017029225, 0.038315144073516064, 0.007663028814703213, 0.08429331696173535};
	private static double[] openScale={0.10112861798325813, 0.16433400422279446, 0.050564308991629066, 0.012641077247907266, 0.20225723596651626, 0.20225723596651626, 0.20225723596651626, 0.12641077247907268, 0.0758464634874436, 0.18961615871860898};
	private static double[] highScale={0.12603085134292352, 0.18904627701438528, 0.20480013343225073, 0.20480013343225073, 0.25206170268584704, 0.12603085134292352, 0.2363078462679816, 0.06301542567146176, 0.15753856417865442, 0.07876928208932721};
	private static double[] lowScale={0.14916287985177265, 0.04068078541411981, 0.08136157082823962, 0.04068078541411981, 0.027120523609413208, 0.10848209443765283, 0.14916287985177265, 0.12204235624235943, 0.06780130902353301, 0.013560261804706604};
	private static double[] volScale={4.981011686806828, 6.226264608508535, 5.811180301274632, 4.981011686806828, 4.565927379572925, 2.905590150637316, 4.981011686806828, 4.565927379572925, 0.4150843072339023, 6.226264608508535};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	public Stestd() {
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
		DataArray statisticArray = null;
		DataArray open = null;
		DataArray close = null;
		DataArray high = null;
		DataArray low = null;
		DataArray vol = null;
		if (!dataMap.containArray(Constant.MA5)
				|| !dataMap.containArray(Constant.MA10)) {
			return null;
		}
		try {
			open = dataMap.getDataArray(Constant.OPEN);
			close = dataMap.getDataArray(Constant.CLOSE);
			high = dataMap.getDataArray(Constant.HIGH);
			low = dataMap.getDataArray(Constant.LOW);
			vol = dataMap.getDataArray(Constant.VOL);

			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			
			DataArray SHClose = SharedStockDataHolder.getInstance().get(Constant.SH000001).getDataArray(Constant.CLOSE);

			for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
			}

			// buy: 1.cross ma5 ma10
			// * 2.ma10 < ma30
			// * 3.VolMa20 < VolMa60

			for (int i = 0; i < close.size(); i++) {
				statisticArray.setValue(i, 0);

				boolean tobuy = false;

				if (i < PREVIOUS) {
					continue;
				}
				
				int datesForBigChange = 2;
				boolean hasBigChange = false;
				for (int k=0;k<datesForBigChange && !hasBigChange;k++){
					if (Math.abs(close.getValue(i-k) - open.getValue(i-k)) > open
							.getValue(i-k) * 0.1f
							|| Math.abs(close.getValue(i-k) - close.getValue(i-k - 1)) > close
									.getValue(i-k - 1) * 0.10) {
						hasBigChange = true;
					}
				}

				if (hasBigChange) {
					continue;
				}
				
				
				String today = close.getDate(i);
				int idxTodayOfSH = SHClose.getIndexByDate(today);
				int datesToCompare = 5;
				int fromDateIdx = idxTodayOfSH-datesToCompare;
				if (!close.getDate(i-datesToCompare).equals(SHClose.getDate(fromDateIdx))){
					continue;
				}
				
				
				double[] closeList = new double[PREVIOUS];
				double[] volList = new double[PREVIOUS];
				double[] closeOpenList = new double[PREVIOUS];
				double[] openList = new double[PREVIOUS];
				double[] highList = new double[PREVIOUS];
				double[] lowList = new double[PREVIOUS];

				for (int j = i, k = 0; j > 0 && k < PREVIOUS; j--, k++) {
					double c = close.getValue(j) / close.getValue(j - 1);
					double v = vol.getValue(j) / vol.getValue(j - 1);
					double day = close.getValue(j) / open.getValue(j);
					double o = open.getValue(j) / open.getValue(j - 1);
					double h = high.getValue(j) / high.getValue(j - 1);
					double l = low.getValue(j) / low.getValue(j - 1);

					closeList[k] = Math.log10(c);
					volList[k] = Math.log10(v);
					closeOpenList[k] = Math.log10(day);
					openList[k] = Math.log10(o);
					highList[k] = Math.log10(h);
					lowList[k] = Math.log10(l);
					// System.out.println(" " + offset + " price:" + price +
					// ", vol=" + v + ", closeOpen" + day);

				}
				double value = 0;
				for (int j = 0; j < PREVIOUS; j++) {
					double temp = closeList[j] + closeOffset[j];
					value += temp * temp / closeScale[j] / closeScale[j];
					temp = openList[j] + openOffset[j];
					value += temp * temp / openScale[j] / openScale[j];
					temp = highList[j] + highOffset[j];
					value += temp * temp / highScale[j] / highScale[j];
					temp = lowList[j] + lowOffset[j];
					value += temp * temp / lowScale[j] / lowScale[j];
					temp = volList[j] + volOffset[j];
					value += temp * temp / volScale[j] / volScale[j];
					
				}
				if (value <= LIMIT) {
					tobuy = true;
				}

				if (tobuy) {
					statisticArray.setValue(i, 1);
				}

			}
			// sell: cross ma5 ma10
			int gainLeft = -1;
			for (int i = 0; i < close.size(); i++) {
				if (gainLeft > 0) {
					gainLeft--;
				}
				if (statisticArray.getValue(i) > 0) {
					gainLeft = GAIN;
				}
				if (gainLeft == 0) {
					gainLeft = -1;
					statisticArray.setValue(i, -1);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return statisticArray;
	}

}
