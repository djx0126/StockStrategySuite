package com.stockstrategyanalyzer.result;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stockstrategyanalyzer.http.ResponseHeaderHelper;
import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobDao;
import com.stockstrategyanalyzer.job.JobType;


@RestController
@RequestMapping("/StockStrategyAnalyzer/result")
public class ResultController {
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping(value="/{jobId}")
	public ResponseEntity<?> getResultByJobId(@PathVariable long jobId, @RequestParam(required=false) Map<String, Object> options){
		Job job = jobDao.getJob(jobId);
		String jobType = JobType.getJobTypeString(job.getType());
		String resultType = jobType + "Result";
		ITaskResultFactory taskResultFactory = (ITaskResultFactory) context.getBean(resultType);
		return ResponseHeaderHelper.buildResponseWithCROSHeader(taskResultFactory.getResult(jobId));
	}
	
	@RequestMapping(value="/{jobId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteResultByJobId(@PathVariable long jobId){
		Job job = jobDao.getJob(jobId);
		String jobType = JobType.getJobTypeString(job.getType());
		String resultType = jobType + "Result";
		ITaskResultFactory taskResultFactory = (ITaskResultFactory) context.getBean(resultType);
		int lines = taskResultFactory.deleteResultByJobId(jobId);
		System.out.println("Result of job "+jobId+" deleted");
		return ResponseHeaderHelper.buildResponseWithCROSHeader(lines);
	}
	


}
