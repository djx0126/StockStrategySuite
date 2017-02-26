package com.stockstrategyanalyzer.job.findtobuy;

import org.springframework.stereotype.Component;

import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobParam;
import com.stockstrategyanalyzer.task.ITask;
import com.stockstrategyanalyzer.task.ITaskFactory;

@Component("FindToBuy")
public class FindToBuyFactory implements ITaskFactory{

	@Override
	public ITask getTask(Job job, JobParam jobParam) {
		return new FindToBuy(job, jobParam);
	}

}
