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
	private static String myStatisticType = Constant.Stesta;
	private static String START_DATE = "20140301";

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
						if (i - lastGoldenCross <60 && i - lastGoldenCross > 12
						) {
							double difValue = dif.getValue(i) / Math.abs(atr.getValue(i));
							double dif2Value = dif.getValue(lastGoldenCross) / Math.abs(atr.getValue(lastGoldenCross));
							double sizeRate = Math.log10(greenSize1/greenSize2);

							if (difValue > -1.2739033092340826 && difValue< -0.6533809556695691
									&& maxDif > -1.7100025125926361 && maxDif < 0.2089873177343693
									&& dif2Value> -2.4746844249873945 && dif2Value < -1.693687577222569
									&& sizeRate> -1.2466607691767195 && sizeRate < -0.15379618937533834) {
								tobuy = true;
							}

//PartMacdData{avgGainByDay=8.897555009590619, count=63.0, lDif=-5.239209043625603, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.897555009590619, count=63.0, lDif=-5.239209043625603, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=2.446783185111885}
//PartMacdData{avgGainByDay=8.897555009590619, count=63.0, lDif=-5.239209043625603, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=4.5040231107119295}
//PartMacdData{avgGainByDay=8.897555009590619, count=63.0, lDif=-5.239209043625603, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=4.5040231107119295}
//PartMacdData{avgGainByDay=8.861042360629762, count=81.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=1.8089614666908231, lDif2=-3.724946591688249, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.861042360629762, count=81.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=3.393061989897874, lDif2=-3.724946591688249, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.861042360629762, count=81.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=4.977162513104925, lDif2=-3.724946591688249, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.861042360629762, count=81.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=4.977162513104925, lDif2=-3.724946591688249, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.738683513219295, count=81.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=1.8089614666908231, lDif2=-1.6676966660882044, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.738683513219295, count=81.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=3.393061989897874, lDif2=-1.6676966660882044, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.738683513219295, count=81.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=4.977162513104925, lDif2=-1.6676966660882044, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.738683513219295, count=81.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=4.977162513104925, lDif2=-1.6676966660882044, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.721521173072679, count=62.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.721521173072679, count=62.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=2.446783185111885}
//PartMacdData{avgGainByDay=8.721521173072679, count=62.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=4.5040231107119295}
//PartMacdData{avgGainByDay=8.721521173072679, count=62.0, lDif=-2.5552696779619106, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=4.5040231107119295}
//PartMacdData{avgGainByDay=8.518935647480381, count=62.0, lDif=-3.897234360793757, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=0.3895432595118402}
//PartMacdData{avgGainByDay=8.518935647480381, count=62.0, lDif=-3.897234360793757, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=2.446783185111885}
//PartMacdData{avgGainByDay=8.518935647480381, count=62.0, lDif=-3.897234360793757, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=4.5040231107119295}
//PartMacdData{avgGainByDay=8.518935647480381, count=62.0, lDif=-3.897234360793757, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-3.724946591688249, rDif2=4.5040231107119295}
//PartMacdData{avgGainByDay=8.07410171491971, count=62.0, lDif=-5.239209043625603, rDif=-1.2133049951300645, lMaxDif=-1.3592495797232786, rMaxDif=0.22486094348377228, lDif2=-1.6676966660882044, rDif2=0.3895432595118402}
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