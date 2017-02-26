package com.djx.stockgainanalyzer.data;

import java.util.List;

public interface StockPreviousDataMapper {
    public int saveStock(StockGainData data);
    public void saveBulkStock(List<StockGainData> list);
    public StockGainData loadStock(StockGainData data);
    public List<StockGainData> loadStocks();
    public void savePreviousData(PreviousData previousData);
    public void saveBulkPreviousData(List<PreviousData> list);
    public void clearGainData();
    public void clearPreviousData();
}
