package com.stockstrategyanalyzer.job.findtobuy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stockstrategyanalyzer.result.ITaskResult;
import com.stockstrategyanalyzer.result.ITaskResultFactory;


@Component("FindToBuyResult")
public class FindToBuyResultFactory implements ITaskResultFactory{
	
	@Autowired
	private FindToBuyResultDao findResultDao;

	@Override
	public ITaskResult getResult(long jobId) {
		return findResultDao.loadFindResultByJobId(jobId);
	}

	@Override
	public void saveResult(ITaskResult findResult) {
		findResultDao.saveFindResult((FindResult) findResult);
		
	}

	@Override
	public int deleteResultByJobId(long jobId) {
		return findResultDao.deleteFindResultByJobId(jobId);
	}

}
