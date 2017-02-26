package com.stockstrategyanalyzer.job.onestocktest;

import org.springframework.stereotype.Component;

import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobParam;
import com.stockstrategyanalyzer.task.ITask;
import com.stockstrategyanalyzer.task.ITaskFactory;



@Component("OneStockTest")
public class OneStockTestFactory implements ITaskFactory{
	public OneStockTestFactory(){
		
	}

	
	@Override
	public ITask getTask(Job job, JobParam jobParam) {
		return new OneStockTestTask(job, jobParam);
	}
}
