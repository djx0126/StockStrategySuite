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
public class JobState {
	public static final int NEW = 0;
	public static final int RUNNING = 1;
	public static final int FINISHED = 2;
	public static final int FAILED = 3;
	
	public static Map<Integer, String> states = new HashMap<Integer, String>();
	
	static{
		states.put(NEW, "NEW");
		states.put(RUNNING, "RUNNING");
		states.put(FINISHED, "FINISHED");
		states.put(FAILED, "FAILED");
	}
	
	@RequestMapping(value="/jobstates", method=RequestMethod.GET)
	public static ResponseEntity<?> getJobTypes(){
		return ResponseHeaderHelper.buildResponseWithCROSHeader(states);
	} 
}
