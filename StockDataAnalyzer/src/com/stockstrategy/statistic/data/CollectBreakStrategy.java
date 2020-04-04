package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.tools.BreakDataTester;
import djx.stockdataanalyzer.tools.MacdDataTester;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by dave on 2015/9/29.
 */
public class CollectBreakStrategy extends AbstractSPreGain{
    public static final String NAME = "CollectBreakStrategy";
    public static int PREVIOUS = StockDataAnalyzer.PRE;
    public static int GAIN = StockDataAnalyzer.GAIN;
    public static int MAX_MA_DAYS = StockDataAnalyzer.fieldModel.maxMaDays;
    public static int OVER_ALL_MAX_MA_DAYS = StockDataAnalyzer.fieldModel.overAllMaxMaDays;

    private static int BREAKLONG = 52;
    private static int LONGLONG = 100;


    public CollectBreakStrategy() {
        super(NAME, PREVIOUS, GAIN, /*limit*/PREVIOUS+MAX_MA_DAYS+OVER_ALL_MAX_MA_DAYS);

    }

    @Override
    public DataArray actualGenerate(String stockCode, String statisticType,
                                    DataMap dataMap) throws Exception {
        DataArray close = dataMap.getDataArray(Constant.CLOSE);
        DataArray open = dataMap.getDataArray(Constant.OPEN);
        DataArray high = dataMap.getDataArray(Constant.HIGH);
        DataArray low = dataMap.getDataArray(Constant.LOW);
        DataArray ma20 = dataMap.getDataArray(Constant.MA20);
        DataArray macd_atr = dataMap.getDataArray(Constant.MACD_ATR);
        DataArray statisticArray = new DataArray(stockCode, NAME, dataMap);
        DataArray R2 = new DataArray(stockCode, NAME, dataMap);
        DataArray Peek = new DataArray(stockCode, NAME, dataMap);

        DataArray ema65 = new DataArray(stockCode, "EMA12", dataMap);
        DataArray ema130 = new DataArray(stockCode, "EMA26", dataMap);
        DataArray macd_l = new DataArray(stockCode, "MACDL", dataMap);

        Map<String, BreakData> buyData = new HashMap<>();


        for (int i = 0; i < close.size(); i++) {
            statisticArray.addData(new RawData(close.getDate(i), 0));

            R2.addData(new RawData(close.getDate(i), 0));
            Peek.addData(new RawData(close.getDate(i), 0));

            ema65.addData(new RawData(close.getDate(i), 0));
            ema130.addData(new RawData(close.getDate(i), 0));
            macd_l.addData(new RawData(close.getDate(i), 0));
        }

        double tempEma65 = 0f;
        double tempEma130 = 0f;
        for (int i = 0;i <close.size(); i++) {
            double EDGEH = 0.0;
            double PEEK = 0.0;
            double EDGEL = 100000.0;
            double maCloseL = 0.0;
            double sumClose = 0.0;
            int count = 0;
            for (int k = i-BREAKLONG+1; k<=i ; k++ ) {
                if (k >=0) {
                    count++;
                    double tryEdgeH = Math.max(open.getValue(k), close.getValue(k));
                    if (tryEdgeH > EDGEH) {
                        EDGEH = tryEdgeH;
                    }
                    double tryPeek = (tryEdgeH + high.getValue(k))/2;
                    if (tryPeek > PEEK) {
                        PEEK = tryPeek;
                    }
                    double tryEdgeL = Math.min(open.getValue(k), close.getValue(k));
                    if (tryEdgeL < EDGEL) {
                        EDGEL = tryEdgeL;
                    }

                    sumClose += close.getValue(k);
                }
            }
            maCloseL = count > 0 ? sumClose / count : 0.0;
            //R2 := IF( ATRL=0, 0, (HL1-LL1)*10/MAL );
            double rate = maCloseL > 0 && EDGEH > EDGEL ? (EDGEH - EDGEL)*10.0/maCloseL : 0;
            R2.setValue(i, rate);

            Peek.setValue(i, PEEK);

            tempEma65=  tempEma65*64/66+close.getValue(i)*2/66   ;
            ema65.setValue(i, tempEma65);

            tempEma130=  tempEma130*129/131+close.getValue(i)*2/131   ;
            ema130.setValue(i, tempEma130);

            macd_l.setValue(i, tempEma65-tempEma130);
        }

        for (int i = 0; i < close.size(); i++) {

            if (i < LONGLONG) {
                continue;
            }

            if (close.getValue(i) - close.getValue(i - 1) > 0.09 * close.getValue(i - 1)) {
                continue;
            }

            if (Math.abs(close.getValue(i) - open.getValue(i)) > open.getValue(i) * 0.1f
                    || Math.abs(close.getValue(i) - close.getValue(i - 1)) > close.getValue(i - 1) * 0.15) {
                continue;
            }
            boolean tobuy = false;

            // CLOSE > REF(HL2,1) AND UP_BROKE=1 AND  REF(R2,1) < REF(L_R2,1)
            double Avg_R2 = AVG(R2, i-BREAKLONG, i-1);
            double sum_Dif_r2 = 0.0;
            for (int k = i-BREAKLONG; k<i; k++ ) {
                sum_Dif_r2 += ((R2.getValue(k) - Avg_R2) * (R2.getValue(k) - Avg_R2));
            }
            double std_R2 = Math.sqrt(sum_Dif_r2/ BREAKLONG) ;
            // L_R2 := AVG_R2 - STD_R2;
            double L_r2 = Avg_R2 - std_R2;

            boolean isBreak = false;
            for (int k = i; k > i-13; k--) {
                if (close.getValue(k) > Peek.getValue(k-1)) {
                    isBreak = true;
                    if (k < i) {
                        isBreak = false;
                    }
                }
            }
            isBreak = isBreak && R2.getValue(i -1 ) < L_r2;


            // MTR:MAX(MAX((HIGH-LOW),ABS(REF(CLOSE,1)-HIGH)),ABS(REF(CLOSE,1)-LOW));
            //SPEED :=100 * MA( ABS(TR)/CLOSE  , LONGLONG);
            //SPPED_IN_RANGE := REF(SPEED,1) > 3 AND  REF(SPEED,1) < 4.5;
            double sumTR = 0.0;
            for (int k = i -LONGLONG; k< i && k >=1; k++) {
                double TR = Math.max(Math.max(high.getValue(k) - low.getValue(k), Math.abs(close.getValue(k-1) - high.getValue(k))) , Math.abs(close.getValue(k-1) - low.getValue(k)) );
                sumTR += Math.abs(TR)/close.getValue(k);
            }
            double speed = 100 * sumTR/LONGLONG;
            boolean SPPED_IN_RANGE = speed > 3 && speed < 4.5;

            //ER_L:=ABS(CLOSE - REF(CLOSE, LONG) )/ SUM( ABS(CLOSE - REF(CLOSE, 1)) , 52);
            //M_ER_L := HHV(ER_L, 52);
            double maxER = 0.0d;
            for (int k = i -1 ; k >= i-52; k--) {
                double absoluteChange = Math.abs(close.getValue(k) - close.getValue(k-26));
                double sumChange = 0.0d;
                for (int j = k ;j >k-26; j--) {
                    sumChange += Math.abs(close.getValue(j) - close.getValue(j-1));
                }
                double er = absoluteChange/ sumChange;
                if (er > maxER) {
                    maxER = er;
                }
            }
            boolean smallER = maxER < 0.5;

            boolean maIncrease = ma20.getValue(i) > ma20.getValue(i-1) && ma20.getValue(i) > ma20.getValue(i-2) && ma20.getValue(i) > ma20.getValue(i-3)
                    && ma20.getValue(i-3) > ma20.getValue(i-5) && ma20.getValue(i-5) > ma20.getValue(i-10);

            if (isBreak && SPPED_IN_RANGE && maIncrease){
                BreakData data = new BreakData();
                data.code = close.getStockCode();
                data.buyDate = close.getDate(i);
                data.buyPrice = close.getValue(i);
                data.buyDateIdx = i;


                for (int k = i-52; k <i ; k++) {
                    if (macd_atr.getValue(k) < 0 && macd_atr.getValue(k+1) > 0) {
                        data.gcCount++;
                    }
                    if (macd_atr.getValue(k) > 0 && macd_atr.getValue(k+1) < 0) {
                        data.dcCount++;
                    }
                    if (macd_atr.getValue(k) < data.minMacd) {
                        data.minMacd = macd_atr.getValue(k);
                    }
                    if (macd_atr.getValue(k) > data.maxMacd) {
                        data.maxMacd = macd_atr.getValue(k);
                    }

                    if (macd_l.getValue(k) < 0 && macd_l.getValue(k+1) > 0) {
                        data.gcCountL++;
                    }
                    if (macd_l.getValue(k) > 0 && macd_l.getValue(k+1) < 0) {
                        data.dcCountL++;
                    }
                    if (macd_l.getValue(k) < data.minMacdL) {
                        data.minMacdL = macd_l.getValue(k);
                    }
                    if (macd_l.getValue(k) > data.maxMacdL) {
                        data.maxMacdL = macd_l.getValue(k);
                    }
                }
                data.macd = macd_atr.getValue(i);
                data.macdL = macd_l.getValue(i);

                if ( (data.dcCountL + data.gcCountL >=3  || data.dcCountL + data.gcCountL ==0)  && ( data.gcCount +data.dcCount >=3 && data.gcCount +data.dcCount <=9 ) ) {
                    buyData.put(data.buyDate, data);
                }
            }

        }

        boolean inhand = false;
        BreakData currentData = null;
        double sellPrice = 0.0;
        for (int i = 0; i< close.size(); i++) {
            if (!inhand && buyData.containsKey(close.getDate(i))) {
                inhand = true;
                statisticArray.setValue(i, 1);
                currentData = buyData.get(close.getDate(i));
                BreakDataTester.addData(currentData);
            } else if (inhand) {
                // test sell
                boolean toSell= false;

                double peek = MAX(high, i-52, i-1);

                toSell = close.getValue(i) < sellPrice || close.getValue(i) < peek * 0.8;

                if (toSell || i == close.size() - 1) {
                    inhand = false;
                    currentData.gain = 100 * (close.getValue(i) - currentData.buyPrice) / currentData.buyPrice;
                    currentData.dates = i - currentData.buyDateIdx;
                    currentData.sellDate = close.getDate(i);
                    currentData = null;
                    statisticArray.setValue(i, -1);
                }
            }

            if (inhand) {
                double sumTR = 0.0;
                for (int k = i -9; k< i; k++) {
                    double TR = Math.max(Math.max(high.getValue(k) - low.getValue(k), Math.abs(close.getValue(k-1) - high.getValue(k))) , Math.abs(close.getValue(k-1) - low.getValue(k)) );
                    sumTR += Math.abs(TR);
                }
                double atr_mid = sumTR / 9;
                double h1 = MAX(high, i-26, i-1);
                if (h1 - 3 * atr_mid > sellPrice) {
                    sellPrice = h1 - 3 * atr_mid;
                }

                double currentGain =  (close.getValue(i) - currentData.buyPrice)*100/currentData.buyPrice;
                if (currentGain < currentData.maxLoss) {
                    currentData.maxLoss = currentGain;
                }
            }
        }

        return statisticArray;
    }

    public static class BreakData {
        public String buyDate;
        public String sellDate;
        public String code;
        public double buyPrice;
        public double gain;
        public int buyDateIdx;
        public int dates;
        public double maxLoss;

        public int gcCount = 0;
        public int dcCount = 0;
        public double macd =0.0;
        public double minMacd = 0.0;
        public double maxMacd = 0.0;

        public int gcCountL = 0;
        public int dcCountL = 0;
        public double macdL =0.0;
        public double minMacdL = 0.0;
        public double maxMacdL = 0.0;

        public String getDate() {
            return buyDate;
        }

        public String getCode() {
            return code;
        }

        public double getGain() {
            return gain;
        }

        @Override
        public String toString() {
            return "BreakData{" +
                    "" + buyDate  +
                    "," + sellDate  +
                    ", " + code  +
                    ", " + gain +
                    ", " + dates +
                    ", " + maxLoss +
                    ", " + gcCount +
                    ", " + dcCount +
                    ", " + macd +
                    ", " + minMacd +
                    ", " + maxMacd +
                    '}';
        }
    }
}
