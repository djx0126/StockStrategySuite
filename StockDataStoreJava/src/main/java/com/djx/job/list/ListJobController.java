package com.djx.job.list;

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

    public static final String LIST_PATH = "/list";

    public static final String SHARED_LIST_PATH = "/shared_list";

    public static final String DATA_PATH = "/data";

    @RequestMapping(value=LIST_PATH, method= RequestMethod.GET)
    public ResponseEntity<List<StockListItem>> readList(){
        System.out.println("create a new job to list ");
        StockLister lister = new StockLister();
        return new ResponseEntity<>(lister.getStockList(), HttpStatus.OK);
    }

    @RequestMapping(value=SHARED_LIST_PATH, method= RequestMethod.GET)
    public ResponseEntity<List<StockListItem>> readSharedList(){
        System.out.println("create a new job to shared list ");
        StockLister lister = new StockLister();
        return new ResponseEntity<>(lister.getSharedStockList(), HttpStatus.OK);
    }

    @RequestMapping(value=DATA_PATH, method= RequestMethod.GET)
    public ResponseEntity<List<RawDayDataItem>> readData(@RequestParam String code, @RequestParam String start, @RequestParam String end){
        System.out.println("create a new job to read data for code: " + code + ", between [" + start + "," + end + "]");
        DataFileReader dataReader = new DataFileReader(code, start, end);
        return new ResponseEntity<>(dataReader.readData(), HttpStatus.OK);
    }
}
