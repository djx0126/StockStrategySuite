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
public class Stesta extends AbstractStrategyStatisticData {

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

//5250.271(avgGain=1.162%,sum=4094.522,lost=9521.872,rate=1.430,count=3524,accuracy=56.385) time:86s offset:close9:U2//122873.548(avgGain=4.627%,sum=1415.819,lost=586.827,rate=3.413,count=306,accuracy=70.915) time:80s offset:close9:U2
//834879.244(avgGain=9.091%,sum=1590.913,lost=170.480,rate=10.332,count=175,accuracy=84.000) time:85s offset:high3:U2//1780123.210(avgGain=13.552%,sum=772.454,lost=8.089,rate=96.499,count=57,accuracy=94.737) time:85s offset:close9:U

	//**********  end run with >>>>>>>> 719208.968(avgGain=12.434%,sum=174.070,lost=0.000,rate=1740704.580,count=14,accuracy=100.000), end at Thu Jun 25 23:52:59 CST 2015 passed 604s
	private static double[] closeOffset={-0.010737821094208608, 0.007886238413999071, -0.0017020053732910417, -0.02046068479947027, -0.004299980492724041, 0.002682494519104788, 0.004326755408078997, 0.0032735919963875863, 0.01561175216398614, 0.05306912503609099};
	private static double[] openOffset={0.010737821094208639, 0.0022948242662921077, -0.025143035362755355, -0.010723865391773064, 0.010723865391773066, 0.0027228583354732975, 0.0032901254470500095, 0.015122031869819783, 0.02401970662017145, -0.02855546045305287};
	private static double[] highOffset={0.0, 0.008931820866926024, -0.016599261819461704, 0.02405056613141384, 0.0015840545776749622, 0.006931796344854665, -0.005872540985832293, -0.026693876068111178, 0.01603560894098665, -0.0378093689507683};
	private static double[] lowOffset={0.004607942240353462, 0.0058295436607237955, -0.014429715422641414, -0.019428551838325956, 0.007115254632879052, 0.0011050756138120167, 0.0016629036854044954, 0.011251781720247517, 0.0300853706098342, -0.022612216969700144};
	private static double[] volOffset={0.04206712208386167, -0.013613363732694603, -0.05282713088514148, -0.05939844940645431, -0.05106044206291711, 0.13860890056884048, -0.21125632088651125, 0.013810622136537967, 0.09711956461421725, 0.1172024797062126};
	private static double[] closeScale={0.04597817288821928, 0.09195634577643856, 0.12260846103525141, 0.107282403405845, 0.038315144073516064, 0.0536412017029225, 0.12260846103525141, 0.04597817288821928, 0.08429331696173535, 0.007663028814703213};
	private static double[] openScale={0.1137696952311654, 0.16433400422279446, 0.06320538623953634, 0.13905184972697993, 0.1516929269748872, 0.17697508147070173, 0.0379232317437218, 0.08848754073535087, 0.13905184972697993, 0.012641077247907266};
	private static double[] highScale={0.09452313850719264, 0.14178470776078897, 0.07876928208932721, 0.01575385641786544, 0.22055398985011615, 0.2363078462679816, 0.03150771283573088, 0.01575385641786544, 0.03150771283573088, 0.01575385641786544};
	private static double[] lowScale={0.18984366526589247, 0.12204235624235943, 0.06780130902353301, 0.12204235624235943, 0.13560261804706603, 0.10848209443765283, 0.09492183263294623, 0.13560261804706603, 0.17628340346118584, 0.14916287985177265};
	private static double[] volScale={1.6603372289356093, 5.811180301274632, 2.905590150637316, 2.075421536169512, 5.3960959940407305, 2.075421536169512, 4.981011686806828, 3.3206744578712186, 6.641348915742437, 3.3206744578712186};
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	
	
	public Stesta() {
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
									.getValue(i-k - 1) * 0.1) {
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
