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
public class Stestb extends AbstractStrategyStatisticData {

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

//638166.969(avgGain=7.256%,sum=2612.029,lost=235.087,rate=12.111,count=360,accuracy=88.056) time:80s offset:vol4:U//640326.811(avgGain=7.268%,sum=2609.246,lost=235.087,rate=12.099,count=359,accuracy=88.022) time:81s scale:open8:D

	//**********  end run with >>>>>>>> 277535.353(avgGain=6.537%,sum=875.983,lost=101.586,rate=9.623,count=134,accuracy=85.821), end at Fri Jun 26 00:15:38 CST 2015 passed 1356s
	private static double[] closeOffset={-0.006382532109198803, 0.05008658725863155, 0.016985747990650626, 0.006859064755606792, 0.0031539316674467307, -0.014842046960690124, 0.002558443227640828, 0.023053640616290977, 0.002034169910453142, 0.004097148141045005};
	private static double[] openOffset={0.011143886433839645, 0.016962051938036474, 0.004970520429710119, 0.05603698619709345, -0.01743872665834881, 0.056344129928865214, 0.028789087637898416, 0.002034169910453142, 0.004097148141045005, 0.03614504090908438};
	private static double[] highOffset={0.00455358805350064, 0.012185414072438835, 0.013750320021960893, 0.004886628004181187, 0.01246267673734894, -0.0050280675305634515, 0.006294233097688304, 0.01944927150064254, 0.009383270472965626, 0.0020373507976551336};
	private static double[] lowOffset={0.003013853052230529, 0.011026404228043454, 0.013868346147193435, 0.005803576099240261, 0.022444763670040273, -0.005240419322539097, 0.01998640158119848, 0.004110073091216192, 6.888097905526052E-4, 0.006948859955327825};
	private static double[] volOffset={-0.03173830824565552, 0.015255193385939865, 0.1665449052261479, -0.04360691241332759, 0.6092714015698711, -0.06294463605725627, -0.12216549187635518, 0.08973230288074331, 0.16026497013987126, -0.04994726833801434};
	private static double[] closeScale={0.12260846103525141, 0.015326057629406427, 0.04597817288821928, 0.07663028814703213, 0.04597817288821928, 0.030652115258812854, 0.04597817288821928, 0.038315144073516064, 0.038315144073516064, 0.06130423051762571};
	private static double[] openScale={0.1516929269748872, 0.12641077247907268, 0.10112861798325813, 0.012641077247907266, 0.012641077247907266, 0.025282154495814533, 0.20225723596651626, 0.16433400422279446, 0.16433400422279446, 0.1516929269748872};
	private static double[] highScale={0.17329242059651984, 0.20480013343225073, 0.12603085134292352, 0.12603085134292352, 0.06301542567146176, 0.2363078462679816, 0.01575385641786544, 0.2363078462679816, 0.18904627701438528, 0.03150771283573088};
	private static double[] lowScale={0.16272314165647925, 0.21696418887530566, 0.14916287985177265, 0.13560261804706603, 0.054241047218826416, 0.16272314165647925, 0.08136157082823962, 0.12204235624235943, 0.23052445068001226, 0.20340392707059907};
	private static double[] volScale={3.3206744578712186, 0.4150843072339023, 3.735758765105121, 0.4150843072339023, 4.981011686806828, 6.226264608508535, 4.150843072339024, 7.05643322297634, 1.245252921701707, 5.811180301274632};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	public Stestb() {
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
