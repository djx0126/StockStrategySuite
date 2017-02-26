package com.stockstrategy.simulator.aggregate;

public abstract class AbstractAggregate {
	private String name;
	private boolean active = true;
	
	protected AbstractAggregate(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void deActive(){
		active = false;
	}
	
	public void active(){
		active = true;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractAggregate other = (AbstractAggregate) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public abstract void update(String stockCode,
            String startDate, String endDate);
	public abstract void reset();
	public abstract Object getResult();
}
