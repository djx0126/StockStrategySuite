package djx.stockdataanalyzer;

import com.djx.stockgainanalyzer.*;
import com.djx.stockgainanalyzer.data.IStockGain;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.CollectDataStrategy;
import djx.stockdataanalyzer.data.FieldModel;
import djx.stockdataanalyzer.data.ModelWithStatistic;
import djx.stockdataanalyzer.data.StockDataModel;
import djx.stockdataanalyzer.data.StockDataModelHelper;
import djx.stockdataanalyzer.learner.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by dave on 2015/9/29.
 */
public class StockDataAnalyzer {
    /*model parameters*/
    public static int PRE = 3;
    public static int GAIN = 2;

    public static int[] dayFields = {/*close*/10, /*open*/0, /*high*/0, /*low*/0, /*vol*/0};
    public static int[] maFields = {5, 20}; //{5, 10, 20, 30};
    public static int[] overAllmaFields = {}; //{5, 10, 20, 30};

    public static boolean skipBigChange = false;
    public static boolean forceBuy = false;

    /*calc parameters*/
    public static int MIN_CLUSTER_SIZE = 400;     // 150
    public static double TARGET_GAIN = 0.0d;
    public static boolean NORMALIZE = true;
    public static final int offset_steps = 11; //180512 13  //    130,  15   ,    15
    public static final int scale_steps = 5;     //     20,  17   ,    20
    public static double scale_step = 0.125d;// 0.125,  0.025,  0.05
    public static final int MAX_ITER_PER_RUN = 100;
    public static boolean ADJUST_COUNT_BY_DAY = true;

    /*running parameters*/
    public static boolean debug = false;
    public static boolean usingPreFilter = false;
    public static float preFilterRate = 0.5f;
    public static String SELECT_TEST_DATA_BY_DATE_STRING = "20150701"; // if set to null, will use a random selector by stock code
    public static final double E = 1e-4f;
    public static final int TEST_RUN_TIMES = 1000;
    public static final int THREAD_NUM = 12;
    public static int continueFrom = 0;
    public static String endDate;


    /*running data*/
    public static FieldModel fieldModel = new FieldModel(dayFields, maFields, overAllmaFields);

    public static StockDataModelHelper dataModelHelper = new StockDataModelHelper();
    public static List<StockDataModel> dataList = new ArrayList<>();
    public static StockDataModel[] rawData;
    public static StockDataModel[] trainData;
    public static StockDataModel[] testData;

    public static Normalizer.NormalizeInfo normalizeInfo;
    public static double TARGET_HIGH_GAIN = GAIN + 1.0d; //5.0d;
    public static boolean LOOSE_VALID = true;

    public static List<StockDataModel> dataWithHighGain = new ArrayList<>();
    public static double[] paramsMax;
    public static double[] paramsMin;
    public static double[] scaleSteps;
    public static double[][] scaleParamList;
    public static double[] offsetSteps;

    public static String today = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
    public static Random random = new Random(System.currentTimeMillis());

    public static synchronized void addStockGainData(StockDataModel data){
        dataList.add(data);
    }

    public static void main(String[] args){
        long startTime = System.currentTimeMillis();
        ArgParser.loadInitConfigures(args, StockDataAnalyzer.class);
        ArgParser.loadInitConfigures(args, Constant.class);

        init();

        int maxDayField = Arrays.stream(dayFields).max().getAsInt();
        if (maxDayField > PRE) {
            PRE = maxDayField;
        }

        generateData();

        dataList.stream().forEach(d -> Utils.transformValueToLog(d.getDataArray()));
        normalizeData();

        setParamsMaxAndMin(rawData);
        prepareParamSteps();
        prepareHighGainData(rawData);

        LearnerFactory factory = LearnerFactory.prepare(RepeatLearner.class).append(IterativeLearner.class).append(ParamLearner.class).get();
        ILeaner learner = factory.buildLearner();
        learner.learnForModelAndStatistic(trainData,testData);

        long endTime = System.currentTimeMillis();
        System.out.println("time passed:" + (endTime - startTime) / 1000 + " seconds.");
        System.out.println("End at " + new Date());
    }

