package com.djx.stockgainanalyzer.data;

public class PreviousData implements ArrayMode{
	private int stockDateKey;
    private int offset;
    private double open;
    private double close;
    private double closeOpen;
    private double high;
    private double low;
    private double vol;
    private String date;

    public PreviousData(){
    }
    
    public PreviousData(double close, double vol, double closeOpen){
    	this.setClose(close);
    	this.setVol(vol);
    	this.setCloseOpen(closeOpen);
    }

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder("{");
		sb.append("stockDateKey").append(":").append(stockDateKey).append(", ");
		sb.append("offset").append(":").append(offset).append(", ");
		sb.append("close").append(":").append(this.getClose()).append(", ");
//		sb.append("open").append(":").append(open).append(", ");
		sb.append("closeOpen").append(":").append(this.getCloseOpen()).append(", ");
//		sb.append("high").append(":").append(high).append(", ");
//		sb.append("low").append(":").append(low).append(", ");
		sb.append("vol").append(":").append(this.getVol());
    	return sb.append("}").toString();
    }

    public int getStockDateKey() {
        return stockDateKey;
    }

    public void setStockDateKey(int stockDateKey) {
        this.stockDateKey = stockDateKey;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double price) {
        this.close = price;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }
    public double getCloseOpen() {
        return closeOpen;
    }

    public void setCloseOpen(double closeOpen) {
        this.closeOpen = closeOpen;
    }

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

    public void setValue(Field field, double value){
        switch (field.getIdx()){
            case 0:
                this.close = value;
                break;
            case 1:
                this.open = value;
                break;
            case 2:
                this.high = value;
                break;
            case 3:
                this.low = value;
                break;
            case 4:
                this.vol = value;
                break;
            default:
                break;
        }
    }

    public double getValue(Field field){
        double value = 0.0f;
        switch (field.getIdx()){
            case 0:
                value = this.close;
                break;
            case 1:
                value = this.open;
                break;
            case 2:
                value = this.high;
                break;
            case 3:
                value = this.low;
                break;
            case 4:
                value = this.vol;
                break;
            default:
                break;
        }
        return value;
    }

    @Override
    public double[] serialize(Field[] fields){
        double[] array = new double[fields.length];
        int i=0;
        for(Field field: fields){
            array[i++] = this.getValue(field);
        }
        return array;
    }

//    public void deserialize(double[] array, Field[] fields){
//        int i=0;
//        for(Field field: fields){
//            this.setValue(field, array[i++]);
//        }
//    }
}
