package com.stockstrategy.statistic.data;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import com.djx.stockgainanalyzer.StockAnalyzer;
import com.djx.stockgainanalyzer.data.PreviousData;
import com.djx.stockgainanalyzer.data.StockGainData;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;

public class GainDataStrategy extends AbstractStatisticData{
	public static final String NAME = "GainDataStrategy";
	private static final int PREVIOUS = StockAnalyzer.PREVIOUS_NUM;
    private static final int GAIN = StockAnalyzer.GAIN_NUM;
//    private static final int[] MA_LIST = StockAnalyzer.MA_LIST;
//    private static final int MA_MAX = Arrays.stream(MA_LIST).max().getAsInt();
    public static int keyDateCount = 0;
    
    private Random random = new Random(new Date().getTime());
	
	public GainDataStrategy(String type) {
		super(type);
	}
	
	public GainDataStrategy(){
		this(NAME);
	}

	@Override
	public DataArray generate(String stockCode, String statisticType,
			DataMap dataMap) {
		DataArray dLine = null;
		try {
			DataArray close = dataMap.getDataArray(Constant.CLOSE);
			DataArray open = dataMap.getDataArray(Constant.OPEN);
			DataArray high = dataMap.getDataArray(Constant.HIGH);
			DataArray vol = dataMap.getDataArray(Constant.VOL);
			DataArray low = dataMap.getDataArray(Constant.LOW);
			dLine = new DataArray(stockCode, NAME, dataMap);
			int start = 0;
            int count = 0;
//            DBHelper helper = new DBHelper();
            for (int i = 0; i < close.size(); i++) {
				RawData data = new RawData(close.getDate(i), 0);
				dLine.addData(data);
			}
            
            for (int i = 0; i < close.size(); i++) {
            	if (i<=0){
            		continue;
            	}
            	
                if (Math.abs(close.getValue(i) - open.getValue(i)) > open.getValue(i) * 0.1f
                        || Math.abs(close.getValue(i) - close.getValue(i - 1)) > close.getValue(i - 1) * 0.15) {
                    start = i;
                    count = 0;
                    continue;
                }
                
                if (i>=close.size()){
                	continue;
                }

                if (count < PREVIOUS + GAIN - 1) {// not full
                    count++;
                } else {
                    if (i - start > PREVIOUS + GAIN) {
                        start++;
                    }
                    int key = i - GAIN;
                    String keyDate = close.getDate(key);

                    double nextLow = low.getValue(key + 1);
                    double keyDayClose = close.getValue(key);
                    if (nextLow > keyDayClose){
                        continue;
                    }

                    float gain = (float) (close.getValue(i) / close.getValue(key));
                    // System.out.println(stockCode + " on " + keyDate +
                    // ", gain:" + gain);
                    
                    StockGainData data = new StockGainData(stockCode, keyDate, gain);
//                    int maStart = key-MA_MAX;
//                    int calcStart = Math.min(start, maStart);
//                    double closeSum = 0;
//                    double[] relativeMA = new double[MA_LIST.length];
//                    int closeSumIdx = 0;
//                    for (int j = key,k=1; j > calcStart; j--, k++) {
                    for (int j = key; j > start; j--) {
//                        if (j > start){
                            double c = (double) (close.getValue(j) / close.getValue(j - 1));
                            double o = (double) (open.getValue(j) / open.getValue(j - 1));
                            double day = (double) (close.getValue(j) / open.getValue(j));
                            double h = (double) (high.getValue(j) / high.getValue(j - 1));
                            double l = (double) (low.getValue(j) / low.getValue(j - 1));
                            double v = (double) (vol.getValue(j) / vol.getValue(j - 1));
                            // System.out.println(" " + offset + " price:" + price +
                            // ", vol=" + v + ", closeOpen" + day);

                            PreviousData pData = new PreviousData();
                            pData.setClose(c);
                            pData.setOpen(o);
                            pData.setCloseOpen(day);
                            pData.setHigh(h);
                            pData.setLow(l);
                            pData.setVol(v);
                            pData.setDate(close.getDate(j));
                            data.insertPreviousData(pData);
//                        }
//                        closeSum += close.getValue(j);
//                        if (MA_LIST[closeSumIdx] == k){
//                            relativeMA[closeSumIdx++] = close.getValue(key)/(closeSum/k);
//                        }
                    }
//                    data.setRelativeMA(relativeMA);
//                    helper.prepareBulkStockData(data);
                    if (StockAnalyzer.usingPreFilter){
                    	if (random.nextFloat()<StockAnalyzer.preFilterRate){
                    		StockAnalyzer.addData(data);
                    	}
                    }else{
                    	StockAnalyzer.addData(data);
                    }
                    
                    
                    keyDateCount++;
                }

            }
//            helper.batchSave();
//            helper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dLine;
	}
	
}
