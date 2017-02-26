package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import com.stock.data.TransformerVol;
//import com.stock.view.painter.LinePainter;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class MAVOL10 extends AbstractMA {
	private static final String myStatisticType = Constant.MAVOL10;
	private static final int MA = 10;
	
	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray staticArray = null;
		staticArray = actualGenerate(stockCode, statisticType, MA,
				dataMap, Constant.VOL);
		
		if (staticArray != null)
		{
			//setTransformer(new TransformerVol());
			//Paint paintParam = new Paint();
			//paintParam.setAntiAlias(true);
			//paintParam.setARGB(200, 255, 255, 0);
			//setPainter(new LinePainter(paintParam));
		}
		
		
		return staticArray;
	}

	public MAVOL10() {
		super(myStatisticType);
	}
}
