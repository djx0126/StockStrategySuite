/**
 * 
 */
package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Arrays;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;

/**
 * @author Administrator
 *
 *when ma5 > ma10 , tigger buy , set value to 1; when ma5 < ma10 , tigger sell , set value to -1
 *
 *
 *
 */
public class SBreak extends AbstractStrategyStatisticData {

	/* (non-Javadoc)
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String, java.lang.String, com.stock.data.DataMap)
	 * 
	 * buy: 1.cross ma5 ma10
	 * 		2.Low < ma30
	 * 		3.VolMa10 < VolMa60
	 * 
	 * sell:1.cross ma5 ma10
	 */
	private static final String myStatisticType = Constant.SBreak;
	private static float arg1 = 1.05f;
	private static float arg2 = 0.86f;
	private static float arg3 = 1.1f;
	private static float arg4 = 0.5f;
	private static int arg5 = 6;
	private static float arg6 = 2.0f;
	private static float arg7 = 1.8f;
	private static float arg8 = 1.015f;
	private static float arg9 = 0.075f;
	
	public SBreak() {
		super(myStatisticType);
	}

	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray statisticArray = null;
		DataArray open = null;
		DataArray close = null;
		DataArray high = null;
		DataArray vol = null;
		DataArray ma5 = null;
		DataArray ma10 = null;
		DataArray low = null;
		DataArray ma30 = null;
		DataArray ma60 = null;
		DataArray ma120 = null;
		DataArray ma250 = null;
		DataArray volma5 = null;
		DataArray volma10 = null;
		DataArray volma20 = null;
		DataArray volma30 = null;
		DataArray volma60 = null;
		DataArray volma120 = null;
		DataArray lvol = null;
		DataArray dif = null;
		DataArray dea = null;
		DataArray macd = null;
		if (!dataMap.containArray(Constant.MA5) || !dataMap.containArray(Constant.MA10))
		{
			return null;
		}
		try {
			open = dataMap.getDataArray(Constant.OPEN);
			close = dataMap.getDataArray(Constant.CLOSE);
			high = dataMap.getDataArray(Constant.HIGH);
			vol = dataMap.getDataArray(Constant.VOL);
			ma5 = dataMap.getDataArray(Constant.MA5);
			ma10 = dataMap.getDataArray(Constant.MA10);
			low = dataMap.getDataArray(Constant.LOW);
			ma30 = dataMap.getDataArray(Constant.MA30);
			ma60 = dataMap.getDataArray(Constant.MA60);
			ma120 = dataMap.getDataArray(Constant.MA120);
			ma250 = dataMap.getDataArray(Constant.MA250);
			volma5 = dataMap.getDataArray(Constant.MAVOL5);
			volma10 = dataMap.getDataArray(Constant.MAVOL10);
			volma20 = dataMap.getDataArray(Constant.MAVOL20);
			volma30 = dataMap.getDataArray(Constant.MAVOL30);
			volma60 = dataMap.getDataArray(Constant.MAVOL60);
			volma120 = dataMap.getDataArray(Constant.MAVOL120);
			lvol = dataMap.getDataArray(Constant.LargeVol);
			dif = dataMap.getDataArray(Constant.MACDDIF);
			dea = dataMap.getDataArray(Constant.MACDDEA);
			macd = dataMap.getDataArray(Constant.MACD);
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			
			for (int i = 0; i<ma5.size();i++)
			{
				RawData data = new RawData(ma5.getDate(i),0);
				statisticArray.addData(data);
			}
			//buy: 1.cross ma5 ma10
			// * 		2.ma10 < ma30
			// * 		3.VolMa20 < VolMa60
			for (int i = 1 ; i<ma5.size();i++)
			{
				
				boolean tobuy = false;
				///////  Negative case
				
				//if (ma5.getValue(i-1)<=0 || ma10.getValue(i-1)<=0 || ma5.getValue(i)<=0 || ma10.getValue(i)<=0){continue;}
				//if (volma5.getValue(i)<=0 || volma10.getValue(i)<=0 || volma20.getValue(i)<=0 || volma30.getValue(i)<=0 || volma60.getValue(i)<=0 ){continue;}
				
				int days = 20;
				if (i<days+1){continue;}
				double min60 = close.getValue(i);
				double max60 = close.getValue(i);
				boolean toSkip=false;
				for (int j=i;j>=i-days;j--){
					if (close.getValue(i)<0.87*close.getValue(j) ){
						toSkip=true;
					}
					if(low.getValue(j)<min60){
						min60=low.getValue(j);
					}
					if (high.getValue(j)>max60){
						max60=high.getValue(j);
					}
				}
				if (toSkip){
					continue;
				}
				//System.out.println(Arrays.toString(priceVolDistr));
				int slots=18;
				double step=(max60-min60)/slots;
				double priceVolDistr[]= new double[slots];
				
				for (int j=0;j<slots;j++){
					priceVolDistr[j]=0.0;
				}
				for (int j=i;j>=i-days;j--){
					priceVolDistr[getIdx(close.getValue(j),min60,step)]+= vol.getValue(j)*2/9;
					priceVolDistr[getIdx(open.getValue(j),min60,step)]+= vol.getValue(j)*2/9;
					priceVolDistr[getIdx((open.getValue(j)+close.getValue(j))/2,min60,step)]+= vol.getValue(j)*5/9;
				}
				int keyVolNum = 1;
				ArrayList<Double> keyPrices = getMostVolPrice( priceVolDistr, min60, step, keyVolNum);
				
				for (double keyPrice:keyPrices){
					if (close.getValue(i)>=keyPrice && close.getValue(i-1)<keyPrice){
						tobuy = true;
						break;
					}
				}
				
				
				if (tobuy){
					statisticArray.setValue(i, 1);
				}
				
			}
			//sell: cross ma5 ma10
			for (int i = 10 ; i<ma5.size();i++)
			{
				if (ma5.getValue(i-1)>0 && ma10.getValue(i-1)>0 && ma5.getValue(i)>0 && ma10.getValue(i)>0){
					if (ma5.getValue(i-1)>=ma10.getValue(i-1) && ma5.getValue(i)<ma10.getValue(i))
					{
						statisticArray.setValue(i, -1);
					}
					if (vol.getValue(i)>volma10.getValue(i)*arg7 && vol.getValue(i)>volma60.getValue(i)*arg7 &&vol.getValue(i)>volma120.getValue(i)*arg7 &&
							ma5.getValue(i)>ma10.getValue(i) && ma60.getValue(i)>ma120.getValue(i)
					) {
						statisticArray.setValue(i, -1);
					}
					if(open.getValue(i)>close.getValue(i) && open.getValue(i-1)>close.getValue(i-1) && open.getValue(i)<close.getValue(i-1)){
						if(volma5.getValue(i)>3.3f*lvol.getValue(i)){
							statisticArray.setValue(i, -1);
						}
					}
					if(i>40){
						double diffma30 = ma30.getValue(i)-ma30.getValue(i-10);
						if(diffma30 > 0.09f*ma30.getValue(i)){
							if(volma10.getValue(i)>2.5f*lvol.getValue(i)){
								statisticArray.setValue(i, -1);
							}
						}
					}
					
					if(open.getValue(i)>close.getValue(i) && open.getValue(i-1)>close.getValue(i-1) && open.getValue(i-2)>close.getValue(i-2) && open.getValue(i-3)>close.getValue(i-3)){
						statisticArray.setValue(i, -1);
					}
					
					if(ma5.getValue(i)>ma10.getValue(i) && ma10.getValue(i)>ma30.getValue(i) && ma30.getValue(i)>ma60.getValue(i) && ma30.getValue(i)>ma120.getValue(i) &&
							ma30.getValue(i)>ma30.getValue(i-2) && ma60.getValue(i)>ma60.getValue(i-2) && ma120.getValue(i)>ma120.getValue(i-2)){
						if( (high.getValue(i)-low.getValue(i))>3.2f*Math.abs(open.getValue(i)-close.getValue(i)) && 
							(high.getValue(i-1)-low.getValue(i-1))>3.2f*Math.abs(open.getValue(i-1)-close.getValue(i-1))){
							statisticArray.setValue(i, -1);
						}
					}
					boolean hasHugeVolBefore = false;
					for (int j=i-arg5;j<=i;j++){
						boolean hasHugeVol = vol.getValue(j)>volma10.getValue(j)*arg6;
						hasHugeVol = hasHugeVol && vol.getValue(j)>volma60.getValue(j)*arg6;
						hasHugeVol = hasHugeVol && vol.getValue(j)>volma120.getValue(j)*arg6;
						if (hasHugeVol){
							hasHugeVolBefore = true;
							break;
						}
					}
					if (hasHugeVolBefore && ma30.getValue(i) >= 1.07f* ma30.getValue(i-arg5)){
						statisticArray.setValue(i, -1);
					}
					if (close.getValue(i)<0.92f*open.getValue(i)&& vol.getValue(i)>1.35f*volma30.getValue(i)){
						statisticArray.setValue(i, -1);
					}
					if(volma5.getValue(i-1)>volma5.getValue(i-2) && volma5.getValue(i-2)>volma5.getValue(i-3) && volma5.getValue(i-3)>volma5.getValue(i-4) && 
							volma10.getValue(i-1)>volma10.getValue(i-2) && volma10.getValue(i-2)>volma10.getValue(i-3) && volma10.getValue(i-3)>volma10.getValue(i-4) &&
						vol.getValue(i-1)>2f*volma60.getValue(i-1)	&& vol.getValue(i-1)>2f*volma120.getValue(i-1)  && 
						vol.getValue(i-2)>2f*volma60.getValue(i-2)	&& vol.getValue(i-2)>2f*volma120.getValue(i-2)){
						if(volma5.getValue(i)>1.5f*vol.getValue(i)   ){
							statisticArray.setValue(i, -1);
						}
					}
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return statisticArray;
	}

	/**
	 * @param price     the price to compute
	 * @param priceMin  the lower bound 
	 * @param step      the price step for each slot
	 * @return          the index for the slot to put
	 */
	private int getIdx(double price, double priceMin, double step){
		int tmpi =  (int) ((price-priceMin)*100);//to fix like Math.floor((11.48-6.77)/0.23550000000000004)
		int idx = (int) Math.floor((tmpi/100.0)/(step+0.0001));
		//if (idx>=slots){
		//	idx=slots-1;
		//}else if(idx<0){
		//	idx=0;
		//}
		return idx;
	}
	
	private ArrayList<Double> getMostVolPrice(double[] priceVolDistr, double lowerPrice, double step, int num){
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i=0;i<num;i++){
			result.add(lowerPrice+getMaxVolPriceAndRmv(priceVolDistr)*step);
		}
		return result;
	}
	
	private int getMaxVolPriceAndRmv(double[] priceVolDistr){
		double maxVol = 0;
		int maxPriceStep=0;
		for (int i=0;i<priceVolDistr.length;i++){
			if (priceVolDistr[i]>maxVol){
				maxVol=priceVolDistr[i];
				maxPriceStep=i;
			}
		}
		priceVolDistr[maxPriceStep]=0.0;
		return maxPriceStep;
	}
	
}
