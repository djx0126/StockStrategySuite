package djx.stockdataanalyzer.data;

import com.djx.stockgainanalyzer.Utils;

/**
 * Created by dave on 2015/11/15.
 */
public class ResultModel {
    public double[] offsets;
    public double[] scales;

    public ResultModel(double[] offsets, double[] scales) {
        this.offsets = offsets==null? null : Utils.cloneArray(offsets);
        this.scales = scales==null? null : Utils.cloneArray(scales);
    }

    public ResultModel(ResultModel src) {
        this.offsets = src==null? null : Utils.cloneArray(src.offsets);
        this.scales = src==null? null : Utils.cloneArray(src.scales);
    }

    public boolean testOnData(StockDataModel data) {
        double limit = offsets.length;

        double[] dataValue = data.getDataArray();
        double value = 0.0f;

        for (int j=0;j<offsets.length;j++){
            if (value<=limit){
                double temp = dataValue[j]+offsets[j];
                value += (temp*temp)/(scales[j]*scales[j]);
            }
        }

        return value <= limit;
    }

    @Override
    public String toString() {
        return Utils.getArrayString("offsets", offsets) + "\n" +
                Utils.getArrayString("scales", scales);
    }
}
