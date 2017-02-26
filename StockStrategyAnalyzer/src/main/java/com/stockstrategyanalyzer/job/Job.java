package com.stockstrategyanalyzer.job;



public class Job {
	private final long id;
	private int state = JobState.NEW;
	private int type = JobType.TEST;
	private String creationDate;
	
	private JobParam param;
	
	

	protected Job(long id){
		this.id = id;
	}
	
	public long getId() {
		return id;
	}


	public int getState() {
		return state;
	}
	
	public void setState(int newState) {
		this.state = newState;
		
	}


	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public void setParam(JobParam param) {
		this.param = param;
	}

	public JobParam getParam() {
		return param;
	}
}
