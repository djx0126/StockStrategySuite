/**
 *
 */
package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;
import com.stockstrategy.data.SharedStockDataHolder;

import java.util.HashMap;
import java.util.Map;

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
	private static String myStatisticType = Constant.SMacd2018b;
	private static String START_DATE = "20140301";

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

		Map<String, BreakData> buyData = new HashMap<>();
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray open = dataMap.getDataArray(Constant.OPEN);
			DataArray high = dataMap.getDataArray(Constant.HIGH);
			DataArray macd = dataMap.getDataArray(Constant.MACD);
			DataArray dif = dataMap.getDataArray(Constant.MACDDIF);
			DataArray atr = dataMap.getDataArray(Constant.ATR);
			DataArray macdAtr = dataMap.getDataArray(Constant.MACD_ATR);
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			int start = 0;
			int count = 0;
			for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
			}

			for (int i = 0; i < close.size(); i++) {

				if (i < 10) {
					continue;
				}

				if (close.getValue(i) - close.getValue(i - 1) > 0.09 * close.getValue(i - 1)) {
					continue;
				}

				if (Math.abs(close.getValue(i) - open.getValue(i)) > open.getValue(i) * 0.1f
						|| Math.abs(close.getValue(i) - close.getValue(i - 1)) > close.getValue(i - 1) * 0.15) {
					start = i;
					count = 0;
					continue;
				}
				boolean tobuy = false;

				boolean macdGoldCross = macd.getValue(i) > 0 && macd.getValue(i - 1) <= 0;
				boolean macdAtrGoldCross = macdAtr.getValue(i) > 0 && macdAtr.getValue(i - 1) <= 0;
				if ( macdGoldCross && macdAtrGoldCross  ) {
					int lastDeathCrossK = -1;
					for (int j = i - 10; j > 2; j--) {
						if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
							lastDeathCrossK = j;
							break;
						}
					}

					boolean minorDeath = false;
					for (int j = i -1; j > 2 && j>i-10 ; j--) {
						if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
							minorDeath = true;
							break;
						}
					}

					int lastGoldenCross = -1;
					double maxDif = -100;
					for (int j = lastDeathCrossK - 10; lastDeathCrossK > 20 && j > 2; j--) {
						double dif1 = dif.getValue(j)/ Math.abs(atr.getValue(j));
						if (dif1 > maxDif) {
							maxDif = dif1;
						}

						if (macd.getValue(j) > 0 && macd.getValue(j - 1) < 0) {
							lastGoldenCross = j;
							break;
						}
					}

					boolean minorGoldenCross = false;
					for (int j = lastDeathCrossK -1; j > 2 && j>lastDeathCrossK-10 ; j--) {
						if (macd.getValue(j) > 0 && macd.getValue(j - 1) < 0) {
							minorGoldenCross = true;
							break;
						}
					}



					if (lastDeathCrossK > 0 && lastGoldenCross > 0 && !minorDeath && !minorGoldenCross) {
						if (i - lastGoldenCross <60 && i - lastGoldenCross > 12
						) {
							double difValue = dif.getValue(i) / Math.abs(atr.getValue(i));
							double dif2Value = dif.getValue(lastGoldenCross) / Math.abs(atr.getValue(lastGoldenCross));

							String indexStockCode = this.getStockCode001(statisticArray.getStockCode());
							DataMap indexDataMap = SharedStockDataHolder.getInstance().get(indexStockCode);
							DataArray indexMA60 = indexDataMap.getDataArray(Constant.MA200);

							DataArray indexClose = indexDataMap.getDataArray(Constant.CLOSE);

							int indexI = indexMA60.getIndexByDate(close.getDate(i));

							double maxDifRLimit = indexClose.getValue(indexI) > indexMA60.getValue(indexI) ? 0.00328021314758674 : -0.11328021314758674;

							if (difValue >-0.6083574973706583 && difValue< 0.10674762147488259
									&& maxDif > -2.1641223541356296 && maxDif < maxDifRLimit
									&& dif2Value> -0.443338738719806 && dif2Value < 0.579727965522391) {
								tobuy = true;
							}

//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-2.1641223541356296, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=0.579727965522391}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-2.1641223541356296, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=1.602794669764588}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-2.1641223541356296, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=2.625861374006785}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-2.1641223541356296, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=3.648928078248982}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-2.1641223541356296, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=3.648928078248982}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-1.4805016404729487, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=0.579727965522391}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-1.4805016404729487, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=1.602794669764588}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-1.4805016404729487, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=2.625861374006785}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-1.4805016404729487, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=3.648928078248982}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-1.4805016404729487, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=3.648928078248982}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-0.7968909268102677, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=0.579727965522391}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-0.7968909268102677, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=1.602794669764588}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-0.7968909268102677, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=2.625861374006785}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-0.7968909268102677, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=3.648928078248982}
//							PartMacdData{avgGainByDay=3.570060233952363, count=138.0, lDif=-0.6083574973706583, rDif=0.10674762147488259, lMaxDif=-0.7968909268102677, rMaxDif=-0.11328021314758674, lDif2=-0.443338738719806, rDif2=3.648928078248982}
						}
					}
				}

				if (tobuy){
					BreakData data = new BreakData();
					data.code = close.getStockCode();
					data.buyDate = close.getDate(i);
					data.buyPrice = close.getValue(i);
					data.buyDateIdx = i;
					buyData.put(data.buyDate, data);
				}

			}
			//sell: cross ma5 ma10

			boolean inhand = false;
			BreakData currentData = null;
			double sellPrice = 0.0;
			for (int i = 1; i<close.size();i++)
			{


				if (!inhand && buyData.containsKey(close.getDate(i))) {
					inhand = true;
					statisticArray.setValue(i, 1);
					currentData = buyData.get(close.getDate(i));
				} else if (inhand) {
					// test sell

					boolean macdDeadCross = macd.getValue(i) < 0 && macd.getValue(i - 1) >= 0;
					boolean macdAtrDeadCross = macdAtr.getValue(i) < 0 && macdAtr.getValue(i - 1) >= 0;

					boolean toSell= false;

					double peek = MAX(high, i-52, i-1);

					toSell = macdAtrDeadCross || macdDeadCross || close.getValue(i) < sellPrice || close.getValue(i) < peek * 0.8 ;

					if (toSell) {
						inhand = false;
						currentData.gain = 100 * (close.getValue(i) - currentData.buyPrice) / currentData.buyPrice;
						currentData.dates = i - currentData.buyDateIdx;
						currentData.sellDate = close.getDate(i);
						currentData = null;
						statisticArray.setValue(i, -1);
					}
				}

				if (inhand) {
					double sumTR = 0.0;
					for (int k = i -9; k< i; k++) {
						sumTR += atr.getValue(k);
//						double TR = Math.max(Math.max(high.getValue(k) - low.getValue(k), Math.abs(close.getValue(k-1) - high.getValue(k))) , Math.abs(close.getValue(k-1) - low.getValue(k)) );
//						sumTR += Math.abs(TR);
					}
					double atr_mid = sumTR / 9;
					double h1 = MAX(high, i-26, i-1);
					if (h1 - 3 * atr_mid > sellPrice) {
						sellPrice = h1 - 3 * atr_mid;
					}

					if (currentData.buyPrice - close.getValue(i) > currentData.maxLoss) {
						currentData.maxLoss = currentData.buyPrice - close.getValue(i);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statisticArray;
	}

	public static class BreakData {
		public String buyDate;
		public String sellDate;
		public String code;
		public double buyPrice;
		public double gain;
		public int buyDateIdx;
		public int dates;
		public double maxLoss;

		public int gcCount = 0;
		public int dcCount = 0;
		public double macd =0.0;
		public double minMacd = 0.0;
		public double maxMacd = 0.0;

		public int gcCountL = 0;
		public int dcCountL = 0;
		public double macdL =0.0;
		public double minMacdL = 0.0;
		public double maxMacdL = 0.0;

		public String getDate() {
			return buyDate;
		}

		public String getCode() {
			return code;
		}

		public double getGain() {
			return gain;
		}

		@Override
		public String toString() {
			return "BreakData{" +
					"" + buyDate  +
					"," + sellDate  +
					", " + code  +
					", " + gain +
					", " + dates +
					", " + maxLoss +
					", " + gcCount +
					", " + dcCount +
					", " + macd +
					", " + minMacd +
					", " + maxMacd +
					'}';
		}
	}

}