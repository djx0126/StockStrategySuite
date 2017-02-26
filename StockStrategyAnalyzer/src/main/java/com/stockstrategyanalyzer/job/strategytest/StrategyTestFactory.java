package com.stockstrategyanalyzer.job.strategytest;

import org.springframework.stereotype.Component;

import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobParam;
import com.stockstrategyanalyzer.task.ITask;
import com.stockstrategyanalyzer.task.ITaskFactory;

@Component("StrategyTest")
public class StrategyTestFactory  implements ITaskFactory{

	@Override
	public ITask getTask(Job job, JobParam jobParam) {
		return new StrategyTest(job, jobParam);
	}
	
}
