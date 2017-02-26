package com.stockstrategyanalyzer;

import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.statistic.data.ConfigBasedStrategyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.stockstrategy.constant.Configurer;
import com.stockstrategy.constant.Constant;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		ArgParser.loadInitConfigures(args, Constant.class);

		ConfigBasedStrategyFactory.loadStrategyProperties();
		SpringApplication.run(Application.class, args);
	}
}
