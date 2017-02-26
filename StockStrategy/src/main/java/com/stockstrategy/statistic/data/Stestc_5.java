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
public class Stestc_5 extends AbstractStrategyStatisticData {

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

//6843.227(avgGain=7.186%,sum=2443.352,lost=174.965,rate=14.965,count=340,accuracy=87.647) time:34s offset:low9:U2//6902.158(avgGain=7.203%,sum=2456.350,lost=174.965,rate=15.039,count=341,accuracy=87.683) time:34s scale:high7:U2

	//**********  end run with >>>>>>>> 2600.815(avgGain=5.927%,sum=924.642,lost=116.930,rate=8.908,count=156,accuracy=85.256), end at Sat Jun 20 12:09:54 CST 2015 passed 530s
	private static double[] closeOffset={0.001039870985373649, 0.04116217179278373, -0.009143379439869771, 0.05304882189285706, -0.008126513588411542, 0.002659858832904372, -0.009964008158151185, 0.009981646546514962, 0.004862089763251743, 0.008715231898212009};
	private static double[] openOffset={-0.0111627317373158, -0.01088298971773876, 0.014523367125856562, 0.004409118905055112, 0.0014797100580477636, -0.009819122591535263, -0.010451632531541724, 0.010741548751856258, 0.004665175245279056, -0.003939536844378564};
	private static double[] highOffset={0.026708332393949742, -0.009435340104310574, -0.0026786629817656857, 0.015159660624203392, 5.825547243759472E-4, -0.023675741059661897, -0.011358463216185251, 5.609228831461798E-4, 1.403439927880574E-4, 0.013399054643686329};
	private static double[] lowOffset={-0.001645340111903393, -0.011200614218328707, 0.0027731887730247373, 0.005748424258571283, 0.004024724534835857, -0.01799978943553896, -0.006353119831866362, -0.004523181803124635, 0.004669878133485216, -0.043747207110968744};
	private static double[] volOffset={-0.09727109002203996, -0.08622142780682883, -0.16638989089662704, 0.41087803378019755, 0.03525439446846847, -0.05444904967929915, -0.19420554061775033, 0.05559350063078716, -0.12756372874294716, 0.31639189981891164};
	private static double[] closeScale={0.09961937459114177, 0.007663028814703213, 0.007663028814703213, 0.015326057629406427, 0.007663028814703213, 0.030652115258812854, 0.12260846103525141, 0.038315144073516064, 0.07663028814703213, 0.04597817288821928};
	private static double[] openScale={0.11779240655427833, 0.15313012852056185, 0.16490936917598967, 0.1766886098314175, 0.070675443932567, 0.011779240655427833, 0.08245468458799483, 0.20024709114227318, 0.023558481310855667, 0.18846785048684533};
	private static double[] highScale={0.046172185576728, 0.07695364262788001, 0.23086092788364004, 0.015390728525576002, 0.20007947083248803, 0.030781457051152004, 0.16929801378133602, 0.24625165640921604, 0.21547019935806402, 0.20007947083248803};
	private static double[] lowScale={0.1565117541052491, 0.20868233880699877, 0.10434116940349938, 0.20868233880699877, 0.19563969263156133, 0.013042646175437423, 0.03912793852631227, 0.1173838155789368, 0.06521323087718711, 0.14346910792981166};
	private static double[] volScale={3.735758765105121, 3.3206744578712186, 3.3206744578712186, 7.05643322297634, 5.3960959940407305, 2.075421536169512, 7.05643322297634, 4.150843072339024, 4.981011686806828, 2.490505843403414};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public Stestc_5() {
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
