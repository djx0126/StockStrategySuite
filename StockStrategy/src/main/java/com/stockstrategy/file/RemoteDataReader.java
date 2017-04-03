package com.stockstrategy.file;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.http.HttpClient;
import com.stockstrategy.http.RawDayDataItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteDataReader implements IDataReader {
    private DataMap dataMap = null;
    private DataArray open = null;
    private DataArray close = null;
    private DataArray high = null;
    private DataArray low = null;
    private DataArray volume = null;
    private String stockCode = null;
    private String startDate = null;
    private String endDate = null;

    public RemoteDataReader(String stockCode, String startDate, String endDate) {
        this.stockCode = stockCode;
        this.startDate = startDate;
        this.endDate = endDate;
        prepareDataMap();
    }

    @Override
    public DataMap readDataMap() {
        List<RawDayDataItem> rawDayDataItems = readFromRemote();
        rawDayDataItems.stream().forEach(this::parseRemoteDataItem);
        return dataMap;
    }

    private List<RawDayDataItem> readFromRemote() {
        String url = Constant.DATA_STORE_ADDRESS + "/data";
        Map<String, Object> params = new HashMap<>();
        params.put("code", this.stockCode);
        params.put("start", this.startDate);
        params.put("end", this.endDate);
        return HttpClient.getInstance().getList(url, params, RawDayDataItem.class);
    }

    private void parseRemoteDataItem(RawDayDataItem dataItem) {
        String date = (String) dataItem.get("date");
        double closeValue = Double.parseDouble(dataItem.get(Constant.CLOSE).toString());
        double openValue = Double.parseDouble(dataItem.get(Constant.OPEN).toString());
        double highValue = Double.parseDouble(dataItem.get(Constant.HIGH).toString());
        double lowValue = Double.parseDouble(dataItem.get(Constant.LOW).toString());
        double volValue = Double.parseDouble(dataItem.get(Constant.VOL).toString());
        close.addRawData(date, closeValue);
        open.addRawData(date, openValue);
        high.addRawData(date, highValue);
        low.addRawData(date, lowValue);
        volume.addRawData(date, volValue);
    }

    private void prepareDataMap() {
        dataMap = new DataMap(stockCode);
        open = new DataArray(stockCode, Constant.OPEN, dataMap);
        close = new DataArray(stockCode, Constant.CLOSE, dataMap);
        high = new DataArray(stockCode, Constant.HIGH, dataMap);
        low = new DataArray(stockCode, Constant.LOW, dataMap);
        volume = new DataArray(stockCode, Constant.VOL, dataMap);
        dataMap.putDataArray(Constant.OPEN, open);
        dataMap.putDataArray(Constant.HIGH, high);
        dataMap.putDataArray(Constant.LOW, low);
        dataMap.putDataArray(Constant.CLOSE, close);
        dataMap.putDataArray(Constant.VOL, volume);
    }

}
