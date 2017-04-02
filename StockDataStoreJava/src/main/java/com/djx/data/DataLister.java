package com.djx.data;

import com.stockstrategy.http.StockListItem;

import java.util.List;

/**
 * Created by dave on 2017/4/2.
 */
public interface DataLister {
    List<StockListItem> getSharedStockList();
    List<StockListItem> getStockList();
}
