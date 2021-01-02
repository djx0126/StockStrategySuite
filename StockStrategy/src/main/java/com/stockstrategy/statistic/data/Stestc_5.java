/**
 *
 */
package com.stockstrategy.statistic.data;

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
	private static String myStatisticType = Constant.Stestc_5;
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
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray open = dataMap.getDataArray(Constant.OPEN);
			DataArray macd = dataMap.getDataArray(Constant.MACD);
			DataArray dif = dataMap.getDataArray(Constant.MACDDIF);
			DataArray atr = dataMap.getDataArray(Constant.ATR);
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

				if (macd.getValue(i) > 0 && macd.getValue(i - 1) < 0  ) {
					int lastDeathCrossK = -1;
					double greenSize1 = 0.0d;
					for (int j = i - 1; j > 2; j--) {
						if (macd.getValue(j) < 0) {
							greenSize1 += macd.getValue(j) / atr.getValue(j);
							if ( j<=i-10 && macd.getValue(j - 1) > 0) {
								lastDeathCrossK = j;
								break;
							}
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

					double greenSize2 = 0.0d;
					for (int j= lastGoldenCross-1; j>2;j--) {
						if (macd.getValue(j) < 0) {
							greenSize2+= macd.getValue(j) / atr.getValue(j);
							if (macd.getValue(j) < 0 && macd.getValue(j-1) > 0) {
								break;
							}
						}
					}

					if (lastDeathCrossK > 0 && lastGoldenCross > 0 && !minorDeath  && greenSize2 < greenSize1) {
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
					statisticArray.setValue(i, 1);
				}

			}
			//sell: cross ma5 ma10
			for (int i = 1; i<close.size();i++)
			{
				if (macd.getValue(i) < 0 && macd.getValue(i - 1) > 0) {
					statisticArray.setValue(i, -1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statisticArray;
	}

}