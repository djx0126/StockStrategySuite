package com.djx.data;

import com.stockstrategy.http.RawDayDataItem;

import java.util.List;

/**
 * Created by dave on 2017/4/2.
 */
public interface DataReader {
    List<RawDayDataItem> readData(String stockCode, String startDate, String endDate);
}
