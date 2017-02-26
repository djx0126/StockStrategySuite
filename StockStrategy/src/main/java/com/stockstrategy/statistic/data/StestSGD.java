/**
 * 
 */
package com.stockstrategy.statistic.data;

//import android.graphics.Paint;

//import android.graphics.Paint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class StestSGD extends AbstractStrategyStatisticData {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.stock.statistic.data.IStatisticData#generate(java.lang.String,
	 * java.lang.String, com.stock.data.DataMap)
	 */
	private int PREVIOUS = 30;
	private int GAIN = 5;
	private final double LIMIT = PREVIOUS;
	private static String myStatisticType = Constant.SPre30Gain5I;
	private static final int DELAY = 0;

	private double[] closeOffset= {-13.91528935995652, -39.84509937384741, -45.619825468669184, -55.88184451778844, -57.01337828693051, -46.4907692378553, -34.16457756098195, -35.98142822989085, -29.610665440021414, -21.012983104828965, -11.265254455968645, -1.6003988670482097, -4.753083095993926, -16.048399984826936, -28.470737592208348, -36.80591446769712, -49.4260557679747, -55.37124690053977, -57.13480806414561, -61.374812949313565, -61.11684817203115, -59.20187346133823, -61.11668152134827, -52.431637789130924, -38.904174142571236, -18.623977630091545, -4.661457916020998, 2.177914416686295, 9.256187930584884, 9.337972345241653};
	private double[] openOffset= {0.921166476956051, -3.7948024620988976, -5.156232441638442, -4.09109100887936, -10.727161065764824, -24.79040187928082, -30.536706488231033, -36.339542919770075, -31.525271558357325, -13.414242173008041, 1.090334617920679, 8.828683463997189, 9.5308210133578, 2.2293539654688495, -15.528112119255479, -26.538402782626964, -30.52683749280368, -32.28588867763733, -32.27739659449902, -37.62207845736875, -43.63180995491164, -41.934969835380755, -40.72865848391034, -34.72413547322719, -23.16663693005824, -5.330460664515903, -6.051512275909743, -11.592286724735438, -14.145161301824642, -6.624002282035162};
	private double[] highOffset= {6.03272987355134, 9.321865690090172, 8.446997466711542, 12.202150373175769, 10.6867239741942, 1.5653027857018884, -3.488431149156647, 0.04743885590537022, -0.35242250085152865, -6.079516100182068, -5.813014709041536, -1.2390105921196009, 7.61366895473381, 17.906914957575722, 25.439268285591233, 36.61613040516232, 42.4628507451078, 41.974154452289355, 40.06838767421818, 43.25851067544869, 39.47783881545585, 35.61838244192553, 28.061920663456107, 18.377353394945686, 4.58896484747706, -8.459560902196838, -15.814600155128417, -8.028401861533702, -5.652652030984714, -3.3951122822386153};
	private double[] lowOffset= {11.643070307748701, 32.384907278229335, 45.62286929923647, 45.91809364666366, 53.7680407853198, 64.78114987443269, 74.02615199671993, 70.93010255513597, 67.12097593152778, 38.256758946557916, 9.782170178906464, -12.363087476294611, -13.819861407940836, -4.689977573317448, 11.858343697834385, 22.63371341954341, 39.00533565526617, 45.0337885196405, 43.094419241524406, 51.63995683999734, 63.470698394140385, 70.7834015429034, 74.65458302104955, 75.14472539122602, 56.741052685510894, 35.08139689846324, 18.22026048620182, 8.545141700015492, 4.904884915735691, 5.296812169088764};
	private double[] volOffset= {-0.24515125933853618, 0.013921268171043576, -0.17193988279001682, -0.16278344057818467, 0.18817766097172897, -0.2630716173809428, 0.35854970306157125, 0.026547130072941027, -0.02679404235229738, -0.2895227529457613, -0.17747365534556483, -0.3336859340877492, -0.20348088522000113, -0.09203534489564101, -0.17290771721567855, -0.27207380579828866, -0.4873983526947272, -0.29369691714085117, 0.06855407838434915, 0.2612474084602476, -0.6182185319661271, -0.48763195600496406, -0.09614345271889099, -0.2814339456469626, 0.06888160631974752, 0.2768985248487771, 0.5408851927709617, 0.2000421314820568, -0.25117356231480503, 0.02395448509432893};

		private double theta0 = -1.3921675318722069;
		private double H_THREHOLD = 0.95d;
		
		
	public StestSGD() {
		super(myStatisticType);
	}
	
	public StestSGD(String myStatisticType, int pre, int gain,
			double[] closeOffset, double[] closeOpenOffset,
			double[] volOffset, double[] openOffset,
			double[] highOffset, double[] lowOffset, double theta0, double H_THREHOLD
			) {
		super(myStatisticType);
		this.myStatisticType = myStatisticType;
		this.PREVIOUS = pre;
		this.GAIN = gain;
		this.closeOffset = closeOffset;
		this.volOffset = volOffset;
		this.openOffset = openOffset;
		this.highOffset = highOffset;
		this.lowOffset = lowOffset;
		this.theta0 = theta0;
		this.H_THREHOLD = H_THREHOLD;
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
	
	public double[][] getOffsetParams(){
		double[][] result = new double[5][];
		result[0] = this.closeOffset;
		result[1] = this.openOffset;
		result[2] = this.highOffset;
		result[3] = this.lowOffset;
		result[4] = this.volOffset;
		return result;
	}
	

	@Override
	public DataArray actualGenerate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray statisticArray = null;
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray open = dataMap.getDataArray(Constant.OPEN);
			DataArray high = dataMap.getDataArray(Constant.HIGH);
			DataArray vol = dataMap.getDataArray(Constant.VOL);
			DataArray low = dataMap.getDataArray(Constant.LOW);
			statisticArray = new DataArray(stockCode, myStatisticType, dataMap);
			int start = 0;
            int count = 0;
            for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				statisticArray.addData(data);
			}
            
            for (int i = 0; i < close.size(); i++) {
            	if (i<=DELAY){
            		continue;
            	}
                if (Math.abs(close.getValue(i) - open.getValue(i)) > open.getValue(i) * 0.1f
                        || Math.abs(close.getValue(i) - close.getValue(i - 1)) > close.getValue(i - 1) * 0.15) {
                    start = i;
                    count = 0;
                    continue;
                }
                boolean tobuy = false;

                double[] closeList = new double[PREVIOUS];
                double[] volList = new double[PREVIOUS];
                double[] openList = new double[PREVIOUS];
                double[] highList = new double[PREVIOUS];
                double[] lowList = new double[PREVIOUS];
                double[][] valueLists = new double[5][PREVIOUS];
                valueLists[0] = closeList;
                valueLists[1] = openList;
                valueLists[2]  = highList;
                valueLists[3] = lowList;
                valueLists[4] = volList; 
                
                for (int j = i, k=0; j>0 && k<PREVIOUS; j--,k++) {
                    double c = (double) (close.getValue(j) / close.getValue(j - 1));
                	double o = (double) (open.getValue(j) / open.getValue(j - 1));
                	double h = (double) (high.getValue(j) / high.getValue(j - 1));
                	double l = (double) (low.getValue(j) / low.getValue(j - 1));
                	double v = (double) (vol.getValue(j) / vol.getValue(j - 1));
                    
                    closeList[k] = Math.log10(c);
                    volList[k] = Math.log10(v);
                    openList[k] = Math.log10(o);
                    highList[k] = Math.log10(h);
                    lowList[k] = Math.log10(l);
                }
                double hi = hypothesis(valueLists, getOffsetParams(), theta0);
        		if (hi > H_THREHOLD){
        			tobuy = true;
        		}
				
				if (tobuy){
					statisticArray.setValue(i, 1);
				}

            }
          //sell: cross ma5 ma10
			int gainLeft = -1;
			for (int i = 0 ; i<close.size()-GAIN;i++)
			{
				if (gainLeft>0){
					gainLeft--;
				}
				if (statisticArray.getValue(i)>0){
					gainLeft = GAIN;
				}
				if(gainLeft==0){
					gainLeft=-1;
					statisticArray.setValue(i, -1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statisticArray;
	}
	
	private static double hypothesis(double[][] x, double[][] params, double theta0){
		return sigmod(getThetaX(x, params, theta0));
    }
    
	
	public static double getThetaX(double[][] x, double[][] params, double theta0){
    	double thetaX = 0d;
		for (int j=0;j<x[0].length;j++){
			for (int i=0;i<x.length;i++){
				double Xj = x[i][j];
				double Oj = params[i][j];
				thetaX += Xj*Oj;
			}
		}
		return thetaX+theta0;
    }
	
	private static double sigmod(double x){
    	return 1.0d/(1.0d+Math.exp(-x));
    }

}
