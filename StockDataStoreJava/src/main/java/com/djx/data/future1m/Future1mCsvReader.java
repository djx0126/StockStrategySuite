package com.djx.data.future1m;

import com.djx.data.DataReader;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.http.RawDayDataItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 2017/4/2.
 */
public class Future1mCsvReader implements DataReader {
    @Override
    public List<RawDayDataItem> readData(String stockCode, String startDate, String endDate) {
        String filePath = Constant.DATA_DIR + Constant.SP + stockCode + ".csv";
        File file = new File(filePath);
        List<RawDayDataItem> dataItems = new ArrayList<>();

        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ( (line = br.readLine()) != null ) {
                String[] items = line.split(",");
                if (items.length == 8) {
                    String date = items[0].replace("/", "") + items[1].replace(":", "");
                    if (date.compareTo(startDate) > 0 && date.compareTo(endDate) <= 0) {
                        String open = items[2];
                        String high = items[3];
                        String low = items[4];
                        String close = items[5];
                        String vol = items[6];

                        RawDayDataItem dataItem = new RawDayDataItem();
                        dataItem.put("date", date);
                        dataItem.put(Constant.CLOSE, close);
                        dataItem.put(Constant.OPEN, open);
                        dataItem.put(Constant.HIGH, high);
                        dataItem.put(Constant.LOW, low);
                        dataItem.put(Constant.VOL, vol);

                        dataItems.add(dataItem);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataItems;
    }
}
