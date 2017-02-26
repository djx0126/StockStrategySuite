package com.stockstrategy.statistic.data;

import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public abstract class AbstractMA extends AbstractStatisticData {

	protected static final int MA = 10;
	//protected IPainter painter = null;
	//protected IDataTransformer mDataTransformer = null;
	
	
	
	public AbstractMA(String type ){super(type);}
	
	
	
	public DataArray actualGenerate(String stockCode, String statisticType, int ma, 
			DataMap dataMap, String baseStatistic)//  baseStatistic = Constant.CLOSE
	{
		DataArray statictisticArray = null;
		try {
			DataArray baseArray = dataMap.getDataArray(baseStatistic);
			statictisticArray = new DataArray(stockCode, statisticType, dataMap);
			for (int i = 0; i < baseArray.size() ;i++)
			{
				double tempMAValue = 0f;
				String tempDate = "";
				
				if (i<ma-1)
				{
					for (int j = 0 ; j <=i ;j++)
					{
						tempMAValue=0f;
					}
				}else
				{
					for (int j = i-ma+1; j <= i ; j++)
					{
						tempMAValue += baseArray.getValue(j);
					}
				}
				tempDate = baseArray.getDate(i);
				statictisticArray.addRawData(tempDate, tempMAValue/ma);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//setTransformer(new TransformerPrice());
		//setPainter(new linePainter());
		return statictisticArray;
	}

	
	
}
