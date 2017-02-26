/**
 * 
 */
package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import com.stock.data.TransformerVol;
//import com.stock.view.painter.LinePainter;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

/**
 * @author Administrator
 * a statistic data describe the large/largest Vol of a stock 
 */
public class LargeVol extends AbstractStatisticData {
	private final int DIV = 5; //divide the actual value by DIV, the default is one
	
	public LargeVol() {
		super(Constant.LargeVol);// the type name of a statistic
	}

	/* (non-Javadoc)
	 * @see com.stock.statistic.data.AbstractStatisticData#generate(java.lang.String, java.lang.String, com.stock.data.DataMap)
	 */
	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray largeVol = null;
		try {
			DataArray vol = dataMap.getDataArray(Constant.VOL);
			if (vol !=null){
				double currentMaxVol = 0f;
				double targetMaxVol = 0f;
				largeVol = new DataArray(stockCode, statisticType, dataMap);
				for (int i=0; i< vol.size();i++){
					if (vol.getValue(i)>targetMaxVol){
						targetMaxVol = vol.getValue(i);
					}
					if (targetMaxVol-currentMaxVol>10){
						currentMaxVol+=(targetMaxVol-currentMaxVol)/10;
					}
					largeVol.addRawData(vol.getDate(i), currentMaxVol/DIV);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (largeVol != null)
		{
			//setTransformer(new TransformerVol());
			//Paint paintParam = new Paint();
			//paintParam.setAntiAlias(true);
			//paintParam.setARGB(200, 20, 255, 20);
			//setPainter(new LinePainter(paintParam));
			
		}
		
		return largeVol;
	}

}
