/**
 * 
 */
package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import android.graphics.Paint;

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
public class SLevel3 extends AbstractStrategyStatisticData {

	/* (non-Javadoc)
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String, java.lang.String, com.stock.data.DataMap)
	 * 
	 * buy: 1.cross ma5 ma10
	 * 		2.Low < ma30
	 * 		3.VolMa10 < VolMa60
	 * 
	 * sell:1.cross ma5 ma10
	 */
	private static final String myStatisticType = Constant.SLevel3;
	private static float arg1 = 1.05f;
	private static float arg2 = 0.86f;
	private static float arg3 = 1.1f;
	private static float arg4 = 0.5f;
	private static int arg5 = 6;
	private static float arg6 = 2.0f;
	private static float arg7 = 1.8f;
	private static float arg8 = 1.015f;
	private static float arg9 = 0.075f;
	
	public SLevel3() {
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
				
				boolean tobuy = true;
				///////  Negative case
				if (ma5.getValue(i-1)<=0 || ma10.getValue(i-1)<=0 || ma5.getValue(i)<=0 || ma10.getValue(i)<=0){continue;}
				if (volma5.getValue(i)<=0 || volma10.getValue(i)<=0 || volma20.getValue(i)<=0 || volma30.getValue(i)<=0 || volma60.getValue(i)<=0 || volma120.getValue(i)<=0){continue;}
				
				boolean has1030DcrossBF = false;
				for (int j=i-1;j>=i-11;j--){
					if (ma10.getValue(j-1)>ma30.getValue(j-1) && ma10.getValue(j)<=ma30.getValue(j)){
						has1030DcrossBF = true;
						break;
					}
				}
				if (has1030DcrossBF){
					continue;
				}
				
				boolean hasma30topBF=false;
				for (int j=i-3;j>i-25;j--){
					if (ma30.getValue(j)>ma30.getValue(j-1) && ma30.getValue(j)>ma30.getValue(j+1)){
						if (ma30.getValue(j)>ma30.getValue(j-3) && ma30.getValue(j)>ma30.getValue(j+3)){
							hasma30topBF=true;
							break;
						}
					}
				}
				if(hasma30topBF){
					if(ma30.getValue(i)<ma30.getValue(i-3)){
						if(ma30.getValue(i-3)-ma30.getValue(i)>0.015*ma30.getValue(i)){
							continue;
						}
					}
				}
				
				boolean hasbigdownBF=false;
				for (int j=i;j>=i-10;j--){
					double abs = Math.abs(close.getValue(j)-open.getValue(j));
					double avg = Math.min(close.getValue(j), open.getValue(j))+abs/2;
					if (close.getValue(j)<open.getValue(j) && abs>=0.065f*avg){
						hasbigdownBF=true;
						break;
					}
				}
				boolean hasbigupBF=false;
				for (int j=i;j>=i-10;j--){
					double abs = Math.abs(close.getValue(j)-open.getValue(j));
					double avg = Math.min(close.getValue(j), open.getValue(j))+abs/2;
					if (close.getValue(j)>open.getValue(j) && abs>=0.075f*avg){
						hasbigupBF=true;
						break;
					}
				}
				if (hasbigdownBF && hasbigupBF){
					continue;
				}
				
				
				if ( close.getValue(i)>open.getValue(i) && close.getValue(i)<=1.004f*open.getValue(i) && vol.getValue(i)>3.5f*volma5.getValue(i-1) ){
					continue;
				}
				
				if ( open.getValue(i)==close.getValue(i) && vol.getValue(i)>1.25f*volma5.getValue(i-1) ){
					continue;
				}
				
				if( dif.getValue(i)<dea.getValue(i)  ){
					
					if( macd.getValue(i)<macd.getValue(i-4)   ){
						if( ma120.getValue(i)> ma120.getValue(i-5)  || ma60.getValue(i)> ma120.getValue(i) || ma30.getValue(i)>ma120.getValue(i)  ){
							continue;
						}
					}
					
					if ( ma120.getValue(i)< ma120.getValue(i-10) && ma120.getValue(i-10)>= 1.18f*ma120.getValue(i) ){
						continue;
					}
					
				}
				
				
				if(  macd.getValue(i)<0.0f && dea.getValue(i)>0.2f && dif.getValue(i)>0.3f){
					continue;
				}
				
				
				if(ma10.getValue(i)<0.97f*ma10.getValue(i-7) && 
						dea.getValue(i)<dea.getValue(i-7)-0.3f && 
						macd.getValue(i)<-0.25f){
					continue;
				}
				
				if(dea.getValue(i)>0 && macd.getValue(i)<-0.4f){
					continue;
				}
				
				if(close.getValue(i)>=1.205f*open.getValue(i-5)){
					continue;
				}
				
				if(close.getValue(i)>=1.075f*open.getValue(i)){
					continue;
				}
				
				
				boolean has3060DcrossBF = false;
				for (int j=i-1;j>=i-5;j--){
					if (ma30.getValue(j-1)>ma60.getValue(j-1) && ma30.getValue(j)<=ma60.getValue(j)){
						has3060DcrossBF = true;
						break;
					}
				}
				if (has3060DcrossBF){
					continue;
				}
				
				if (ma30.getValue(i-10)-ma30.getValue(i)>=arg9*ma30.getValue(i)){continue;}
				
				if (ma10.getValue(i-8)-ma10.getValue(i)>=0.075f*ma10.getValue(i-8)){
					if (ma120.getValue(i)-ma120.getValue(i-8)>=0.025f*ma120.getValue(i-8)){
						continue;
					}
				}
				
				if ((ma30.getValue(i-30)+ma30.getValue(i))/2-ma30.getValue(i-15)>0.05f*ma30.getValue(i)){
					if(ma60.getValue(i)>ma60.getValue(i-15) && ma120.getValue(i)>ma120.getValue(i-15) &&
						ma60.getValue(i)>ma120.getValue(i)){
						continue;
					}
				}
				
				if ((ma60.getValue(i-30)+ma60.getValue(i))/2-ma60.getValue(i-15)>0.03f*ma60.getValue(i)){
					if(ma120.getValue(i)>ma120.getValue(i-15) && ma250.getValue(i-15) > 0 && ma250.getValue(i)>ma250.getValue(i-15) &&
						ma120.getValue(i)>ma250.getValue(i)){
						continue;
					}
				}
				
				double max10120diff = 0;
				boolean inUpChannel = true;
				for (int j=i;j>i-30;j-=5){
					inUpChannel = inUpChannel && ma10.getValue(j)>ma120.getValue(j);
					inUpChannel = inUpChannel && ma120.getValue(j)>ma120.getValue(j-5);
					if ( inUpChannel && ma10.getValue(j)-ma120.getValue(j)>max10120diff){
						max10120diff = ma10.getValue(j)-ma120.getValue(j);
					}
				}
				if (inUpChannel && max10120diff>2f*(ma10.getValue(i)-ma120.getValue(i))){
					continue;
				}
				
				double avg = (open.getValue(i)+close.getValue(i))/2;
				if (ma120.getValue(i)<0.90f*avg){
					if(ma10.getValue(i)>ma30.getValue(i) && ma30.getValue(i)>ma60.getValue(i) && ma60.getValue(i)>ma120.getValue(i)){
						if (avg>1.025f*ma30.getValue(i)){
							if (ma60.getValue(i)-ma120.getValue(i)>ma60.getValue(i-10)-ma120.getValue(i-10)&&
								ma60.getValue(i-10)-ma120.getValue(i-10)>ma60.getValue(i-20)-ma120.getValue(i-20) &&
								ma30.getValue(i-10) > ma60.getValue(i-10) && ma60.getValue(i-10) > ma120.getValue(i-10)){
								continue;
							}
						}
					}
				}
				
				boolean longTermDown = ma30.getValue(i-2)>ma30.getValue(i-1);
				longTermDown = longTermDown && ma60.getValue(i-2)>ma60.getValue(i-1);
				longTermDown = longTermDown && ma120.getValue(i-2)>ma120.getValue(i-1);
				longTermDown = longTermDown && ma5.getValue(i-1)<=ma10.getValue(i-1);
				longTermDown = longTermDown && ma10.getValue(i-1)<=ma30.getValue(i-1);
				longTermDown = longTermDown && ma30.getValue(i-1)<=ma60.getValue(i-1);
				longTermDown = longTermDown && ma60.getValue(i-1)<=ma120.getValue(i-1);
				if (longTermDown &&  ma5.getValue(i-1)>ma5.getValue(i)){continue;}
				
				//volma5 > volma120
				//if (volma5.getValue(i)>volma120.getValue(i) && volma5.getValue(i)>volma60.getValue(i) && volma10.getValue(i)>volma60.getValue(i) && volma10.getValue(i)>volma120.getValue(i)){continue;}
				// vol60 as max , vol60-vol120 < vol120-vol10 continue
				//if ((volma60.getValue(i)>=volma5.getValue(i) && volma60.getValue(i)>=volma10.getValue(i) && volma60.getValue(i)>=volma20.getValue(i) && volma60.getValue(i)>=volma30.getValue(i) && volma60.getValue(i)>=volma120.getValue(i) )&&
				//		volma60.getValue(i)-volma120.getValue(i) <= Math.abs(volma120.getValue(i)-volma10.getValue(i))){continue;}
				// vol120 as max , vol120-vol60 < vol60-vol10 continue
				if ((volma120.getValue(i)>=volma5.getValue(i) && volma120.getValue(i)>=volma10.getValue(i) && volma120.getValue(i)>=volma20.getValue(i) && volma120.getValue(i)>=volma30.getValue(i) && volma120.getValue(i)>=volma60.getValue(i) )){
					// save a case that 120 > 60 >30
					if (!(volma120.getValue(i)>volma60.getValue(i) && volma60.getValue(i)>volma30.getValue(i))){
						if (volma120.getValue(i)-volma60.getValue(i) <= Math.abs(volma60.getValue(i)-volma10.getValue(i))){
							continue;
						}
					}
					if (macd.getValue(i)<=-0.05f && dif.getValue(i)>=0.1f){
						continue;
					}
				}
				
				
				
				boolean hasHugeVolBefore = false;
				int firstHugeVol = 0;
				for (int j=i-3;j<=i;j++){
					boolean hasHugeVol = vol.getValue(j)>volma10.getValue(j)*arg6;
					hasHugeVol = hasHugeVol && vol.getValue(j)>volma60.getValue(j)*arg6;
					hasHugeVol = hasHugeVol && vol.getValue(j)>volma120.getValue(j)*arg6;
					if (hasHugeVol){
						hasHugeVolBefore = true;
						firstHugeVol=j;
						break;
					}
				}
				if (hasHugeVolBefore && close.getValue(i)/close.getValue(firstHugeVol-1) <= arg8){continue;}
				//if (hasHugeVolBefore && ma30.getValue(i)<= ma30.getValue(i-arg5)){continue;}
				
				
				// no down cross point in 6 days(arg5)
				boolean hasDownCross = false;
				int noDownCrossDayBefore = arg5;
				for (int j = 1; j<=noDownCrossDayBefore;j++){
					if (ma5.getValue(i-1-j)>ma10.getValue(i-1-j) && ma5.getValue(i-j)<ma10.getValue(i-j)){
						hasDownCross = true;
						break;
					}
				}
				//boolean ma51030up = ma30.getValue(i) >=ma30.getValue(i-1) && ma30.getValue(i) >= ma30.getValue(i-2) &&
				boolean ma51030up = ma5.getValue(i)>=ma5.getValue(i-1);
				ma51030up = ma51030up && (ma30.getValue(i-5)<1.015f*ma30.getValue(i) ) ;
				if ( ma60.getValue(i)>ma60.getValue(i-5)){
					ma51030up = ma51030up && (ma10.getValue(i)>1.04f*ma60.getValue(i) ) ;
				}else{
					ma51030up = ma51030up && (ma10.getValue(i)>0.72f*ma120.getValue(i) ) ;
				}
				boolean ma51030up2 = (dif.getValue(i)>=dif.getValue(i-30)-0.4f && ma120.getValue(i)>ma120.getValue(i-5)) ;
				ma51030up2 = ma51030up2 && ma10.getValue(i)<1.00f*ma10.getValue(i-2);
				ma51030up = ma51030up2 ||ma51030up;//ma51030up = ma51030up && (volma5.getValue(i)<1.01f*volma120.getValue(i));
				if (hasDownCross){
					if (!ma51030up){
						continue;
					}
				}
				//boolean volDown = volma20.getValue(i)<volma30.getValue(i) && volma30.getValue(i)<volma60.getValue(i) && volma60.getValue(i)<volma120.getValue(i);
				//volDown = volDown && volma20.getValue(i-2) < volma20.getValue(i);
				//volDown = volDown && ma10.getValue(i)<ma10.getValue(i-2) && ma10.getValue(i-2)< ma10.getValue(i-4) &&  ma10.getValue(i-4)< ma10.getValue(i-6);
				//if (hasDownCross && volDown){continue;}
				
				boolean difdeadcrsBF = false;
				for ( int j=i-1 ;j>i-9;j--){
					if ( dif.getValue(j-1)>=dea.getValue(j-1) && dif.getValue(j)<dea.getValue(j) ){
						difdeadcrsBF = true;
						break;
					}
				}
				
				boolean difdeaupcrs = false;
				//difdeaupcrs = dif.getValue(i-1)<=dea.getValue(i-1) && dif.getValue(i)>dea.getValue(i);
				//difdeaupcrs = difdeaupcrs && !difdeadcrsBF;
				//difdeaupcrs = difdeaupcrs && dif.getValue(i)<=0.14f;
				
				
				
			////positive case
				// ma5 ma10 cross
				tobuy = tobuy && (ma5.getValue(i-1)<=ma10.getValue(i-1) && ma5.getValue(i)>ma10.getValue(i)  ||  difdeaupcrs) ;
				// ma5 and ma 30
				tobuy = tobuy && ma5.getValue(i) < arg1*ma30.getValue(i);
				tobuy = tobuy && ma5.getValue(i) >= arg2*ma30.getValue(i);
				//volma20 and volma60
				tobuy = tobuy && volma20.getValue(i) <= arg3*volma30.getValue(i);
				tobuy = tobuy && volma20.getValue(i) > arg4*volma30.getValue(i);
				
				
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

}
