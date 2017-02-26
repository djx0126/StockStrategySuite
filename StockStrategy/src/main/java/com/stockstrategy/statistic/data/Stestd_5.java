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
public class Stestd_5 extends AbstractStrategyStatisticData {

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

	//5555.455(avgGain=7.348%,sum=1440.301,lost=143.767,rate=11.018,count=196,accuracy=87.245) time:35s offset:close3:U
//**********  end run with >>>>>>>> 1552.739(avgGain=5.331%,sum=559.788,lost=158.340,rate=4.535,count=105,accuracy=80.000), end at Sat Jun 20 14:15:57 CST 2015 passed 549s
	private static double[] closeOffset={-0.013058544865236582, -0.0015409637050519277, 0.016725801590730296, 0.050568884536225475, -0.030937230214537353, 0.023131984510865553, 0.005560824781219532, 0.03747686673381805, 0.0029364174199756313, 0.004144627738595629};
	private static double[] openOffset={-0.006160308704818398, 0.017155693006281673, 0.020632867883118137, -0.027993967529487306, 0.02632893872234915, -0.025494671558687076, 0.019938329942187576, 0.005868933664097946, 0.0, 0.0029644789637390144};
	private static double[] highOffset={-0.0137398114143286, 0.00860017176191757, 0.016135023953874294, -0.012543672864323865, 0.035028649900688776, 0.029229617659250886, 0.0011008738008861376, 0.008908917153819198, 0.01429669852033736, 0.0011643290671059145};
	private static double[] lowOffset={-0.016086819893454805, 0.0031700467895066017, 0.020632867883118137, 0.019912206404631493, -0.03131463298728604, 0.014839362818431201, 0.004669878133485216, 0.007697308871938416, 0.0029972132893987375, 0.007278848850563823};
	private static double[] volOffset={-0.03455510249606454, 0.0031778971361325686, 0.02125095815940219, -0.12390032456784555, -0.009313648262474692, 0.05080924082618151, 0.041048315081414474, 0.024658992719546864, 0.11510158544895399, -0.036027498489694706};
	private static double[] closeScale={0.06896725933232892, 0.07663028814703213, 0.08429331696173535, 0.007663028814703213, 0.09961937459114177, 0.06896725933232892, 0.12260846103525141, 0.015326057629406427, 0.030652115258812854, 0.1149454322205482};
	private static double[] openScale={0.09423392524342267, 0.18846785048684533, 0.15313012852056185, 0.09423392524342267, 0.05889620327713917, 0.011779240655427833, 0.16490936917598967, 0.16490936917598967, 0.08245468458799483, 0.16490936917598967};
	private static double[] highScale={0.20007947083248803, 0.24625165640921604, 0.046172185576728, 0.046172185576728, 0.030781457051152004, 0.21547019935806402, 0.138516556730184, 0.24625165640921604, 0.030781457051152004, 0.092344371153456};
	private static double[] lowScale={0.13042646175437422, 0.07825587705262455, 0.05217058470174969, 0.07825587705262455, 0.013042646175437423, 0.13042646175437422, 0.1565117541052491, 0.09129852322806196, 0.14346910792981166, 0.1565117541052491};
	private static double[] volScale={4.565927379572925, 4.150843072339024, 2.905590150637316, 0.8301686144678047, 5.811180301274632, 0.8301686144678047, 7.05643322297634, 1.6603372289356093, 5.811180301274632, 7.05643322297634};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	public Stestd_5() {
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
