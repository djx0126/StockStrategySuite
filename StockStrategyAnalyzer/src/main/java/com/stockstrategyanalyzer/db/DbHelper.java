package com.stockstrategyanalyzer.db;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;


@Component


public class DbHelper {
	@Bean
	@ConfigurationProperties(prefix="db")
	public DataSource getDataSource(){
		DriverManagerDataSource ds = new DriverManagerDataSource();
		return ds;
	}
	
//	@Bean
//	public JdbcTemplate getJdbcTemplate(DataSource dataSource){
//		return new JdbcTemplate(dataSource);
//	}
}
