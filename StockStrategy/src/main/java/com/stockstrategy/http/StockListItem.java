package com.stockstrategy.http;

import java.io.Serializable;

/**
 * Created by dave on 2016/12/9.
 */
public class StockListItem implements Serializable{
    // 600123
    private String code;

    // the code for composite index related to the code
    // 60[0|1]xxx -> 999999
    // 00[0|2]xxx -> 399001
    // 300xxx     -> 399006
    private String compositeIndexCode; // the code for composite index related to the code

    public StockListItem() {
    }

    public StockListItem(String code) {
        this(code, null);
    }

    public StockListItem(String code, String compositeIndexCode) {
        this.code = code;
        this.compositeIndexCode = compositeIndexCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompositeIndexCode() {
        return compositeIndexCode;
    }

    public void setCompositeIndexCode(String compositeIndexCode) {
        this.compositeIndexCode = compositeIndexCode;
    }

    @Override
    public String toString() {
        return "StockListItem{" +
                "code='" + code + '\'' +
                ", compositeIndexCode='" + compositeIndexCode + '\'' +
                '}';
    }
}
