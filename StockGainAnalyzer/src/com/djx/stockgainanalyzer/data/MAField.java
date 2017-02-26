package com.djx.stockgainanalyzer.data;

/**
 * Created by dave on 2015/10/7.
 */
public class MAField extends Field{
    private int maDays;
    public MAField(int idx, String name, int maDays) {
        super(idx, name);
        this.maDays = maDays;
    }
    public int getMaDays(){
        return this.maDays;
    }
}
