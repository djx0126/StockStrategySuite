package djx.stockdataanalyzer.tools;

import com.djx.stockgainanalyzer.*;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;
import djx.stockdataanalyzer.Normalizer;
import djx.stockdataanalyzer.StatisticResultCalculator;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.data.*;
import com.stockstrategy.statistic.data.CollectDataStrategy;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by dave on 2015/9/29.
 */
public class ModelTester {
    public static double[] mean={1.2199290371701975E-4, 2.591573986653512E-4, 2.5219311419778795E-4, 2.473942814933876E-4, 2.7927279863191115E-4, 0.002332792530615212, 0.0038824388558191757, 0.0025093951416055756, 0.004638796559159819};
    public static double[] stdV={0.010490997984008186, 0.010466040992278457, 0.010892426903898678, 0.01091893846216214, 0.010894268378548413, 0.03223862060696609, 0.04502175529722175, 0.022169101108444254, 0.03241693317771712};
    public static double[] offsets={-0.7155822351935363, 0.35324054426255397, -0.9726091817216752, 0.43644680646577305, -0.6623399331541894, -0.06612077773074551, -1.0694967957857109, -0.16852577193161788, -1.3976987626278468};
    public static double[] scales={1.5625, 0.75, 1.0, 1.125, 1.125, 0.31640625, 0.875, 1.0, 0.004228282585245324};
    public static ResultModel model = new ResultModel(offsets, scales);

    /*model parameters*/
    public static final int PRE = 60;
    public static final int GAIN = 20;

    public static final int[] dayFields = {/*close*/5, /*open*/0, /*high*/0, /*low*/0, /*vol*/0};
    public static final int[] maFields = {30, 60}; //{5, 10, 20, 30};
    public static final int[] overAllmaFields = {30, 60}; //{5, 10, 20, 30};

    /*calc parameters*/
    public static boolean NORMALIZE = true;

    static {
        StockDataAnalyzer.PRE = PRE;
        StockDataAnalyzer.GAIN = GAIN;
        StockDataAnalyzer.dayFields = dayFields;
        StockDataAnalyzer.maFields = maFields;
        StockDataAnalyzer.NORMALIZE = NORMALIZE;
        StockDataAnalyzer.fieldModel = new FieldModel(dayFields, maFields, overAllmaFields);
    }

    public static List<StockDataModel> dataList = new ArrayList<>();
    public static StockDataModel[] rawData;

    public static void main(String[] args){
        System.out.println("Start");
        long startTime = System.currentTimeMillis();
        ArgParser.loadInitConfigures(args, Constant.class);

        StrategyHelper.generateData(CollectDataStrategy.NAME);
        ModelTester.dataList = StockDataAnalyzer.dataList.stream().sorted((o1, o2) -> o1.getKeyDate().compareTo(o2.getKeyDate())).collect(Collectors.toList());
        rawData = new StockDataModel[dataList.size()];
        for(int i=0;i<dataList.size();i++){
            StockDataModel data = dataList.get(i);
            rawData[i] = data;
        }

        System.out.println("Total " + dataList.size() + " records loaded.");
        if (dataList.size()<=0){
            return;
        }

        System.out.println("pre="+PRE);
        System.out.println("gain=" + GAIN);
        System.out.println(Utils.getFieldArrayDefString("dayFields", dayFields));
        System.out.println(Utils.getFieldArrayDefString("maFields", maFields));

        dataList.stream().forEach(d -> Utils.transformValueToLog(d.getDataArray()));

        if (NORMALIZE) {
            double[] meanTemp = mean;
            double[] stdVTemp = stdV;
            dataList.stream().forEach(d -> Normalizer.normalizeDataModel(d, meanTemp, stdVTemp));
        }

        String logs = "";

        StatisticResultCalculator resultCalculator = new StatisticResultCalculator(true);
        StatisticResult statisticResult = resultCalculator.calcStatisticResult(model, rawData);
        System.out.println(statisticResult);

        FileHelper.writeLog(logs);


        long endTime = System.currentTimeMillis();
        System.out.println("time passed:" + (endTime - startTime) / 1000 + " seconds.");
        System.out.println("End at " + new Date());
    }


}