    private static void init() {
        TARGET_HIGH_GAIN = GAIN + 1.0d;
        fieldModel = new FieldModel(dayFields, maFields, overAllmaFields);
    }

    private static void generateData() {
        if (usingPreFilter){
            MIN_CLUSTER_SIZE = (int)(MIN_CLUSTER_SIZE * preFilterRate);
        }

        System.out.println("Start");


        endDate = StrategyHelper.generateData(CollectDataStrategy.NAME);
        selectTrainAndTestData();

        System.out.println("Total " + dataList.size() + " records loaded.");
        if (dataList.size()<=0){
            return;
        }

        System.out.println("pre="+PRE);
        System.out.println("gain="+GAIN);
        System.out.println(Utils.getFieldArrayDefString("dayFields", dayFields));
        System.out.println(Utils.getFieldArrayDefString("maFields", maFields));
        System.out.println(Utils.getFieldArrayDefString("overAllmaFields", overAllmaFields));
        System.out.println("limit=" + dataList.get(0).getDataArray().length);
        System.out.println("skipBigChange=" + StockDataAnalyzer.skipBigChange);

    }

    private static void selectTrainAndTestData() {
        IStockSelector dataSelector;
        if (SELECT_TEST_DATA_BY_DATE_STRING!=null){
            System.out.println("Set test data after date: " + SELECT_TEST_DATA_BY_DATE_STRING);
            dataSelector = new StockDateSelector(dataList, SELECT_TEST_DATA_BY_DATE_STRING);
        }else{
            System.out.println("Randomly select test data");
            dataSelector = new StockRandomSelector(dataList);
        }

        List<IStockGain> trainList = dataSelector.getTrainDataList();
        List<IStockGain> testList = dataSelector.getValidateDataList();

        trainData = new StockDataModel[trainList.size()];
        testData = new StockDataModel[testList.size()];
        int index = 0;
        rawData = new StockDataModel[trainList.size() + testList.size()];

        for (int i = 0;i<trainList.size();i++) {
            trainData[i] = (StockDataModel)trainList.get(i);
            rawData[index++] = trainData[i];
        }

        for (int i = 0;i<testList.size();i++) {
            testData[i] = (StockDataModel)testList.get(i);
            rawData[index++] = testData[i];
        }

        System.out.println("Train data size: " + trainData.length);
        System.out.println("Test data size: " + testData.length);
    }

    private static void normalizeData() {
        double[] mean = {};
        double[] stdV = {};
        if (NORMALIZE) {
            mean = Normalizer.getMean(rawData);
            stdV = Normalizer.getStdV(rawData, mean);
            double[] meanTemp = mean;
            double[] stdVTemp = stdV;
            dataList.stream().forEach(d -> Normalizer.normalizeDataModel(d, meanTemp, stdVTemp));

            normalizeInfo = Normalizer.buildNormalizeInfo(mean, stdV);

            System.out.println(Utils.getFieldArrayDefString("mean", mean));
            System.out.println(Utils.getFieldArrayDefString("stdV", stdV));
        }
    }

    private static void prepareHighGainData(StockDataModel[] dataModels) {
        for (int i = 0; i < dataModels.length; i++) {
            StockDataModel data = dataModels[i];
            double percentageGain = data.getPercentageGain();
            if(percentageGain >= TARGET_HIGH_GAIN){
                dataWithHighGain.add(data);
            }
        }

        if (dataWithHighGain.size() == 0) {
            LOOSE_VALID = true;
            for (int i = 0; i < dataModels.length; i++) {
                StockDataModel data = dataModels[i];
                double percentageGain = data.getPercentageGain();
                if(percentageGain >= 0.0){
                    dataWithHighGain.add(data);
                }
            }
            System.out.println("loose valid used!!!");
        }

        dataWithHighGain = dataWithHighGain.stream().sorted((o1, o2) -> o1.getPercentageGain() > o2.getPercentageGain() ? 1 : o1.getPercentageGain() < o2.getPercentageGain() ? -1: 0).collect(Collectors.toList());
        System.out.println(dataWithHighGain.size() + " records have high gain.");

        if (continueFrom > 0) {
            dataWithHighGain = dataWithHighGain.subList(continueFrom, dataWithHighGain.size());
            System.out.println("Continued from " + continueFrom + " with " + dataWithHighGain.size() + " records left.");
        }
    }

