package com.djx.stockgainanalyzer.data;

import com.djx.stockgainanalyzer.data.Field;

/**
 * Created by dave on 2015/9/29.
 */
public interface ArrayMode {

    double[] serialize(Field[] fields);

}
