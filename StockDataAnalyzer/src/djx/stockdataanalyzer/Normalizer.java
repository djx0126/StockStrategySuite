package djx.stockdataanalyzer;

import djx.stockdataanalyzer.data.StockDataModel;

/**
 * Created by dave on 2015/11/16.
 */
public class Normalizer {

    public static StockDataModel normalizeDataModel(StockDataModel srcDataModel, double[] mean, double[] stdV){

        srcDataModel.setDataArray(normalize(srcDataModel.getDataArray(), mean, stdV));

        return srcDataModel;
    }

    public static double[] normalize(double[] dataArray, double[] mean, double[] stdV){
        double[] normalized = new double[dataArray.length];
        for(int i=0;i< dataArray.length;i++) {
            normalized[i] = (dataArray[i] - mean[i]) /stdV[i];
        }
        return  normalized;
    }

    public static double[] getMean(StockDataModel[] data){
        assert(data.length > 0);

        if (StockDataAnalyzer.debug) {
            System.out.println("Calc for mean");
        }

        double[] mean = new double[data[0].getDataArray().length];
        long size = data.length;
        for (StockDataModel oneData: data){
            double[] values = oneData.getDataArray();
            for (int i=0; i<values.length; i++) {
                mean[i] += values[i];
            }
        }

        for (int i=0; i< mean.length; i++){
            mean[i] = mean[i]/size;
        }
        return mean;
    }

    public static double[] getStdV(StockDataModel[] dataModels, double[] mean){
        assert(dataModels.length > 0);

        if (StockDataAnalyzer.debug) {
            System.out.println("Calc for stdV");
        }

        long size = dataModels.length;
        double[] stdV = new double[dataModels[0].getDataArray().length];

        double[] divSquare = new double[stdV.length];
        for (StockDataModel dataModel: dataModels){
            double[] values = dataModel.getDataArray();
            for (int i=0; i<stdV.length; i++) {
                double temp = values[i] - mean[i];
                divSquare[i] += temp * temp;
            }
        }

        for (int i=0;i<stdV.length;i++) {
            stdV[i] = Math.sqrt(divSquare[i]/(double)size);
        }

        return stdV;
    }

    public static NormalizeInfo buildNormalizeInfo(double[] mean, double[] stdV) {
        return new NormalizeInfo(mean, stdV);
    }

    public static class NormalizeInfo{
        public double[] mean;
        public double[] stdV;

        private NormalizeInfo(double[] mean, double[] stdV) {
            this.mean = mean;
            this.stdV = stdV;
        }
    }

}