    private static void setParamsMaxAndMin(StockDataModel[] dataModels) {
        int paramsLength = dataModels[0].getDataArray().length;

        paramsMax = new double[paramsLength];
        Arrays.fill(paramsMax, -1000000000000.0d);
        paramsMin = new double[paramsLength];
        Arrays.fill(paramsMin, 1000000000000.0d);

        for (StockDataModel dataModel: dataModels) {
            double[] dataArray = dataModel.getDataArray();
            for (int i=0;i<paramsLength;i++) {
                if (dataArray[i] < paramsMin[i]) {
                    paramsMin[i] = dataArray[i];
                }
                if (dataArray[i] > paramsMax[i]) {
                    paramsMax[i] = dataArray[i];
                }
            }
        }
    }

    private static void prepareParamSteps(){
        int fieldLength = paramsMin.length;

        scaleSteps = new double[fieldLength];
        offsetSteps = new double[fieldLength];
        scaleParamList = new double[fieldLength][];
        for (int i=0;i<fieldLength;i++) {
            double min = paramsMin[i]*0.95f;
            double max = paramsMax[i]*1.0f;
            scaleSteps[i] = (max-min)/(double)(scale_steps-1);

            scaleParamList[i] = new double[scale_steps];
            for (int j=0;j<scale_steps;j++){
                scaleParamList[i][j] = (j+1)*(scaleSteps[i]);
            }

            offsetSteps[i] = (max-min)/(double)(offset_steps-1);
        }

        System.out.println("params prepared.");
    }

    public static void writeModel(ModelWithStatistic modelWithStatistic) {
        String type = "ConfigArrayPreGainStrategy";
//        String fileNameString = Utils.nowStr() + "_" + String.valueOf((int)modelWithStatistic.statisticResult.getScore());
        String fileNameString = String.format("%.3f", StockDataAnalyzer.scale_step) + "_"+String.valueOf((int)modelWithStatistic.statisticResult.getScore()) + "_" + Utils.nowStr();
        FileHelper.writeLog(fileNameString, "#" + modelWithStatistic.statisticResult.toString() + "\n"
                + "#strategyCreationDate=" + today + "\n"
                + "creationDate=" + endDate + "\n"
                + "type=" + type + "\n"
                + "avgByDay=" + ADJUST_COUNT_BY_DAY + "\n"
                + "pre=" + PRE + "\n"
                + "gain=" + GAIN + "\n"
                + "limit=" + modelWithStatistic.model.offsets.length + "\n"
                + (!skipBigChange ? "skipBigChange=" + skipBigChange + "\n" : "") // default true, save for false
                + (SELECT_TEST_DATA_BY_DATE_STRING!=null ? "validationDate=" + SELECT_TEST_DATA_BY_DATE_STRING + "\n" : "")
                + (NORMALIZE ? Utils.getArrayString("mean", normalizeInfo.mean).replaceAll("\\{", "").replaceAll("}", "") + "\n" : "")
                + (NORMALIZE ? Utils.getArrayString("stdV", normalizeInfo.stdV).replaceAll("\\{", "").replaceAll("}", "") + "\n" : "")
                + Utils.getArrayString("dayFields", dayFields).replaceAll("\\{", "").replaceAll("}", "") + "\n"
                + Utils.getArrayString("maFields", maFields).replaceAll("\\{", "").replaceAll("}", "") + "\n"
                + Utils.getArrayString("overAllmaFields", overAllmaFields).replaceAll("\\{", "").replaceAll("}", "") + "\n"
                + modelWithStatistic.model.toString().replaceAll("\\{", "").replaceAll("}", "")
        );
    }
}
