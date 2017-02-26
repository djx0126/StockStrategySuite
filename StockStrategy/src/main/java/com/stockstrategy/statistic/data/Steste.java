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
public class Steste extends AbstractStrategyStatisticData {

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

	//1216927.782(avgGain=8.193%,sum=5284.402,lost=517.694,rate=11.208,count=645,accuracy=87.132) time:80s scale:close3:D
//**********  end run with >>>>>>>> 730157.232(avgGain=8.046%,sum=2035.531,lost=162.980,rate=13.489,count=253,accuracy=88.142), end at Fri Jun 26 03:47:35 CST 2015 passed 3425s
	private static double[] closeOffset={0.0013823708708119923, 0.036962132670459294, 0.013624199945017368, 0.005281700587864825, -0.006235146595187347, -0.0033206730576951058, -0.030808438644287235, 0.03809209799217778, -0.030470147270371187, 0.005640267352692757};
	private static double[] openOffset={0.004644905045763538, 0.008964571580936956, -0.0388821650523544, 0.006744501496086325, -0.0014200377182608478, 0.0076277610726054575, -0.0016348046348507658, 0.003719889825129321, 0.03587417974532994, -0.04461153035049356};
	private static double[] highOffset={9.181704940105002E-4, 0.002303954909564255, 0.011706759533250396, -0.0065946199271934266, 9.359798491447044E-4, -0.002336178209484306, -0.0037119412457173023, -0.0023039549095642147, 0.0041559597711577035, -0.0036961455599637317};
	private static double[] lowOffset={-9.349723350731204E-4, 0.041249677225295635, 0.005281700587864825, 0.0014516820711035286, -0.003379740651380506, 9.629593344428314E-4, -0.0028824960810322994, -0.007123633993534104, 0.018739507882006016, 4.743796014020706E-4};
	private static double[] volOffset={-0.001081217742951794, 0.003909563566721556, 0.2727565872091177, -0.3673686951185164, 0.35643345445001906, -0.11276235995400592, 0.4418195113095762, 0.11440808088024969, 0.0037113713581048077, -0.13441986524922006};
	private static double[] closeScale={0.06130423051762571, 0.007663028814703213, 0.09195634577643856, 0.1149454322205482, 0.12260846103525141, 0.09195634577643856, 0.01532605762940643, 0.045978172888219274, 0.007663028814703213, 0.08429331696173535};
	private static double[] openScale={0.10112861798325813, 0.16433400422279446, 0.012641077247907268, 0.050564308991629066, 0.20225723596651626, 0.20225723596651626, 0.20225723596651626, 0.12641077247907268, 0.05056430899162907, 0.18961615871860898};
	private static double[] highScale={0.12603085134292352, 0.18904627701438528, 0.20480013343225073, 0.20480013343225073, 0.25206170268584704, 0.12603085134292352, 0.2363078462679816, 0.03150771283573088, 0.15753856417865442, 0.07876928208932721};
	private static double[] lowScale={0.14916287985177265, 0.013560261804706604, 0.08136157082823962, 0.04068078541411981, 0.027120523609413208, 0.10848209443765283, 0.14916287985177265, 0.12204235624235943, 0.06780130902353301, 0.08136157082823962};
	private static double[] volScale={4.981011686806828, 6.226264608508535, 5.811180301274632, 4.981011686806828, 4.565927379572925, 2.905590150637316, 4.981011686806828, 4.565927379572925, 0.4150843072339023, 6.226264608508535};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	public Steste() {
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
