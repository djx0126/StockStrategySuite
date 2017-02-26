package com.stockstrategyanalyzer.task;

import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobParam;

public interface ITaskFactory {
	public ITask getTask(Job job, JobParam jobParam);
}
