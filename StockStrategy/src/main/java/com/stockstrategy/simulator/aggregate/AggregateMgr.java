package com.stockstrategy.simulator.aggregate;

import java.util.ArrayList;
import java.util.List;

public class AggregateMgr {
	private static AggregateMgr aggregateMgr= null;
	List<AbstractAggregate> aggregates;
	
	public void addAggregate(AbstractAggregate aggregate){
		if (!aggregates.contains(aggregate)){
			aggregates.add(aggregate);
		}
	}
	
	public synchronized void updateAggregates(String stockCode, String startDate, String endDate){
		for (AbstractAggregate aggregate: aggregates){
			if (aggregate.isActive()){
				aggregate.update(stockCode, startDate, endDate);
			}
		}
	}
	
	public AbstractAggregate getAggregate(String name){
		for (AbstractAggregate aggregate: aggregates){
			if (aggregate.getName().equals(name)){
				return aggregate;
			}
		}
		return null;
	}
	
	public AbstractAggregate getAggregate(AbstractAggregate counter) {
		for (AbstractAggregate aggregate: aggregates){
			if (aggregate==counter){
				return aggregate;
			}
		}
		return null;
	}
	
	public List<AbstractAggregate> getAggregates(){
		return aggregates;
	}
	
	public void releaseAggregates(){
		aggregates.clear();
	}
	
	public void releaseAggregate(AbstractAggregate counter){
		if (aggregates.contains(counter)){
			aggregates.remove(counter);
		}
	}
	
	public void resetAggregates(){
		for (AbstractAggregate aggregate: aggregates){
			aggregate.reset();
		}
	}
	
	private AggregateMgr(){
		aggregates = new ArrayList<>();
	}
	
	public static AggregateMgr getInstance(){
		if(aggregateMgr==null){
			aggregateMgr = new AggregateMgr();
		}
		return aggregateMgr;
	}

	

	
	
	
}
