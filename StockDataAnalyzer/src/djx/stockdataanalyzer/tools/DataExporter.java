package djx.stockdataanalyzer.tools;

import com.djx.stockgainanalyzer.FileHelper;
import com.djx.stockgainanalyzer.StrategyHelper;
import com.djx.stockgainanalyzer.Utils;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.CollectDataStrategy;
import djx.stockdataanalyzer.Normalizer;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.data.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by dave on 2016/4/6.
 */
public class DataExporter {

    /*model parameters*/
    public static final int PRE = 64;
    public static final int GAIN = 5;

    public static final int[] dayFields = {/*close*/64, /*open*/64, /*high*/64, /*low*/64, /*vol*/64};
    public static final int[] maFields = {}; //{5, 10, 20, 30};
    public static final int[] overAllmaFields = {}; //{5, 10, 20, 30};

    /*calc parameters*/
    public static boolean NORMALIZE = true;
    public static boolean usingPreFilter = false;
    public static float preFilterRate = 0.1f;

    public static boolean PRINT_HEADER = true;

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

    private static float TARGET = 10.0f;
    private static String fileNameTemplate = "raw_data_pre" + PRE + "_gain" + GAIN + "_%s_%s%s.txt";
    private static String splitByDate = null; //"20140701"; // if null, don't split

    public static List<StockDataModel> dataList = new ArrayList<>();
    public static StockDataModel[] rawData;

    public static void main(String[] args) {
        System.out.println("Start");
        long startTime = System.currentTimeMillis();
        ArgParser.loadInitConfigures(args, Constant.class);

        StrategyHelper.generateData(CollectDataStrategy.NAME);
        DataExporter.dataList = StockDataAnalyzer.dataList.stream().sorted((o1, o2) -> o1.getKeyDate().compareTo(o2.getKeyDate())).collect(Collectors.toList());
        rawData = new StockDataModel[dataList.size()];

        dataList.toArray(rawData);

        System.out.println("Total " + dataList.size() + " records loaded.");
        if (dataList.size() <= 0) {
            return;
        }

        System.out.println("pre=" + PRE);
        System.out.println("gain=" + GAIN);
        System.out.println(Utils.getFieldArrayDefString("dayFields", dayFields));
        System.out.println(Utils.getFieldArrayDefString("maFields", maFields));


        transformValueToLog();
        normalizeData(dataList);

        transformOutput();

        String startDate = rawData[0].getKeyDate();
        String endDate = rawData[rawData.length - 1].getKeyDate();
        String minSuffix = usingPreFilter ? "_small" : "";
        String fileName = String.format(fileNameTemplate, startDate, endDate, minSuffix);

        for (StockDataModel dataModel : DataExporter.dataList) {
            String fileNameToWrite = fileName;
            if (splitByDate != null) {
                if (dataModel.getKeyDate().compareTo(splitByDate) > 0) {
                    // after
                    fileNameToWrite = String.format(fileNameTemplate, splitByDate, endDate, minSuffix);
                } else {
                    // before
                    fileNameToWrite = String.format(fileNameTemplate, startDate, splitByDate, minSuffix);
                }
            }

            if (PRINT_HEADER) {
                String[] fields = {"c", "o", "h", "l", "v"};
                String fieldsHeaderStr = IntStream.range(0, fields.length).mapToObj(i -> i).flatMap(i -> IntStream.range(1, dayFields[i]+1).mapToObj(n -> fields[i] + String.valueOf(n))).collect(Collectors.joining(","));
                FileHelper.writeLog(fileNameToWrite, "date,code,gain,"+fieldsHeaderStr);
                PRINT_HEADER = false;
            }

            FileHelper.writeLog(fileNameToWrite, toExportFormat(dataModel));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("time passed:" + (endTime - startTime) / 1000 + " seconds.");
        System.out.println("End at " + new Date());
    }

    private static void transformValueToLog() {
        dataList.stream().forEach(dataModel1 -> Utils.transformValueToLog(dataModel1.getDataArray()));
    }

    private static void transformOutput() {
        dataList.stream().forEach(dataModel -> dataModel.setGain(dataModel.getPercentageGain()));
    }

    private static String toExportFormat(StockDataModel dataModel) {
        StringBuilder sb = new StringBuilder();
        sb.append(dataModel.getKeyDate() + ", " + dataModel.getStockCode() + ", ");
        sb.append(dataModel.getGain() + ", ");
        sb.append(Arrays.toString(dataModel.getDataArray()).replace("[", "").replace("]", ""));//.replace(",", ' '));
        return sb.toString();
    }

    private static void normalizeData(List<StockDataModel> dataList) {
        double[] mean;
        double[] stdV;
        mean = Normalizer.getMean(rawData);
        stdV = Normalizer.getStdV(rawData, mean);
        double[] meanTemp = mean;
        double[] stdVTemp = stdV;

        if (NORMALIZE) {
            dataList.stream().forEach(d -> Normalizer.normalizeDataModel(d, meanTemp, stdVTemp));
        }

        Normalizer.NormalizeInfo normalizeInfo = Normalizer.buildNormalizeInfo(mean, stdV);

        String today = (new SimpleDateFormat("yyyyMMdd")).format(new Date());

        String startDate = rawData[0].getKeyDate();
        String endDate = rawData[rawData.length - 1].getKeyDate();
        String suffix = "_meta";
        String fileNameString = String.format(fileNameTemplate, startDate, endDate, suffix);

        FileHelper.writeLog(fileNameString, "#" + "\n"
                + "#creationDate=" + today + "\n"
                + "#startDate=" + startDate + "\n"
                + "#endDate=" + endDate + "\n"
                + "pre=" + PRE + "\n"
                + "gain=" + GAIN + "\n"
                + "normalized=" + NORMALIZE + "\n"
                + Utils.getArrayString("mean", normalizeInfo.mean).replaceAll("\\{", "").replaceAll("}", "") + "\n"
                + Utils.getArrayString("stdV", normalizeInfo.stdV).replaceAll("\\{", "").replaceAll("}", "") + "\n"
                + Utils.getArrayString("dayFields", dayFields).replaceAll("\\{", "").replaceAll("}", "") + "\n"
                + Utils.getArrayString("maFields", maFields).replaceAll("\\{", "").replaceAll("}", "") + "\n"
                + Utils.getArrayString("overAllmaFields", overAllmaFields).replaceAll("\\{", "").replaceAll("}", "") + "\n"
        );

    }
}
