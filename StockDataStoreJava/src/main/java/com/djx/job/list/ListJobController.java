package com.djx.job.list;

import com.djx.data.DataLister;
import com.djx.data.DataReader;
import com.djx.data.future1m.Future1mCsvReader;
import com.djx.data.future1m.FutureLister;
import com.djx.data.yhzq.DataFileReader;
import com.djx.data.yhzq.StockLister;
import com.stockstrategy.http.RawDayDataItem;
import com.stockstrategy.http.StockListItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dave on 2016/11/21.
 */
@RestController
@RequestMapping("/StockDataStore")
public class ListJobController {
    public static String DATA_SOURCE_YHZQ = "yhzq";
    public static String DATA_SOURCE_FUTURE_1M_TEST = "future_1m";
    public static String DATA_SOURCE = DATA_SOURCE_YHZQ;

    public static final String LIST_PATH = "/list";

    public static final String SHARED_LIST_PATH = "/shared_list";

    public static final String DATA_PATH = "/data";

    @RequestMapping(value=LIST_PATH, method= RequestMethod.GET)
    public ResponseEntity<List<StockListItem>> readList(){
        System.out.println("create a new job to list ");
        DataLister lister = new StockLister();
        if (DATA_SOURCE.equals(DATA_SOURCE_YHZQ)) {
            lister = new StockLister();
        } else if (DATA_SOURCE.equals(DATA_SOURCE_FUTURE_1M_TEST)) {
            lister = new FutureLister();
        }
        return new ResponseEntity<>(lister.getStockList(), HttpStatus.OK);
    }

    @RequestMapping(value=SHARED_LIST_PATH, method= RequestMethod.GET)
    public ResponseEntity<List<StockListItem>> readSharedList(){
        System.out.println("create a new job to shared list ");
        DataLister lister = new StockLister();
        if (DATA_SOURCE.equals(DATA_SOURCE_YHZQ)) {
            lister = new StockLister();
        } else if (DATA_SOURCE.equals(DATA_SOURCE_FUTURE_1M_TEST)) {
            lister = new FutureLister();
        }
        return new ResponseEntity<>(lister.getSharedStockList(), HttpStatus.OK);
    }

    @RequestMapping(value=DATA_PATH, method= RequestMethod.GET)
    public ResponseEntity<List<RawDayDataItem>> readData(@RequestParam String code, @RequestParam String start, @RequestParam String end){
        System.out.println("create a new job to read data for code: " + code + ", between [" + start + "," + end + "]");
        DataReader dataReader = new DataFileReader();
        if (DATA_SOURCE.equals(DATA_SOURCE_YHZQ)) {
            dataReader = new DataFileReader();
        } else if (DATA_SOURCE.equals(DATA_SOURCE_FUTURE_1M_TEST)) {
            dataReader = new Future1mCsvReader();
        }
        return new ResponseEntity<>(dataReader.readData(code, start, end), HttpStatus.OK);
    }
}
