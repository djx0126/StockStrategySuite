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
public class StestAggregate2 extends AbstractStrategyStatisticData {

	private static String myStatisticType = Constant.StestAggregate2;

	public StestAggregate2() {
		super(myStatisticType);
	}


	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
									DataMap dataMap) throws Exception {
		DataArray close = dataMap.getDataArray(Constant.CLOSE);
		DataArray statisticArray = new DataArray(stockCode, myStatisticType, dataMap);

		DataArray refDataArray = dataMap.getDataArray("pending.gain5b.Aggregated");


		String indexStockCode = this.getStockCode001(statisticArray.getStockCode());
		DataMap indexDataMap = SharedStockDataHolder.getInstance().get(indexStockCode);
		DataArray indexDif = indexDataMap.getDataArray(Constant.MACDDIF);

		for (int i = 0; i < close.size(); i++) {
			statisticArray.addData(new RawData(close.getDate(i), 0));
		}

		for (int i = 0; i< close.size(); i++) {
			if (refDataArray.getValue(i) < 0) {
				statisticArray.setValue(i, refDataArray.getValue(i));
			}

			if (refDataArray.getValue(i) > 0 && indexDif.getValue(i) > 0 ) {
				statisticArray.setValue(i, refDataArray.getValue(i));
			} else if (refDataArray.getValue(i) > 0) {
				statisticArray.setValue(i, refDataArray.getValue(i) / 2.0d);
			}
		}

		return statisticArray;
	}

}