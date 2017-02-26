package com.stockstrategyanalyzer.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHeaderHelper {
	public static ResponseEntity<?> buildResponseWithCROSHeader(Object data){
		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.set("Access-Control-Allow-Origin", "*");
		//responseHeaders.set("Access-Control-Allow-Headers", "X-Requested-With");
//		responseHeaders.set("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
//		responseHeaders.set("X-Powered-By","3.2.1");
		
		ResponseEntity<?> resp = new ResponseEntity<>(data, responseHeaders, HttpStatus.OK);
		return resp;
	}
}
