package com.stockstrategyanalyzer.result;

public interface ITaskResultFactory {
	public ITaskResult getResult(long jobId);
	public void saveResult(ITaskResult result);
	public int deleteResultByJobId(long jobId);
}
