package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;

/**
 * Created by dave on 2017/3/16.
 */
public class ForceBuyPrice extends AbstractStatisticData {
    public ForceBuyPrice(String type) {
        super(type);
    }

    public ForceBuyPrice() {
        super(Constant.ForceBuyPrice);
    }

    @Override
    public DataArray generate(String stockCode, String statisticType, DataMap dataMap) {
        DataArray dLine = null;
        try {
            DataArray close = dataMap.getDataArray(Constant.CLOSE);
            DataArray high = dataMap.getDataArray(Constant.HIGH);
            DataArray low = dataMap.getDataArray(Constant.LOW);
            DataArray open = dataMap.getDataArray(Constant.OPEN);
            dLine = new DataArray(stockCode, Constant.ForceBuyPrice, dataMap);
            for(int i=0;i<close.size();i++){
                double buyPrice;
                int key = i;
                if (i == close.size() - 1) {
                    buyPrice = close.getValue(i);
                } else if (open.getValue(key + 1) < close.getValue(key)) {
                    buyPrice = close.getValue(key);
                } else if (low.getValue(key + 1) < close.getValue(key)) {
                    buyPrice = (low.getValue(key + 1) + open.getValue(key + 1)) / 2;
                } else {
                    buyPrice = open.getValue(key + 1) + (high.getValue(key + 1) - open.getValue(key + 1))*2/3;
                }
                dLine.addRawData(close.getDate(i), buyPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dLine;
    }
}
