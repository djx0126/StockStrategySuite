package com.djx.data.future1m;

import com.djx.data.DataLister;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.http.StockListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dave on 2017/4/2.
 */
public class FutureLister implements DataLister {

    @Override
    public List<StockListItem> getSharedStockList() {
        return new ArrayList<>();
    }

    @Override
    public List<StockListItem> getStockList() {
        File dir = new File(Constant.DATA_DIR);
        String[] files = dir.list();
        return Arrays.stream(files).map(s -> s.replace(".csv", ""))
                .map(s -> new StockListItem(s)).collect(Collectors.toList());
    }
}
