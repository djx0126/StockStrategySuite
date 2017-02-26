package djx.stockdataanalyzer.learner;

import com.djx.stockgainanalyzer.Utils;
import djx.stockdataanalyzer.StatisticResultCalculator;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.data.ModelWithStatistic;
import djx.stockdataanalyzer.data.ResultModel;
import djx.stockdataanalyzer.data.StatisticResult;
import djx.stockdataanalyzer.data.StockDataModel;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created by dave on 2015/11/18.
 */
public class ParamLearner implements ILeaner{
    private static final int MAX_ITER = 100;

    private StatisticResultCalculator resultCalculator = new StatisticResultCalculator();
    private ResultModel startParam;
    private boolean learnForOffset = true;
    private boolean learnForScale = true;

    public ParamLearner(ResultModel startParam){
        this(startParam, true, true);
    }


    public ParamLearner(ResultModel startParam, boolean learnForOffset, boolean learnForScale){
        this.startParam = new ResultModel(startParam);
        this.learnForOffset = learnForOffset;
        this.learnForScale = learnForScale;
    }

    @Override
    public ModelWithStatistic learnForModelAndStatistic(StockDataModel[] trainData, StockDataModel[] cvData) {
        if (StockDataAnalyzer.debug) {
            System.out.println("Start Params: " + startParam);
        }
        StatisticResult statisticResult = resultCalculator.calcStatisticResult(startParam, trainData);
        ModelWithStatistic bestModel = new ModelWithStatistic(new ResultModel(startParam), statisticResult);

        int fieldLength = trainData[0].getDataArray().length;

        int latchCountBase = 0;
        if (learnForOffset) {
            latchCountBase++;
        }
        if (learnForScale) {
            latchCountBase++;
        }

        int latchCount = fieldLength * latchCountBase * /*up and down*/2 * /*double step*/2;
        if (StockDataAnalyzer.debug){
            System.out.println("start learn for scale and offset from " + statisticResult + ",start at " + new Date());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }

        long startTime = System.currentTimeMillis();
        int iter = 0;

        String learningLog = "";
        String lastLearnString = "";

        ResultModel lastIterModel = new ResultModel(startParam);

        while(++iter < MAX_ITER){
            long iterStart = System.currentTimeMillis();
            boolean updateInIter = false;/////////////////////////////////////

            ExecutorService threadPool = Executors.newFixedThreadPool(StockDataAnalyzer.THREAD_NUM);
            CountDownLatch latch = new CountDownLatch(latchCount);
            List<Pair<String, Future<ModelWithStatistic>>> resultList = new LinkedList<>();

            for (int i=0;i<fieldLength;i++){
                Future<ModelWithStatistic> future;
                if (learnForOffset) {
                    double offsetStep = StockDataAnalyzer.offsetSteps[i];
                    ResultModel offsetUpModel = new ResultModel(lastIterModel);
                    offsetUpModel.offsets[i] += offsetStep;
                    future = prepareExecuteThread(threadPool, latch, offsetUpModel, trainData);
                    resultList.add(new Pair<>("offset:"+String.valueOf(i)+":U", future));

                    ResultModel offsetUpModel2 = new ResultModel(lastIterModel);
                    offsetUpModel2.offsets[i] += (2*offsetStep);
                    future = prepareExecuteThread(threadPool, latch, offsetUpModel2, trainData);
                    resultList.add(new Pair<>("offset:"+String.valueOf(i)+":U2", future));

                    ResultModel offsetDownModel = new ResultModel(lastIterModel);
                    offsetDownModel.offsets[i] -= offsetStep;
                    future = prepareExecuteThread(threadPool, latch, offsetDownModel, trainData);
                    resultList.add(new Pair<>("offset:"+String.valueOf(i)+":D", future));

                    ResultModel offsetDownModel2 = new ResultModel(lastIterModel);
                    offsetDownModel2.offsets[i] -= (2*offsetStep);
                    future = prepareExecuteThread(threadPool, latch, offsetDownModel2, trainData);
                    resultList.add(new Pair<>("offset:" + String.valueOf(i) + ":D2", future));
                }

                if (learnForScale) {
                    ResultModel scaleUpModel = new ResultModel(lastIterModel);
                    scaleUpModel.scales[i] *= (1.0d + StockDataAnalyzer.scale_step);
                    future = prepareExecuteThread(threadPool, latch, scaleUpModel, trainData);
                    resultList.add(new Pair<>("scale:"+String.valueOf(i)+":U", future));

                    ResultModel scaleUpModel2 = new ResultModel(lastIterModel);
                    scaleUpModel2.scales[i] *= (1.0d + 2.0 * StockDataAnalyzer.scale_step);
                    future = prepareExecuteThread(threadPool, latch, scaleUpModel2, trainData);
                    resultList.add(new Pair<>("scale:"+String.valueOf(i)+":U2", future));

                    ResultModel scaleDownModel = new ResultModel(lastIterModel);
                    scaleDownModel.scales[i] *= (1.0d - StockDataAnalyzer.scale_step);
                    future = prepareExecuteThread(threadPool, latch, scaleDownModel, trainData);
                    resultList.add(new Pair<>("scale:"+String.valueOf(i)+":D", future));

                    ResultModel scaleDownModel2 = new ResultModel(lastIterModel);
                    scaleDownModel2.scales[i] *= (1.0d - 2.0 * StockDataAnalyzer.scale_step);
                    future = prepareExecuteThread(threadPool, latch, scaleDownModel2, trainData);
                    resultList.add(new Pair<>("scale:"+String.valueOf(i)+":D2", future));
                }
            }

            threadPool.shutdown();
            try {
                latch.await();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            ModelWithStatistic iterResult=null;
            String iterTag="";
            for (Pair<String, Future<ModelWithStatistic>> resultPair:resultList){
                String tag = resultPair.getKey();
                Future<ModelWithStatistic> future = resultPair.getValue();
                ModelWithStatistic result;
                try {
                    result = future.get();

//                    if (StockDataAnalyzer.debug) {
//                        System.out.println(tag + ":" + result.statisticResult);
//                    }

                    if(result.statisticResult.getScore() > bestModel.statisticResult.getScore()+StockDataAnalyzer.E &&
                            result.statisticResult.getOverFit() < 0.5d &&
                            result.statisticResult.getCount() >= StockDataAnalyzer.MIN_CLUSTER_SIZE
                            ){
                        bestModel = result;
                        iterResult = result;
                        iterTag = tag;
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            if (iterResult!=null){
                updateInIter = true;

                lastIterModel = iterResult.model;

                String partLearningLog = "";
                long iterEnt = System.currentTimeMillis();
                if(iter==1){
                    partLearningLog += "learing with gain:\n";
                }else if (iter>1 ){
                    partLearningLog += "//";
                }

                lastLearnString = iterResult.statisticResult+" time:"+(iterEnt-iterStart)/1000+"s " + iterTag;

                partLearningLog += lastLearnString;

                if(iter%2==0){
                    partLearningLog+="\n";
                }

                if (StockDataAnalyzer.debug){
                    System.out.print(partLearningLog);
                }else{
                    learningLog += partLearningLog;
                }

            }

            if (StockDataAnalyzer.MIN_CLUSTER_SIZE > 0){
                if (!updateInIter ||
                        iterResult.statisticResult.getOverFit() >= 0.5d ||
                        iterResult.statisticResult.getCount() <= StockDataAnalyzer.MIN_CLUSTER_SIZE
                        ){
                    break;
                }
            }
        }

        bestModel.log = learningLog;

        long endTime = System.currentTimeMillis();
        System.out.println("//**********  end run with test result>>>>>>>> "+bestModel.statisticResult+", end at "+ new Date()+" passed "+(endTime-startTime)/1000+"s");



        return bestModel;
    }

    private static Future<ModelWithStatistic> prepareExecuteThread(ExecutorService threadPool, CountDownLatch latch, ResultModel model, StockDataModel[] trainData){
        Future<ModelWithStatistic> future = threadPool.submit(() -> {
            StatisticResultCalculator calculator = new StatisticResultCalculator();
            StatisticResult statisticResult = calculator.calcStatisticResult(model, trainData);
            latch.countDown();
            return new ModelWithStatistic(model, statisticResult);
        });
        return future;
    }

}
