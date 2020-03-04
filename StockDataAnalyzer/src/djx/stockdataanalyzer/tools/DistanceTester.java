package djx.stockdataanalyzer.tools;

import com.djx.stockgainanalyzer.StrategyHelper;
import com.djx.stockgainanalyzer.Utils;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.CollectDataStrategy;
import djx.stockdataanalyzer.Normalizer;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.data.FieldModel;
import djx.stockdataanalyzer.data.StockDataModel;

import java.util.*;
import java.util.stream.Collectors;

public class DistanceTester {
    /*model parameters*/
    public static final int PRE = 64;
    public static final int GAIN = 5;

    public static final int[] dayFields = {/*close*/20, /*open*/0, /*high*/0, /*low*/0, /*vol*/0};
    public static final int[] maFields = {}; //{5, 10, 20, 30};
    public static final int[] overAllmaFields = {}; //{5, 10, 20, 30};

    /*calc parameters*/
    public static boolean NORMALIZE = true;

    public static boolean PRINT_HEADER = true;

    static {
        StockDataAnalyzer.PRE = PRE;
        StockDataAnalyzer.GAIN = GAIN;
        StockDataAnalyzer.dayFields = dayFields;
        StockDataAnalyzer.maFields = maFields;
        StockDataAnalyzer.NORMALIZE = NORMALIZE;
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
        dataList = StockDataAnalyzer.dataList.stream().sorted(Comparator.comparing(StockDataModel::getKeyDate).thenComparing(StockDataModel::getStockCode)).collect(Collectors.toList());
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

        StockDataModel targetData = dataList.stream().filter(d -> d.getKeyDate().equals("20160805") && d.getStockCode().equals("300422")).findFirst().get();

        List<StockDataModel> closetDataList = dataList.stream().parallel().filter(d -> !d.getKeyDate().equals(targetData.getKeyDate())).sorted((o1, o2) -> {
            double v = distance(o1, targetData) - distance(o2, targetData);
            if (v > 0) {
                return 1;
            } else if (v < 0) {
                return -1;
            }
            return 0;
        }).limit(30).collect(Collectors.toList());

        System.out.println("Target: " + targetData);

//            String distances = closetDataList.stream().map(d -> String.valueOf(distance(targetData, d))).collect(Collectors.joining(","));
//            System.out.println(distances);

        double avgGain = closetDataList.stream().mapToDouble(d -> d.getPercentageGain()).average().getAsDouble();

        System.out.println("avg gain: " + avgGain + ", dis=" + String.format("%.3f", distance(targetData, closetDataList.get(0))));

//        Random random = new Random(10000);
//        for (int t = 0; t < 10; t++) {
//            int randIdx = random.nextInt(dataList.size());
//            StockDataModel targetData = dataList.get(randIdx);
//            List<StockDataModel> closetDataList = dataList.stream().parallel().filter(d -> !d.getKeyDate().equals(targetData.getKeyDate())).sorted((o1, o2) -> {
//                double v = distance(o1, targetData) - distance(o2, targetData);
//                if (v > 0) {
//                    return 1;
//                } else if (v < 0) {
//                    return -1;
//                }
//                return 0;
//            }).limit(30).collect(Collectors.toList());
//
//            System.out.println("Target: " + targetData);
//
////            String distances = closetDataList.stream().map(d -> String.valueOf(distance(targetData, d))).collect(Collectors.joining(","));
////            System.out.println(distances);
//
//            double avgGain = closetDataList.stream().mapToDouble(d -> d.getPercentageGain()).average().getAsDouble();
//
//            System.out.println("avg gain: " + avgGain + ", dis=" + String.format("%.3f", distance(targetData, closetDataList.get(0))));
//        }


        long endTime = System.currentTimeMillis();
        System.out.println("time passed:" + (endTime - startTime) / 1000 + " seconds.");
        System.out.println("End at " + new Date());
    }

    private static double distance(StockDataModel o1, StockDataModel o2) {
        double[] dataArray1 = o1.getDataArray();
        double[] dataArray2 = o2.getDataArray();
        int len = dataArray1.length;
        double sum = 0.0d;
        for (int i = 0; i < len; i++) {
            sum += (dataArray2[i] - dataArray1[i]) * (dataArray2[i] - dataArray1[i]);
        }
        return sum / len;
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

    }
}
