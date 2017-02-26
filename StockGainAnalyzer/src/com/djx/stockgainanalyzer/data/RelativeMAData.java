package com.djx.stockgainanalyzer.data;

import com.djx.stockgainanalyzer.Utils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by dave on 2015/10/7.
 */
public class RelativeMAData implements ArrayMode{
    private double[] values;
    public int[] maDaysMapping; // initialized with {-1, -1, -1, 0, -1, 1} for fields {ma3, ma5}
                                       //                    0   1   2  3   4  5

    public static int[] buildMAFieldsMapping(MAField[] maFields){
        int[] maList = Arrays.stream(maFields).mapToInt(f->f.getMaDays()).toArray();//{5, 10, 20, 30};
        int maxMADays = maList.length > 0 ? Arrays.stream(maList).max().getAsInt(): 0;
        int[] maDaysMapping = new int[maxMADays+1];
        for (int i=0, maListIdx=0;i<maDaysMapping.length;i++){
            if (maListIdx < maList.length && i == maList[maListIdx]) {
                maDaysMapping[i] = maListIdx;
                maListIdx++;
            } else {
                maDaysMapping[i] = -1;
            }
        }
        return maDaysMapping;
    }

    public RelativeMAData(MAField[] maFields){
        this.maDaysMapping = buildMAFieldsMapping(maFields);
        this.values = new double[maFields.length];
    }

    public RelativeMAData(MAField[] maFields, int[] maDaysMapping){
        this.maDaysMapping = maDaysMapping;
        this.values = new double[maFields.length];
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("{");
        for(int i=0;i<values.length;i++){
            sb.append("ma"+i+":"+ Utils.doubleToStr(values[i])+",");
        }
        if (values.length > 0){
            sb = sb.deleteCharAt(sb.length()-1);
        }
        return sb.append("}").toString();
    }

    public double getValue(Field field) {
        MAField maField = (MAField)field;
        int maDays = maField.getMaDays();
        int idx = maDaysMapping[maDays];
        return values[idx];
    }

    public void setValue(Field field, double value) {
        MAField maField = (MAField)field;
        int maDays = maField.getMaDays();
        int idx = maDaysMapping[maDays];
        values[idx] = value;
    }

    @Override
    public double[] serialize(Field[] fields) {
        return Arrays.copyOf(values, values.length);
    }

}
