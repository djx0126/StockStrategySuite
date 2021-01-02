/**
 *
 */
package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;

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
	private static String myStatisticType = Constant.Stesta_5;
	private static String START_DATE = "20140301";

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
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray open = dataMap.getDataArray(Constant.OPEN);
			DataArray macd = dataMap.getDataArray(Constant.MACD);
			DataArray dif = dataMap.getDataArray(Constant.MACDDIF);
			DataArray atr = dataMap.getDataArray(Constant.ATR);
			DataArray ma5 = dataMap.getDataArray(Constant.MA5);
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			int start = 0;
			int count = 0;
			for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
			}

			for (int i = 0; i < close.size(); i++) {

				if (i < 70) {
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
					for (int j = i-1; j > 2; j--) {
						if (macd.getValue(j) > 0.01) {
							if (j <= i-10) {
								lastDeathCrossK = j+1;
							}
							break;
						}
						greenSize1 += Math.abs(macd.getValue(j)) / Math.abs(atr.getValue(j));
					}
					if (lastDeathCrossK < 0) {
						continue;
					}


					boolean minorDeath = false;
					for (int j = i -1; j > 2 && j>i-10 ; j--) {
						if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
							minorDeath = true;
							break;
						}
					}
					if (minorDeath) {
						continue;
					}

					int lastGoldenCross = -1;
					double maxDif = -100;
					double greenSize2 = 0.0d;
					for (int j = lastDeathCrossK -1; j > 2; j--) {
						double dif1 = dif.getValue(j)/ Math.abs(atr.getValue(j));
						if (dif1 > maxDif) {
							maxDif = dif1;
						}
						if (macd.getValue(j) < -0.01) {
							if (j <= lastDeathCrossK - 10 ) {
								lastGoldenCross = j+1;
							}
							break;
						}
						greenSize2 += Math.abs(macd.getValue(j)) / Math.abs(atr.getValue(j));
					}
					if (lastGoldenCross < 0) {
						continue;
					}

					if (lastDeathCrossK > 0 && lastGoldenCross > 0 && !minorDeath ) {
						if (i - lastGoldenCross <90 && i - lastGoldenCross > 12
						) {
							double difValue = dif.getValue(i) / Math.abs(atr.getValue(i));
							double dif2Value = dif.getValue(lastGoldenCross) / Math.abs(atr.getValue(lastGoldenCross));
							double sizeRate = Math.log10(close.getValue(i)/ma5.getValue(i-60));

							if (difValue > -1.2739033092340826 && difValue< -0.6533809556695691
									&& maxDif > -1.7100025125926361 && maxDif < 0.2089873177343693
									&& dif2Value> -2.4746844249873945 && dif2Value < -1.693687577222569
									&& sizeRate> -1.2466607691767195 && sizeRate < -0.8823659092429258) {
								tobuy = true;
							}

//PartMacdData{avgGainByDay=7.372844728467382, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=0.41205572935647883}
//PartMacdData{avgGainByDay=7.372844728467382, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=7.372844728467382, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=7.372844728467382, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=0.41205572935647883}
//PartMacdData{avgGainByDay=7.372844728467382, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=7.372844728467382, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=7.301363181914841, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-3.724946591688249, rDif2=0.41205572935647883}
//PartMacdData{avgGainByDay=7.301363181914841, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-3.724946591688249, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=7.301363181914841, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-3.724946591688249, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=7.301363181914841, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-3.724946591688249, rDif2=0.41205572935647883}
//PartMacdData{avgGainByDay=7.301363181914841, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-3.724946591688249, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=7.301363181914841, count=60.0, lDif=-2.449940613958794, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-3.724946591688249, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=6.973459574257666, count=61.0, lDif=-3.3451741653236162, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=0.41205572935647883}
//PartMacdData{avgGainByDay=6.973459574257666, count=61.0, lDif=-3.3451741653236162, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=6.973459574257666, count=61.0, lDif=-3.3451741653236162, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=6.973459574257666, count=61.0, lDif=-3.3451741653236162, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=0.41205572935647883}
//PartMacdData{avgGainByDay=6.973459574257666, count=61.0, lDif=-3.3451741653236162, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=6.973459574257666, count=61.0, lDif=-3.3451741653236162, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=6.874579846431575, count=55.0, lDif=-1.5547170625939712, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=0.41205572935647883}
//PartMacdData{avgGainByDay=6.874579846431575, count=55.0, lDif=-1.5547170625939712, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=6.874579846431575, count=55.0, lDif=-1.5547170625939712, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//PartMacdData{avgGainByDay=6.874579846431575, count=55.0, lDif=-1.5547170625939712, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=0.41205572935647883}
//PartMacdData{avgGainByDay=6.874579846431575, count=55.0, lDif=-1.5547170625939712, rDif=0.23573004013567367, lMaxDif=-1.4927701256563914, rMaxDif=-0.20692867213835342, lDif2=-2.3459391513400067, rDif2=1.7910531697047212}
//Par
//
// PartMacdData{avgGainByDay=9.438608694166591, count=40.0, lDif=-1.7428312835085196, rDif=-1.2347269684097628, lMaxDif=-0.4562094932009244, rMaxDif=3.7902124796714505, lDif2=-0.5957755056112934, rDif2=0.2146127596956493}
//PartMacdData{avgGainByDay=9.438608694166591, count=40.0, lDif=-1.7428312835085196, rDif=-1.2347269684097628, lMaxDif=-0.4562094932009244, rMaxDif=4.49794947515018, lDif2=-0.5957755056112934, rDif2=0.2146127596956493}
						}
					}
				}

				if (tobuy){
					statisticArray.setValue(i, 1);
				}

			}
			//sell: cross ma5 ma10
			for (int i = 1 ; i<close.size();i++)
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