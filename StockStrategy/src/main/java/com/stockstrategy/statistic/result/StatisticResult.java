/**
 * 
 */
package com.stockstrategy.statistic.result;

/**
 * @author Administrator
 * a result of (one stock's) statistic data of one kind of statistic type
 */
public class StatisticResult {
	private float gain = 0f;
	private float netGain = 0f;
	private float gainDelay = 0f;
	private float lost = 0f;
	private float lostDelay = 0f;
	private float stepGain = 0f;
	private float stepLost = 0f;
	
	
	public float getNetGain() {
		return netGain;
	}

	public void setNetGain(float netGain) {
		this.netGain = netGain;
	}
	
	public float getGain() {
		return gain;
	}

	public void setGain(float gain) {
		this.gain = gain;
	}

	public float getGainDelay() {
		return gainDelay;
	}

	public void setGainDelay(float gainDelay) {
		this.gainDelay = gainDelay;
	}

	public float getLost() {
		return lost;
	}

	public void setLost(float lost) {
		this.lost = lost;
	}

	public float getLostDelay() {
		return lostDelay;
	}

	public void setLostDelay(float lostDelay) {
		this.lostDelay = lostDelay;
	}

	public float getStepGain() {
		return stepGain;
	}

	public void setStepGain(float stepGain) {
		this.stepGain = stepGain;
	}

	public float getStepLost() {
		return stepLost;
	}

	public void setStepLost(float stepLost) {
		this.stepLost = stepLost;
	}

}
