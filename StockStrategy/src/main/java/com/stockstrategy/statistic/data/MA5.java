package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import com.stock.data.TransformerPrice;
//import com.stock.view.painter.LinePainter;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

/**
 * @author Administrator
 * Factory class for MA5
 */
public class MA5 extends AbstractMA{
	private static final String myStatisticType = Constant.MA5;
	private static final int MA = 5;

	
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
			//paintParam.setARGB(200, 255, 255, 255);
			//setPainter(new LinePainter(paintParam));
			
		}
		return staticArray;
	}
	
	public MA5() {
		super(myStatisticType);
	}

}
