package com.djx.data.yhzq;

import com.djx.data.DataReader;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.data.DataArray;
import com.stockstrategy.data.DataMap;
import com.stockstrategy.http.RawDayDataItem;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 2016/12/29.
 */
public class DataFileReader implements DataReader{
    public static final int ELEM_SIZE = 32;
    private DataMap dataMap = null;
    private DataArray open = null;
    private DataArray close = null;
    private DataArray high = null;
    private DataArray low = null;
    private DataArray volume = null;

    public DataFileReader() {}

    @Override
    public List<RawDayDataItem> readData(String stockCode, String startDate, String endDate) {

        readFromFile(stockCode, startDate, endDate);
        List<RawDayDataItem> data = new ArrayList<>();

        try {
            for (int i = 0;i< close.size(); i++) {
                RawDayDataItem dataItem = new RawDayDataItem();
                dataItem.put("date", close.getDate(i));
                dataItem.put(Constant.CLOSE, close.getValue(i));
                dataItem.put(Constant.OPEN, open.getValue(i));
                dataItem.put(Constant.HIGH, high.getValue(i));
                dataItem.put(Constant.LOW, low.getValue(i));
                dataItem.put(Constant.VOL, volume.getValue(i));
                data.add(dataItem);
            }
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
        }

        return data;
    }

    private boolean readFromFile(String stockCode, String startDate, String endDate) {
        boolean result = false;
        dataMap = new DataMap(stockCode);
        open = new DataArray(stockCode, Constant.OPEN, dataMap);
        close = new DataArray(stockCode, Constant.CLOSE, dataMap);
        high = new DataArray(stockCode, Constant.HIGH, dataMap);
        low = new DataArray(stockCode, Constant.LOW, dataMap);
        volume = new DataArray(stockCode, Constant.VOL, dataMap);
        BufferedInputStream bis = null;
        try {
            StockLister lister = new StockLister();
            String filePath = lister.getFilePathByStockCode(stockCode);
            bis = new BufferedInputStream(new FileInputStream(filePath));

            if (bis != null) {
                byte[] buffer = new byte[32];

                while (bis.read(buffer) == ELEM_SIZE) {
                    processByteBuffer(buffer, startDate, endDate);
                }
                result = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        dataMap.putDataArray(Constant.OPEN, open);
        dataMap.putDataArray(Constant.HIGH, high);
        dataMap.putDataArray(Constant.LOW, low);
        dataMap.putDataArray(Constant.CLOSE, close);
        dataMap.putDataArray(Constant.VOL, volume);

        return result;
    }

    private void processByteBuffer(byte[] buffer, String startDate, String endDate) {
        long d1, d2, d3, d4, temp_date;
        long o1, o2, o3, o4, temp_open;
        long c1, c2, c3, c4, temp_close;
        long h1, h2, h3, h4, temp_high;
        long l1, l2, l3, l4, temp_low;
        long v1, v2, v3, v4, temp_volume;

        d1 = byteToUnsignedInt(buffer[0]);
        d2 = byteToUnsignedInt(buffer[1]);
        d3 = byteToUnsignedInt(buffer[2]);
        d4 = byteToUnsignedInt(buffer[3]);

        o1 = byteToUnsignedInt(buffer[4]);
        o2 = byteToUnsignedInt(buffer[5]);
        o3 = byteToUnsignedInt(buffer[6]);
        o4 = byteToUnsignedInt(buffer[7]);

        h1 = byteToUnsignedInt(buffer[8]);
        h2 = byteToUnsignedInt(buffer[9]);
        h3 = byteToUnsignedInt(buffer[10]);
        h4 = byteToUnsignedInt(buffer[11]);

        l1 = byteToUnsignedInt(buffer[12]);
        l2 = byteToUnsignedInt(buffer[13]);
        l3 = byteToUnsignedInt(buffer[14]);
        l4 = byteToUnsignedInt(buffer[15]);

        c1 = byteToUnsignedInt(buffer[16]);
        c2 = byteToUnsignedInt(buffer[17]);
        c3 = byteToUnsignedInt(buffer[18]);
        c4 = byteToUnsignedInt(buffer[19]);

        v1 = byteToUnsignedInt(buffer[24]);
        v2 = byteToUnsignedInt(buffer[25]);
        v3 = byteToUnsignedInt(buffer[26]);
        v4 = byteToUnsignedInt(buffer[27]);

        temp_date = d1;
        temp_date += d2 * 256;
        temp_date += d3 * 256 * 256;
        temp_date += d4 * 256 * 256 * 256;
        // Log.d("dateData", String.valueOf(temp_date));

        temp_open = o1;
        temp_open += o2 * 256;
        temp_open += o3 * 256 * 256;
        temp_open += o4 * 256 * 256 * 256;
        // Log.d("openData", String.valueOf(temp_open));

        temp_high = h1;
        temp_high += h2 * 256;
        temp_high += h3 * 256 * 256;
        temp_high += h4 * 256 * 256 * 256;
        // Log.d("highData", String.valueOf(temp_high));

        temp_low = l1;
        temp_low += l2 * 256;
        temp_low += l3 * 256 * 256;
        temp_low += l4 * 256 * 256 * 256;
        // Log.d("low", String.valueOf(temp_low));

        temp_close = c1;
        temp_close += c2 * 256;
        temp_close += c3 * 256 * 256;
        temp_close += c4 * 256 * 256 * 256;
        // .d("closeData", String.valueOf(temp_close));

        temp_volume = v1;
        temp_volume += v2 * 256;
        temp_volume += v3 * 256 * 256;
        temp_volume += v4 * 256 * 256 * 256;
        // Log.d("volumeData", String.valueOf(temp_volume));

        if (startDate != null && !startDate.equals("-") && !startDate.equals("")) {
            if (String.valueOf(temp_date).compareTo(startDate) < 0) {
                return;
            }
        }

        if (String.valueOf(temp_date).compareTo(endDate) > 0) {
            return;
        }

        open.addRawData(String.valueOf(temp_date), (double) temp_open / 100);
        high.addRawData(String.valueOf(temp_date), (double) temp_high / 100);
        low.addRawData(String.valueOf(temp_date), (double) temp_low / 100);
        close.addRawData(String.valueOf(temp_date), (double) temp_close / 100);
        volume.addRawData(String.valueOf(temp_date), (double) temp_volume / 100);
    }

    protected static int byteToUnsignedInt(byte b) {
        int temp = b;
        if (temp < 0) {
            temp += 256;
        }
        return temp;
    }
}
