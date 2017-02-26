package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import com.stock.data.TransformerPrice;
//import com.stock.view.painter.LinePainter;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

public class MA60 extends AbstractMA {
	private static final String myStatisticType = Constant.MA60;
	private static final int MA = 60;

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray staticArray = null;
		staticArray = actualGenerate(stockCode, statisticType, MA,
				dataMap, Constant.CLOSE);
		
		if (staticArray != null)
		{
			//setTransformer(new TransformerPrice());
			//Paint paintParam = new Paint();
			//paintParam.setAntiAlias(true);
			//paintParam.setARGB(200,255, 0, 128);
			//setPainter(new LinePainter(paintParam));
		}
		
		return staticArray;
		
	}
	
	public MA60() {
		super(myStatisticType);
	}
}
