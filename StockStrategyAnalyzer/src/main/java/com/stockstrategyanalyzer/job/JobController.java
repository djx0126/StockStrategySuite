package com.stockstrategyanalyzer.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stockstrategyanalyzer.http.ResponseHeaderHelper;
import com.stockstrategyanalyzer.result.ITaskResult;
import com.stockstrategyanalyzer.result.ITaskResultFactory;
import com.stockstrategyanalyzer.task.ITask;
import com.stockstrategyanalyzer.task.ITaskFactory;


@RestController
@RequestMapping("/StockStrategyAnalyzer")
public class JobController {
	
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping(value="/job", method=RequestMethod.POST)
	public ResponseEntity<?> createJob(@RequestParam("type") int type, @RequestParam(required=false) String startDate, @RequestParam String endDate, @RequestParam(required=false) String strategy, @RequestParam(required=false) String stockCode){
		System.out.println("create a new job with type: "+type);
		
		JobParam jobParam = new JobParam();
		jobParam.setStartDate(startDate).setEndDate(endDate).setStrategy(strategy).setStockCode(stockCode);
		Job job = jobDao.createJob(type, jobParam);
		
		String jobType = JobType.getJobTypeString(job.getType());
		ITaskFactory taskFactory = (ITaskFactory) context.getBean(jobType);
		ITask task = taskFactory.getTask(job, jobParam);
		
		new Thread(() -> {
            try{
                jobDao.setJobState(job.getId(), JobState.RUNNING);
                ITaskResult result = task.runTask();

                String resultType = jobType + "Result";
                ITaskResultFactory taskResultFactory = (ITaskResultFactory) context.getBean(resultType);
                taskResultFactory.saveResult(result);
                jobDao.setJobState(job.getId(), JobState.FINISHED);
            }catch(Exception e){
				System.err.println(e);
                jobDao.setJobState(job.getId(), JobState.FAILED);
            }

        }).start();
		
		return ResponseHeaderHelper.buildResponseWithCROSHeader(job);
		
	}
	
	@RequestMapping(value="/job/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteJobById(@PathVariable int id){
		int jobDeleted = jobDao.deleteJob(id);
		System.out.println("delete a job with id: "+id+" done.");
		return ResponseHeaderHelper.buildResponseWithCROSHeader(jobDeleted);
	}
	
	@RequestMapping(value="/job/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getJobById(@PathVariable int id){
		System.out.println("get a job with id: "+id);
		Job job = jobDao.getJob(id);
		return ResponseHeaderHelper.buildResponseWithCROSHeader(job);
	}
	
	@RequestMapping(value="/jobs", method = RequestMethod.GET)
	public ResponseEntity<?> getAllJobs(){
//		System.out.println("get all jobs");
		List<Job> jobs = jobDao.getJobs();
		return ResponseHeaderHelper.buildResponseWithCROSHeader(jobs);
	}
}
