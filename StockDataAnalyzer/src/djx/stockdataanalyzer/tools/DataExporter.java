package djx.stockdataanalyzer.tools;

import com.djx.stockgainanalyzer.FileHelper;
import com.djx.stockgainanalyzer.StrategyHelper;
import com.djx.stockgainanalyzer.Utils;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.CollectDataStrategy;
import djx.stockdataanalyzer.Normalizer;
import djx.stockdataanalyzer.StatisticResultCalculator;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.data.*;
import djx.stockdataanalyzer.learner.ILeaner;
import djx.stockdataanalyzer.learner.IterativeLearner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dave on 2016/4/6.
 */
public class DataExporter {

    /*model parameters*/
    public static final int PRE = 15;
    public static final int GAIN = 2;

    public static final int[] dayFields = {/*close*/15, /*open*/15, /*high*/15, /*low*/15, /*vol*/15};
    public static final int[] maFields = {5, 10, 20, 30, 60}; //{5, 10, 20, 30};
    public static final int[] overAllmaFields = {5, 10, 20, 30, 60}; //{5, 10, 20, 30};

    /*calc parameters*/
    public static boolean NORMALIZE = true;
    public static boolean usingPreFilter = true;
    public static float preFilterRate = 0.1f;

    static {
        StockDataAnalyzer.PRE = PRE;
        StockDataAnalyzer.GAIN = GAIN;
        StockDataAnalyzer.dayFields = dayFields;
        StockDataAnalyzer.maFields = maFields;
        StockDataAnalyzer.NORMALIZE = NORMALIZE;
        StockDataAnalyzer.usingPreFilter = usingPreFilter;
        StockDataAnalyzer.preFilterRate = preFilterRate;
        StockDataAnalyzer.fieldModel = new FieldModel(dayFields, maFields, overAllmaFields);
    }

    private static float TARGET = 3.0f;
    private static String fileName = "raw_data_pre15_gain2_20150101_20160101.txt";

    public static List<StockDataModel> dataList = new ArrayList<>();
    public static StockDataModel[] rawData;

    public static void main(String[] args){
        System.out.println("Start");
        long startTime = System.currentTimeMillis();
        ArgParser.loadInitConfigures(args, Constant.class);

        StrategyHelper.generateData(CollectDataStrategy.NAME);
        DataExporter.dataList = StockDataAnalyzer.dataList.stream().sorted((o1, o2) -> o1.getKeyDate().compareTo(o2.getKeyDate())).collect(Collectors.toList());
        rawData = new StockDataModel[dataList.size()];

        dataList.toArray(rawData);

        System.out.println("Total " + dataList.size() + " records loaded.");
        if (dataList.size()<=0){
            return;
        }

        System.out.println("pre="+PRE);
        System.out.println("gain=" + GAIN);
        System.out.println(Utils.getFieldArrayDefString("dayFields", dayFields));
        System.out.println(Utils.getFieldArrayDefString("maFields", maFields));


        transformValueToLog();
        normalizeData(dataList);
        transformOutput();

        for(StockDataModel dataModel: DataExporter.dataList) {
            FileHelper.writeLog(fileName, toExportFormat(dataModel));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("time passed:" + (endTime - startTime) / 1000 + " seconds.");
        System.out.println("End at " + new Date());
    }

    private static void transformValueToLog() {
        dataList.stream().forEach(dataModel1 -> Utils.transformValueToLog(dataModel1.getDataArray()));
    }

    private static void transformOutput() {
        dataList.stream().forEach(dataModel -> dataModel.setGain(dataModel.getPercentageGain() >= TARGET ? 1.0 : 0));
    }

    private static String toExportFormat(StockDataModel dataModel){
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(dataModel.getDataArray()).replace("[", "").replace("]", "").replace(',', ' '));
        sb.append(" " + dataModel.getGain());
        return sb.toString();
    }

    private static double[] getMax(List<StockDataModel> dataList) {
        double[] max = new double[dataList.get(0).getDataArray().length];
        for(int i=0;i<max.length;i++) {
            final int index = i;
            max[i] = dataList.stream().mapToDouble(d -> d.getDataArray()[index]).max().getAsDouble();
        }
        return max;
    }

    private static double[] getMin(List<StockDataModel> dataList) {
        double[] min = new double[dataList.get(0).getDataArray().length];
        for(int i=0;i<min.length;i++) {
            final int index = i;
            min[i] = dataList.stream().mapToDouble(d -> d.getDataArray()[index]).min().getAsDouble();
        }
        return min;
    }

    private static void normalizeData(List<StockDataModel> dataList) {
        double[] max = getMax(dataList);
        double[] min = getMin(dataList);
        dataList.forEach(dataModel -> {
            double[] dataArray = dataModel.getDataArray();
            for (int i=0;i < dataArray.length; i++) {
                dataArray[i] = (dataArray[i] - min[i]) / (max[i] - min[i]);
            }
        });

    }
}
