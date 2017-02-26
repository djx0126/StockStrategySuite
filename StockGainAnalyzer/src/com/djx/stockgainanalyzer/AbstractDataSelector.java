package com.djx.stockgainanalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.djx.stockgainanalyzer.data.IStockGain;

public abstract class AbstractDataSelector implements IStockSelector{

    protected List<? extends IStockGain> allData;
    protected List<IStockGain> trainDatas;
    protected List<IStockGain> validateDatas;

    protected AbstractDataSelector(List<? extends IStockGain> allData) {
        this.allData = allData;
    }


    abstract public boolean isStockSelected(IStockGain data);

    @Override
    public List<IStockGain> getTrainDataList(){
        if (trainDatas == null){
            trainDatas = new ArrayList<>();
            trainDatas.addAll(allData.stream().filter(data -> isStockSelected(data)).collect(Collectors.toList()));
        }

        return trainDatas;
    }

    @Override
    public List<IStockGain> getValidateDataList(){
        if (validateDatas == null){
            validateDatas = new ArrayList<>();
            validateDatas.addAll(allData.stream().filter(data -> !isStockSelected(data)).collect(Collectors.toList()));
        }
        return validateDatas;
    }

}



 