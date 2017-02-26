package com.djx.stockgainanalyzer.data;

public class Field {
	public static int LENGTH = 5;
	public static Field close = new Field(0, "close");
	public static Field open = new Field(1, "open");
	public static Field high = new Field(2, "high");
	public static Field low = new Field(3, "low");
	public static Field vol = new Field(4, "vol");
	public static MAField ma3 = new MAField(5, "ma3", 3);
	public static MAField ma5 = new MAField(6, "ma5", 5);
	public static MAField ma10 = new MAField(7, "ma10", 10);
	public static MAField ma20 = new MAField(8, "ma20", 20);
	public static MAField ma30 = new MAField(9, "ma30", 30);
	public static MAField ma60 = new MAField(10, "ma60", 60);
	public static MAField ma120 = new MAField(11, "ma120", 120);
	public static MAField ma200 = new MAField(12, "ma200", 200);

	public static Field[] dayFields = {close, open, high, low, vol};
	public static Field[] fields = {close, open, high, low, vol, ma3, ma5, ma10, ma20, ma30, ma60, ma120, ma200};
	
	
	private int idx = 0;
	private String name = "";
	
	public Field(int idx, String name){
		this.idx = idx;
		this.name = name;
	}
	
	public int getIdx() {
		return idx;
	}

	public String getName() {
		return name;
	}
}
