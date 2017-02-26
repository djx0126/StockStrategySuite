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
public class Steste_5 extends AbstractStrategyStatisticData {

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

	//3947.984(avgGain=6.082%,sum=1873.200,lost=33.656,rate=56.657,count=308,accuracy=95.455) time:33s scale:low6:U
//**********  end run with >>>>>>>> 1493.097(avgGain=5.139%,sum=621.847,lost=39.253,rate=16.842,count=121,accuracy=94.215), end at Sat Jun 20 15:35:27 CST 2015 passed 564s
	private static double[] closeOffset={-0.01989754198062466, 0.004713996437400715, -0.01872471088021536, 0.00134717279610735, 0.0076567122013918515, 0.005180512503780314, -0.005180512503780329, 0.005701978824470203, 0.018238496401665014, 0.004179594201894812};
	private static double[] openOffset={-0.00339643433329933, -0.02068873982890445, -0.0035508019953024187, 0.006959724379251514, 0.006551609086648311, -0.027716131552645108, 0.0, 0.030584087646018606, 0.009421074025565164, -0.0037929890171390676};
	private static double[] highOffset={0.04119742387804981, -0.006839424530305515, -0.0067333826589683786, -0.004321373782642578, 0.01275454131950536, -8.507240367763901E-4, -0.0028800597814529053, 0.008698118575738519, 0.036084860785546305, -0.005579877380836604};
	private static double[] lowOffset={0.010686826930115518, -0.005442721813557216, -0.01563913430717506, -0.03583034930718722, 0.015449452173531556, -0.003559810766220274, 0.007149041789664057, -0.044533705171367996, 0.012732362458678804, 0.003679663107870687};
	private static double[] volOffset={0.16173448802695456, -0.2119841253017907, -0.08937442717515061, -0.015852454954273454, 0.14381827257445143, -0.1787806136025776, -0.2652131337129334, -0.019855156999630925, 0.41018104147414103, 0.22082085646828595};
	private static double[] closeScale={0.030652115258812854, 0.0536412017029225, 0.02298908644410964, 0.09195634577643856, 0.13027148984995462, 0.09961937459114177, 0.007663028814703213, 0.07663028814703213, 0.06896725933232892, 0.107282403405845};
	private static double[] openScale={0.11779240655427833, 0.047116962621711334, 0.11779240655427833, 0.05889620327713917, 0.05889620327713917, 0.011779240655427833, 0.15313012852056185, 0.1060131658988505, 0.18846785048684533, 0.05889620327713917};
	private static double[] highScale={0.015390728525576002, 0.138516556730184, 0.030781457051152004, 0.24625165640921604, 0.21547019935806402, 0.10773509967903201, 0.046172185576728, 0.184688742306912, 0.015390728525576002, 0.092344371153456};
	private static double[] lowScale={0.18259704645612393, 0.13042646175437422, 0.06521323087718711, 0.09129852322806196, 0.13042646175437422, 0.07825587705262455, 0.07825587705262453, 0.013042646175437423, 0.026085292350874846, 0.18259704645612393};
	private static double[] volScale={3.3206744578712186, 5.3960959940407305, 3.3206744578712186, 2.905590150637316, 4.150843072339024, 6.641348915742437, 6.226264608508535, 0.4150843072339023, 4.565927379572925, 3.3206744578712186};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public Steste_5() {
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
