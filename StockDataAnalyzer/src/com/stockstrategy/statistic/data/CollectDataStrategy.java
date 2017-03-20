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

import java.util.Date;
import java.util.Random;


/**
 * Created by dave on 2015/9/29.
 */
public class CollectDataStrategy extends AbstractSPreGain{
    public static final String NAME = "CollectDataStrategy";
    public static int PREVIOUS = StockDataAnalyzer.PRE;
    public static int GAIN = StockDataAnalyzer.GAIN;
    public static int MAX_MA_DAYS = StockDataAnalyzer.fieldModel.maxMaDays;
    public static int OVER_ALL_MAX_MA_DAYS = StockDataAnalyzer.fieldModel.overAllMaxMaDays;

    private static Random random = new Random(new Date().getTime());

    public CollectDataStrategy() {
        super(NAME, PREVIOUS, GAIN, /*limit*/PREVIOUS+MAX_MA_DAYS+OVER_ALL_MAX_MA_DAYS);

    }

    @Override
    public DataArray actualGenerate(String stockCode, String statisticType, DataMap dataMap) {
        DataArray dLine = new DataArray(stockCode, NAME, dataMap);
        try {
            DataArray close = dataMap.getDataArray(Constant.CLOSE);
            DataArray open = dataMap.getDataArray(Constant.OPEN);
            DataArray high = dataMap.getDataArray(Constant.HIGH);
            DataArray low = dataMap.getDataArray(Constant.LOW);
            DataArray vol = dataMap.getDataArray(Constant.VOL);
            DataArray forceBuyPrice = dataMap.getDataArray(Constant.ForceBuyPrice);

            for (int i = 0; i < close.size(); i++) {
                RawData data = new RawData(close.getDate(i), 0);
                dLine.addData(data);
            }

            DataArray SHClose = null;

            String StockCode001 = getStockCode001(stockCode);
            SHClose = SharedStockDataHolder.getInstance().get(StockCode001).getDataArray(Constant.CLOSE);

            for (int i = 0; i < close.size(); i++) {
                if (i < PREVIOUS || i + GAIN >= close.size()) {
                    continue;
                }

                if (MAX_MA_DAYS > 0 && i < MAX_MA_DAYS ) {
                    continue;
                }

                boolean mayBeAllotment = false;
                for (int j = i-PREVIOUS+1; !mayBeAllotment && j<=i+GAIN;j++) {
                    if (Math.abs(close.getValue(j) - open.getValue(j)) > open.getValue(j) * 0.1f
                            || Math.abs(close.getValue(j) - close.getValue(j - 1)) > close.getValue(j - 1) * 0.15) {
                        mayBeAllotment = true;
                    }
                }
                if (mayBeAllotment) {
                    continue;
                }

                String keyDate = close.getDate(i);
                int overAllIndex = SHClose.getIndexByDate(keyDate);
                if (OVER_ALL_MAX_MA_DAYS > 0 && overAllIndex < OVER_ALL_MAX_MA_DAYS || overAllIndex >= SHClose.size() || overAllIndex < 0) {
                    continue;
                }

                if (hasAllotment(i, close, open)) {
                    continue;
                }

                if (StockDataAnalyzer.skipBigChange && hasBigChange(i, close, open)) {
                    continue;
                }

                if (hasRecentBreakDay(i, close, SHClose)){
                    continue;
                }

                if (!StockDataAnalyzer.forceBuy && i<close.size()-1){
                    double nextLow = low.getValue(i + 1);
                    double todayClose = close.getValue(i);
                    if (nextLow > todayClose){
                        continue;
                    }
                }

                int key = i;
                int start = key - PREVIOUS;
                int maStart = key - MAX_MA_DAYS;
                int keyInOverAll = SHClose.getIndexByDate(keyDate);
                int overAllmaStart = keyInOverAll - OVER_ALL_MAX_MA_DAYS;
                int end = key + GAIN;

                if (start <0 || maStart<0 || overAllmaStart<0 || end >= close.size()){
                    continue;
                }

                double keyDayClose = close.getValue(key);

                float gain = (float) (close.getValue(end) / close.getValue(key));

                if (StockDataAnalyzer.forceBuy) {
                    double buyPrice = forceBuyPrice.getValue(key);
                    gain = (float) (close.getValue(end) / buyPrice);
                }

                StockGainData data = new StockGainData(stockCode, keyDate, gain);
                for (int j = key; j > start; j--) {
                    double c = close.getValue(j) / close.getValue(j - 1);
                    double o = open.getValue(j) / open.getValue(j - 1);
                    double h = high.getValue(j) / high.getValue(j - 1);
                    double l = low.getValue(j) / low.getValue(j - 1);
                    double v = vol.getValue(j) / vol.getValue(j - 1);

                    PreviousData pData = new PreviousData();
                    pData.setClose(c);
                    pData.setOpen(o);
                    pData.setHigh(h);
                    pData.setLow(l);
                    pData.setVol(v);
                    pData.setDate(close.getDate(j));
                    data.insertPreviousData(pData);
                }

                RelativeMAData relativeMAData = buildStockRelativeMA(dataMap, key, keyDayClose, StockDataAnalyzer.fieldModel.maList, StockDataAnalyzer.fieldModel.maFields);
                data.setRelativeMA(relativeMAData);

                double overAllKeyDayClose = SHClose.getValue(keyInOverAll);
                RelativeMAData relativeOverAllMAData = buildStockRelativeMA(SharedStockDataHolder.getInstance().get(StockCode001), keyInOverAll, overAllKeyDayClose, StockDataAnalyzer.fieldModel.overAllMaList, StockDataAnalyzer.fieldModel.overAllMaFields);

                data.setRelativeOverAllMAData(relativeOverAllMAData);

                if ( /*no PreFilter*/!StockDataAnalyzer.usingPreFilter ||
                        /*PreFilter*/random.nextFloat()<StockDataAnalyzer.preFilterRate){
                    StockDataAnalyzer.addStockGainData(StockDataAnalyzer.dataModelHelper.buildStockDataModel(StockDataAnalyzer.fieldModel, data));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dLine;
    }

    private RelativeMAData buildStockRelativeMA(DataMap dataMap, int i, double keyDayClose, int[] maList, MAField[] maFields) throws Exception {
        RelativeMAData relativeMAData = new RelativeMAData(maFields);
        for(int index = 0; index<maList.length; index++) {
            int maType = maList[index];
            double maValue = this.getMAValue(dataMap, maType, i);
            relativeMAData.setValue(maFields[index], keyDayClose/maValue);
        }

        return relativeMAData;
    }

}
