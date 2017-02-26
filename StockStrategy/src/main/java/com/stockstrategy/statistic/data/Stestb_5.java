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
public class Stestb_5 extends AbstractStrategyStatisticData {

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

	//11406.435(avgGain=8.981%,sum=2227.203,lost=98.110,rate=23.701,count=248,accuracy=95.161) time:33s offset:high3:U
//**********  end run with >>>>>>>> 5638.899(avgGain=8.195%,sum=860.444,lost=56.315,rate=16.279,count=105,accuracy=92.381), end at Sat Jun 20 07:56:49 CST 2015 passed 449s
	private static double[] closeOffset={0.03236224387028352, 0.0029240348431290716, -0.003154043401018291, 0.013783339402803423, -0.033561462582647625, -0.004734494070604925, -0.004253229185686658, 0.022450107974579282, 0.006775877457703452, -0.013034177481977584};
	private static double[] openOffset={0.0058605968573982176, -0.008913646210339656, 0.019602576911970336, -0.029453674905816345, -0.01346984500721191, -0.001079887374225803, -0.01010718649445053, 0.01291959871159108, 0.015708565262499242, 0.010851853157203356};
	private static double[] highOffset={0.04137176924761186, -0.009632002100907021, 0.006879906753605622, 0.007043033588791025, -0.02469560052214189, 0.010443177398868983, -0.005643514575516915, -0.009573560484123968, -0.006959636627046461, 0.032116161048459636};
	private static double[] lowOffset={0.012519246876647856, -0.006887496020477676, 0.010611287994370501, 0.003194530636728246, -0.03921569266711317, 0.005898827282082137, 0.007110538116369822, -0.0025235692533960227, -0.01816843170082716, 0.06465352392901194};
	private static double[] volOffset={0.33854567686723547, -0.009531272261846593, -0.09710060731406475, -0.18578165228797275, 0.14319789563631038, -0.0017546532528463015, -0.0902211521976863, 0.06541194180783107, -0.14524460221179522, 0.3718135221496854};
	private static double[] closeScale={0.007663028814703213, 0.06896725933232892, 0.09195634577643856, 0.04597817288821928, 0.09195634577643856, 0.107282403405845, 0.02298908644410964, 0.107282403405845, 0.1149454322205482, 0.015326057629406427};
	private static double[] openScale={0.18846785048684533, 0.047116962621711334, 0.0353377219662835, 0.08245468458799483, 0.1766886098314175, 0.16490936917598967, 0.12957164720970615, 0.18846785048684533, 0.20024709114227318, 0.070675443932567};
	private static double[] highScale={0.092344371153456, 0.20007947083248803, 0.261642384934792, 0.092344371153456, 0.21547019935806402, 0.184688742306912, 0.20007947083248803, 0.10773509967903201, 0.23086092788364004, 0.10773509967903201};
	private static double[] lowScale={0.05217058470174969, 0.20868233880699877, 0.026085292350874846, 0.2217249849824362, 0.02608529235087485, 0.03912793852631227, 0.1173838155789368, 0.03912793852631227, 0.07825587705262455, 0.013042646175437423};
	private static double[] volScale={5.811180301274632, 6.226264608508535, 7.05643322297634, 5.3960959940407305, 4.981011686806828, 1.2452529217017072, 4.565927379572925, 6.641348915742437, 4.150843072339024, 1.6603372289356093};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public Stestb_5() {
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
