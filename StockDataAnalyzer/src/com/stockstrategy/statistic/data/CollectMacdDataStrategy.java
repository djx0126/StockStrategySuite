package com.stockstrategy.statistic.data;

import com.djx.stockgainanalyzer.data.MAField;
import com.djx.stockgainanalyzer.data.PreviousData;
import com.djx.stockgainanalyzer.data.RelativeMAData;
import com.djx.stockgainanalyzer.data.StockGainData;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;
import com.stockstrategy.data.SharedStockDataHolder;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.tools.MacdDataTester;

import java.util.Date;
import java.util.Random;


/**
 * Created by dave on 2015/9/29.
 */
public class CollectMacdDataStrategy extends AbstractSPreGain{
    public static final String NAME = "CollectMacdDataStrategy";
    public static int PREVIOUS = StockDataAnalyzer.PRE;
    public static int GAIN = StockDataAnalyzer.GAIN;
    public static int MAX_MA_DAYS = StockDataAnalyzer.fieldModel.maxMaDays;
    public static int OVER_ALL_MAX_MA_DAYS = StockDataAnalyzer.fieldModel.overAllMaxMaDays;


    public CollectMacdDataStrategy() {
        super(NAME, PREVIOUS, GAIN, /*limit*/PREVIOUS+MAX_MA_DAYS+OVER_ALL_MAX_MA_DAYS);

    }

    @Override
    public DataArray actualGenerate(String stockCode, String statisticType,
                                    DataMap dataMap) {
        DataArray statisticArray = null;
        try {
            DataArray close = dataMap.getDataArray(Constant.CLOSE);
            DataArray open = dataMap.getDataArray(Constant.OPEN);
            DataArray macd = dataMap.getDataArray(Constant.MACD);
            DataArray dif = dataMap.getDataArray(Constant.MACDDIF);
            DataArray atr = dataMap.getDataArray(Constant.ATR);
            statisticArray = new DataArray(stockCode, NAME, dataMap);
            int start = 0;
            int count = 0;
            for (int i = 0; i < close.size(); i++) {
                RawData data = new RawData(close.getDate(i), 0);
                statisticArray.addData(data);
            }

            for (int i = 0; i < close.size(); i++) {

                if (i < 10) {
                    continue;
                }

                if (close.getValue(i) - close.getValue(i - 1) > 0.09 * close.getValue(i - 1)) {
                    continue;
                }

                if (Math.abs(close.getValue(i) - open.getValue(i)) > open.getValue(i) * 0.1f
                        || Math.abs(close.getValue(i) - close.getValue(i - 1)) > close.getValue(i - 1) * 0.15) {
                    start = i;
                    count = 0;
                    continue;
                }
                boolean tobuy = false;

                if (macd.getValue(i) > 0 && macd.getValue(i - 1) < 0  ) {
                    int lastDeathCrossK = -1;
                    for (int j = i - 10; j > 2; j--) {
                        if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
                            lastDeathCrossK = j;
                            break;
                        }
                    }

                    boolean minorDeath = false;
                    for (int j = i -1; j > 2 && j>i-9 ; j--) {
                        if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
                            minorDeath = true;
                            break;
                        }
                    }

                    int lastGoldenCross = -1;
                    double maxDif = -100;
                    for (int j = lastDeathCrossK - 2; lastDeathCrossK > 20 && j > 2; j--) {
                        double dif1 = dif.getValue(j)/ Math.abs(atr.getValue(j));
                        if (dif1 > maxDif) {
                            maxDif = dif1;
                        }

                        if (macd.getValue(j) > 0 && macd.getValue(j - 1) < 0) {
                            lastGoldenCross = j;
                            break;
                        }
                    }

                    if (lastDeathCrossK > 0 && lastGoldenCross > 0 && !minorDeath) {
                        if (i - lastGoldenCross <60 && i - lastGoldenCross > 12
                        ) {
                            tobuy = true;

                            int nextDeathCross = -1;
                            for (int j = i+1 ; j<close.size()-GAIN;j++)
                            {
                                if (macd.getValue(j) < 0 && macd.getValue(j - 1) > 0) {
                                    nextDeathCross = j;
                                    break;
                                }
                            }

                            if (nextDeathCross > 0) {
                                double gain = (close.getValue(nextDeathCross) - close.getValue(i)) * 100/close.getValue(i);
                                MacdData macdData = new MacdData();
                                macdData.code = stockCode;
                                macdData.date = close.getDate(i);
                                macdData.gain = gain;
                                macdData.dif = dif.getValue(i)/Math.abs(atr.getValue(i));
                                macdData.lastDeathCross = i - lastDeathCrossK;
                                macdData.maxDif = maxDif;
                                macdData.lastGoldenCross = i - lastGoldenCross;
                                macdData.lastGoldenCrossDif = dif.getValue(lastGoldenCross)/Math.abs(atr.getValue(lastGoldenCross));

                                MacdDataTester.addData(macdData);

//								System.out.printf("%s %s %.2f dif=%.3f d_sc=%d maxDif=%.3f d_bc=%d dif_bc=%.3f\n", close.getDate(i), stockCode, gain, dif.getValue(i), i - lastDeathCrossK, maxDif, i- lastGoldenCross, dif.getValue(lastGoldenCross));
                            }
                        }
                    }
                }

                if (tobuy){
                    statisticArray.setValue(i, 1);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return statisticArray;
    }

    public static class MacdData {
        public String date;
        public String code;
        public double gain;
        public double dif;
        public int lastDeathCross;
        public double maxDif;
        public int lastGoldenCross;
        public double lastGoldenCrossDif;

        public String getDate() {
            return date;
        }

        public String getCode() {
            return code;
        }

        public double getGain() {
            return gain;
        }

        public double getDif() {
            return dif;
        }

        public int getLastDeathCross() {
            return lastDeathCross;
        }

        public double getMaxDif() {
            return maxDif;
        }

        public int getLastGoldenCross() {
            return lastGoldenCross;
        }

        public double getLastGoldenCrossDif() {
            return lastGoldenCrossDif;
        }
    }
}
