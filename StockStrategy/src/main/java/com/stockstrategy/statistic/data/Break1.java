package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.data.RawData;

/**
 * Created by dave on 2017/4/9.
 */
public class Break1 extends AbstractStrategyStatisticData {
    public static String statisticType = "Break1";

    int preDaysToWatch = 60;
    double rangeLimit = 0.01;

    double safeKeeperPer = 10.0;
    double dropFromHigh = 10.0;

    public Break1() {
        super(statisticType);
    }

    @Override
    protected DataArray actualGenerate(String stockCode, String statisticType, DataMap dataMap) {
        DataArray statisticArray = new DataArray(stockCode, this.statisticType, dataMap);

        DataArray open = null;
        DataArray close = null;
        DataArray high = null;
        DataArray low = null;
        DataArray vol = null;
        DataArray ma30 = null;
        DataArray ma120 = null;
        try {
            open = dataMap.getDataArray(Constant.OPEN);
            close = dataMap.getDataArray(Constant.CLOSE);
            high = dataMap.getDataArray(Constant.HIGH);
            low = dataMap.getDataArray(Constant.LOW);
            vol = dataMap.getDataArray(Constant.VOL);
            ma30 = dataMap.getDataArray(Constant.MA30);
            ma120 = dataMap.getDataArray(Constant.MA120);

            for (int i = 0; i<close.size();i++)
            {
                RawData data = new RawData(close.getDate(i),0);
                statisticArray.addData(data);
            }

            // set buy
            for (int i= preDaysToWatch - 1 + 1 + 1; i< close.size() -1; i++ ) {
                if (hasAllotment(i, close, open, preDaysToWatch)) {
                    continue;
                }

                double maxPre = MAX(high, i - 2 - preDaysToWatch + 1, i - 1 - 1);
                double minPre = MIN(low, i - 2 - preDaysToWatch + 1, i - 1 - 1);
                double avgPre = AVG(close, i - 2 - preDaysToWatch + 1, i - 1 - 1);

                if (maxPre - minPre > rangeLimit * avgPre) {
                    continue;
                }

                double lastClose = close.getValue(i - 1);
                double currentClose = close.getValue(i);

//                if (currentClose <= ma30.getValue(i)) {
//                    continue;
//                }

//                if (ma120.getValue(i) < ma250.getValue(i)) {
//                    continue;
//                }

                if (lastClose <= maxPre && currentClose > maxPre) {
                    statisticArray.setValue(i, 1);
                }
            }


            // set sell
            setSell(statisticArray, close);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return statisticArray;
    }

    protected void setSell(DataArray statisticArray, DataArray close) throws Exception {
        boolean inHand = false;
        int buyIdx = -1;
        for (int i =0; i < statisticArray.size(); i++) {
            int toBuy = (int)statisticArray.getValue(i);

            if (!inHand && toBuy > 0) {
                inHand = true;
                buyIdx = i;
            }else if (inHand) {
                // test sell condition
                if (isToSell(i, buyIdx, close)) {
                    inHand = false;
                    buyIdx = -1;
                    statisticArray.setValue(i, -1);
                } else {
                    statisticArray.setValue(i, 0);
                }
            }
        }

        if (inHand) {
            statisticArray.setValue(statisticArray.size() - 1, -1);
        }
    }

    protected boolean isToSell(int currentIdx, int buyIdx, DataArray close) throws Exception {
        boolean toSell = false;
        double buyPrice = close.getValue(buyIdx);
        double highPrice = buyPrice;
        for (int i = buyIdx + 1; i <= currentIdx; i++) {
            double closePrice = close.getValue(i);

            // safe keeper from buy
            if ( closePrice < buyPrice * (1 - safeKeeperPer/100) ) {
                toSell = true;
                break;
            }

            // drop from high
            if (closePrice > highPrice) {
                highPrice = closePrice;
                continue;
            } else if (closePrice < highPrice *(1 - dropFromHigh / 100)) {
                toSell = true;
                break;
            }
        }
        return toSell;
    }

    @Override
    protected void patchAllotment(String stockCode, DataArray buysellArray) {
        // skip original allotment patch
        patchAllotment(stockCode, buysellArray, preDaysToWatch);
        return;
    }
}
