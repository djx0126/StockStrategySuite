package com.stockstrategyanalyzer.job.strategytest;

import com.stockstrategyanalyzer.transaction.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stockstrategyanalyzer.result.ITaskResult;
import com.stockstrategyanalyzer.result.ITaskResultFactory;

@Component("StrategyTestResult")
public class StrategyTestResultFactory  implements ITaskResultFactory{
	@Autowired
	private StrategyTestResultDao strategyTestResultDao;

	@Override
	public ITaskResult getResult(long jobId) {
		return strategyTestResultDao.loadStrategyTestResultByJobId(jobId);
	}

	@Override
	public void saveResult(ITaskResult result) {
		strategyTestResultDao.saveStrategyTestResult((StrategyTestResult) result);
		
	}

	@Override
	public int deleteResultByJobId(long jobId) {

		return strategyTestResultDao.deleteStrategyTestResultByJobId(jobId);
	}

}
