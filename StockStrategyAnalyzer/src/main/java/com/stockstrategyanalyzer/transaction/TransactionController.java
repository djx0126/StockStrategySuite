package com.stockstrategyanalyzer.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stockstrategy.simulator.aggregate.BuySellDetailCollector.Transaction;

@RestController
@RequestMapping("/StockStrategyAnalyzer")
public class TransactionController {
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping(value="/transactions", method = RequestMethod.GET)
	public List<Transaction> getResultByJobId(@RequestParam long jobId, @RequestParam String strategy){
		return transactionDao.getTransactions(jobId, strategy);
	}
}
