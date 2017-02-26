package com.stockstrategyanalyzer.job;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stockstrategyanalyzer.http.ResponseHeaderHelper;

@RestController
@RequestMapping("/StockStrategyAnalyzer")
public class JobType {
	public static final int TEST = -1;
	public static final int ONE_STOCK_TEST = 1;
	public static final int FIND_TO_BUY = 2;
	public static final int STRATEGY_TEST = 3;
	
	private static Map<Integer, String> types = new HashMap<Integer, String>();
	
	static{
		types.put(TEST, "TEST");
		types.put(ONE_STOCK_TEST, "OneStockTest");
		types.put(FIND_TO_BUY, "FindToBuy");
		types.put(STRATEGY_TEST, "StrategyTest");
	}

	@RequestMapping(value="/jobtypes", method=RequestMethod.GET)
	public static ResponseEntity<?> getJobTypes(){
		return ResponseHeaderHelper.buildResponseWithCROSHeader(types);
	}
	
	public static String getJobTypeString(int type){
		return types.get(type);
	}
}
