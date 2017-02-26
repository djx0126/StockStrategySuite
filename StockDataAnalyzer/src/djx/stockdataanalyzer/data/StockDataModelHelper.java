package djx.stockdataanalyzer.data;

import com.djx.stockgainanalyzer.data.Field;
import com.djx.stockgainanalyzer.data.PreviousData;
import com.djx.stockgainanalyzer.data.RelativeMAData;
import com.djx.stockgainanalyzer.data.StockGainData;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dave on 2015/11/11.
 */
public class StockDataModelHelper {

    public StockDataModel buildStockDataModel(FieldModel fieldModel, StockGainData data) {
        double[][] dayFieldValues = buildDayFieldValues(fieldModel.daysFields, data.getPreviousData());
        double[] maFieldValues = buildMaFieldValues(fieldModel.maFields, data.getRelativeMA());
        double[] overAllmaFieldValues = buildMaFieldValues(fieldModel.overAllMaFields, data.getRelativeOverAllMAData());
        double[] dataArray = buildDataArray(fieldModel, dayFieldValues, maFieldValues, overAllmaFieldValues);
        return new StockDataModel(data.getKeyDate(), data.getStockCode(), data.getGain(), dataArray);
    }

    private double[][] buildDayFieldValues(int[] dayFields, List<PreviousData> preDatas){
        double[][] dayFieldValues = new double[dayFields.length][];

        for(int i=0;i < dayFields.length;i++) {
            Field field = Field.fields[i];
            dayFieldValues[i] = new double[dayFields[i]];
            for (int j=0;j< dayFieldValues[i].length; j++){
                PreviousData preData = preDatas.get(j);
                dayFieldValues[i][j] = preData.getValue(field);
            }
        }
        return dayFieldValues;
    }

    private double[] buildMaFieldValues(Field[] maFields, RelativeMAData maData){
        double[] maFieldValues = new double[maFields.length];
        for (int i=0;i < maFieldValues.length;i++) {
            maFieldValues[i] = maData.getValue(maFields[i]);
        }
        return maFieldValues;
    }

    private double[] buildDataArray(FieldModel fieldModel, double[][] dayFieldValues, double[] maFieldValues, double[] overAllmaFieldValues) {
        int dayFieldsSum = Arrays.stream(fieldModel.daysFields).sum();
        double[] dataArray = new double[dayFieldsSum + fieldModel.maFields.length + fieldModel.overAllMaFields.length];

        int index = 0;

        for(int i=0;i<dayFieldValues.length;i++) {
            for (int j=0; j< dayFieldValues[i].length;j++){
                dataArray[index++] = dayFieldValues[i][j];
            }
        }

        for(int i=0;i<fieldModel.maFields.length;i++) {
            dataArray[index++] = maFieldValues[i];
        }

        for(int i=0;i<fieldModel.overAllMaFields.length;i++) {
            dataArray[index++] = overAllmaFieldValues[i];
        }

        return dataArray;
    }
}
