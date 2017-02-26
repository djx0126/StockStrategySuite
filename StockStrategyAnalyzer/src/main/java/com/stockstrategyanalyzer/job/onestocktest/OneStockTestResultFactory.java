package com.stockstrategyanalyzer.job.onestocktest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stockstrategyanalyzer.result.ITaskResult;
import com.stockstrategyanalyzer.result.ITaskResultFactory;

@Component("OneStockTestResult")
public class OneStockTestResultFactory implements ITaskResultFactory{

	@Autowired
	private OneStockTestResultDao resultDao;
	
	@Override
	public ITaskResult getResult(long jobId) {
		return resultDao.getResultByJobId(jobId);
	}

	@Override
	public void saveResult(ITaskResult result) {
		OneStockTestResult taskResult = (OneStockTestResult)result;
		resultDao.saveResult(taskResult);
	}

	@Override
	public int deleteResultByJobId(long jobId) {
		return resultDao.deleteResultByJobId(jobId);
	}
	
	
}
