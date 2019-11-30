package djx.stockdataanalyzer.data;

import com.djx.stockgainanalyzer.Utils;
import com.djx.stockgainanalyzer.data.IStockGain;

import java.util.Arrays;

/**
 * Created by dave on 2015/11/12.
 */
public class StockDataModel implements IStockGain {
    double[] dataArray; // c,c,c,o,o,h,v,ma5,ma20  -> 3,2,1,0,1  5,20 // for each value = C(k)/C(k-1)
    double gain;
    double percentageGain;
    String stockCode;
    String keyDate;
    public StockDataModel(String keyDate, String stockCode, double gain, double[] dataArray) {
        this.keyDate = keyDate;
        this.stockCode = stockCode;
        this.gain = gain;
        this.percentageGain = Utils.adjustSmallGain(gain);
        this.dataArray = dataArray;
    }

    public StockDataModel(StockDataModel dataModel) {
        this.keyDate = dataModel.getKeyDate();
        this.stockCode = dataModel.getStockCode();
        this.gain = dataModel.getGain();
        this.percentageGain = Utils.adjustSmallGain(gain);
        this.dataArray = Utils.cloneArray(dataArray);
    }

    @Override
    public String toString() {
        return keyDate+"/"+stockCode+":"+percentageGain+" ("+ Arrays.toString(dataArray)+")";
    }

    @Override
    public String getKeyDate() {
        return keyDate;
    }

    @Override
    public String getStockCode() {
        return stockCode;
    }

    public double getGain() {
        return gain;
    }

    public double getPercentageGain(){
        return percentageGain;
    }

    public double[] getDataArray() {
        return dataArray;
    }

    public void setDataArray(double[] array){
        this.dataArray = array;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }
}
