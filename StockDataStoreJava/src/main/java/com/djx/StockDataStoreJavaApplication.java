package com.djx;

import com.djx.job.list.ListJobController;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockDataStoreJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockDataStoreJavaApplication.class, args);
		ArgParser.loadInitConfigures(args, Constant.class);
		ArgParser.loadInitConfigures(args, ListJobController.class);
	}
}
