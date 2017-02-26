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
public class Stesta_5 extends AbstractStrategyStatisticData {

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

	//**********  end run with >>>>>>>> 2479898.030(avgGain=14.226%,sum=1166.530,lost=28.763,rate=41.557,count=82,accuracy=95.122), end at Fri Jun 26 04:47:06 CST 2015 passed 1942s
	private static double[] closeOffset={0.008854715765630987, -0.03889333094638889, 0.041498237347808505, 0.03238315978089674, -0.007597181750521507, -6.271401550489037E-4, -6.26235843048227E-4, -0.03384231396910856, 0.004702740032450421, -0.026475677817894012};
	private static double[] openOffset={-0.020387839045786015, -0.022789398213035644, 0.04653901764201731, 0.0015672860095255307, -0.005305855568927435, 0.003425789940622393, 0.00534813436688268, -0.014015738876727498, -0.021269319230111355, -0.002409406911648205};
	private static double[] highOffset={0.0477979599087936, -0.020530178467017348, 0.041498237347808505, 0.034109413419131956, -0.008396932546069307, 6.164578492865119E-4, 0.0, -0.003073576040954326, -0.020050222806183418, -0.0484081571410989};
	private static double[] lowOffset={-0.008289444223186988, -0.017247580197210823, 0.058757578830461246, 0.0016162820363591211, -0.011505162405324723, 0.033071524012622595, -0.031491695126431817, 0.0, -0.014322119476314833, 6.129774932975554E-4};
	private static double[] volOffset={0.8074951500247731, -0.6762662805811215, 0.464833539077433, 1.0323375749948456, 0.1496589045733713, -0.24149692746072782, -0.04940721988292291, 1.0839664892596963, -0.20098318865746784, -0.16465967575803866};
	private static double[] closeScale={0.1149454322205482, 0.038315144073516064, 0.06896725933232892, 0.038315144073516064, 0.107282403405845, 0.13027148984995462, 0.12260846103525141, 0.007663028814703213, 0.06896725933232892, 0.08429331696173535};
	private static double[] openScale={0.10112861798325813, 0.1137696952311654, 0.16433400422279446, 0.11376969523116541, 0.06320538623953634, 0.17697508147070173, 0.1516929269748872, 0.2275393904623308, 0.21489831321442354, 0.18961615871860898};
	private static double[] highScale={0.01575385641786544, 0.20480013343225073, 0.09452313850719264, 0.22055398985011615, 0.25206170268584704, 0.12603085134292352, 0.22055398985011615, 0.12603085134292352, 0.20480013343225073, 0.07876928208932721};
	private static double[] lowScale={0.04068078541411981, 0.21696418887530566, 0.20340392707059907, 0.09492183263294623, 0.20340392707059907, 0.013560261804706604, 0.027120523609413208, 0.14916287985177265, 0.16272314165647925, 0.14916287985177265};
	private static double[] volScale={1.6603372289356093, 0.4150843072339023, 1.6603372289356093, 5.811180301274632, 6.226264608508535, 1.6603372289356093, 0.8301686144678047, 1.245252921701707, 1.6603372289356093, 4.150843072339024};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	public Stesta_5() {
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
