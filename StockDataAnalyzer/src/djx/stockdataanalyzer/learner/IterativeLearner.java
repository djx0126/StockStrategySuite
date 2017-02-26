package djx.stockdataanalyzer.learner;

import com.djx.stockgainanalyzer.Utils;
import djx.stockdataanalyzer.StatisticResultCalculator;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.data.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dave on 2015/11/17.
 */
public class IterativeLearner extends HierarchyLearner{
    private static int MAX_ITER_TO_RUN = StockDataAnalyzer.MAX_ITER_PER_RUN;
    private static boolean Shrink_Start_Point_Collection = false;

    private StatisticResultCalculator resultCalculator = new StatisticResultCalculator();
    private ModelWithStatistic accumulative = new ModelWithStatistic(new AccumulativeModel(), new StatisticResult());

    private static List<StockDataModel> dataWithHighGain;

    @Override
    public ModelWithStatistic learnForModelAndStatistic(StockDataModel[] dataModels, StockDataModel[] cvData) {
        if (dataWithHighGain == null) {dataWithHighGain = StockDataAnalyzer.dataWithHighGain;}

        int iter = 0;
        ModelWithStatistic bestModel = new ModelWithStatistic(null, new StatisticResult());
        while (iter++ < MAX_ITER_TO_RUN && dataWithHighGain.size() > 0){
            long highGainLeftCount = dataWithHighGain.size();
            System.out.println(String.format(">>>>>>>>>>>>>>>>>>>>>>>iter: %d, high gain left: %d ", iter, highGainLeftCount));

            // choose start point
            StockDataModel centerPoint = pickRandomDataModel();

            if (StockDataAnalyzer.debug) {
                System.out.println("pick center point: " + centerPoint);
            }
            double[] offsetStart = Utils.toNegative(centerPoint.getDataArray());

//            System.out.println("try start with unit scale");
            double[] scaleStart1 = getUnitScale();
            ResultModel startModel1 = new ResultModel(offsetStart, scaleStart1);
            if (bestModel.model == null) { // first iter
                bestModel.model = startModel1;
            }
            bestModel = learnForBetterModel(startModel1, dataModels, cvData, bestModel);

//            System.out.println("try start with uniform scale");
//            double[] scaleStart2 = getUniformScale();
//            ResultModel startModel2 = new ResultModel(offsetStart, scaleStart2);
//            bestModel = learnForBetterModel(startModel2, dataModels, cvData, bestModel);

//            ModelWithStatistic bestModelThisIter = bestModel;
//            dataWithHighGain = dataWithHighGain.stream().filter(d -> bestModelThisIter.model.testOnData(d)).collect(Collectors.toList());

            System.out.println("<<<<<<<<<<<<<<<<<<<<<<iter: " + iter +" done!");
            // try random scale start
        }

//        shrinkAccumulativeModel(cvData);
        System.out.println("At the end of iterative learner, accumulated result: " + accumulative.statisticResult);
        System.out.println(accumulative.model);

        return bestModel;
    }

    private ModelWithStatistic learnForBetterModel(ResultModel startModel, StockDataModel[] dataModels, StockDataModel[] cvData, ModelWithStatistic currentBestModel) {
        StatisticResult currentBestStatisticResult = currentBestModel.statisticResult;
        ResultModel bestModel = currentBestModel.model;

        ILeaner learner = this.buildSubLeaner(startModel);
        ModelWithStatistic modelWithStatistic = learner.learnForModelAndStatistic(dataModels, cvData);

        validateModel(modelWithStatistic, cvData);

        ResultModel resultModel = modelWithStatistic.model;
        StatisticResult statisticResult = modelWithStatistic.statisticResult;

        if (statisticResult.isValid()) {
            StockDataAnalyzer.writeModel(modelWithStatistic);
            if (statisticResult.getScore() > currentBestStatisticResult.getScore()) {
                bestModel = resultModel;
                currentBestStatisticResult = statisticResult;

//            AccumulativeModel tryingModel = new AccumulativeModel((AccumulativeModel)accumulative.model);
//            tryingModel.addModel(resultModel);
//            StatisticResult tryingStatisticResult = resultCalculator.calcStatisticResult(tryingModel, cvData);
//
//            if (tryingStatisticResult.isValid() && tryingStatisticResult.getScore() > accumulative.statisticResult.getScore()) {
//                this.accumulative = new ModelWithStatistic(tryingModel, tryingStatisticResult);
//                System.out.println("Accumulative model updated! " + tryingStatisticResult);
//                System.out.println(tryingModel);
//            }
            }
        }

        if (Shrink_Start_Point_Collection) {
            if (statisticResult.isValid()) {
                dataWithHighGain = dataWithHighGain.stream().filter(d -> !modelWithStatistic.model.testOnData(d)).collect(Collectors.toList());
            } else {
                dataWithHighGain = dataWithHighGain.stream().filter(d -> !Arrays.equals(d.getDataArray(), startModel.offsets)).collect(Collectors.toList());
            }
        }

        return new ModelWithStatistic(bestModel, currentBestStatisticResult);
    }

    private double[] getUnitScale(){
        int fieldLength = StockDataAnalyzer.scaleParamList.length;
        double[] scaleParam = new double[fieldLength];
        Arrays.fill(scaleParam,1.0d);
        return scaleParam;
    }

    private double[] getUniformScale(){
        int fieldLength = StockDataAnalyzer.scaleParamList.length;
        double[] scaleParam = new double[fieldLength];
        for (int i=0;i<fieldLength;i++){
            scaleParam[i] = StockDataAnalyzer.scaleParamList[i][StockDataAnalyzer.scale_steps/4];
        }
        return scaleParam;
    }

    private StockDataModel pickRandomDataModel() {
//        return StockDataAnalyzer.dataWithHighGain.get(StockDataAnalyzer.random.nextInt(StockDataAnalyzer.dataWithHighGain.size()));
        return dataWithHighGain.get(StockDataAnalyzer.random.nextInt(dataWithHighGain.size()));
    }

    private void shrinkAccumulativeModel(StockDataModel[] cvData) {
        boolean updated;
        do{
            AccumulativeModel tryingModel = new AccumulativeModel((AccumulativeModel)this.accumulative.model);
            List<ResultModel> modelList = tryingModel.getModels();
            updated = false;

            if (modelList.size() > 1) {
                for (ResultModel model: modelList) {
                    List<ResultModel> testingList = new LinkedList<>();
                    testingList.addAll(modelList);
                    testingList.remove(model);
                    tryingModel.setModels(testingList);

                    if (StockDataAnalyzer.debug) {
                        System.out.println("Shrinking accumulative model length: " + modelList.size() + "->" + testingList.size());
                    }

                    StatisticResult testingStatisticResult = resultCalculator.calcStatisticResult(tryingModel, cvData);
                    if (testingStatisticResult.isValid() && testingStatisticResult.getScore() > this.accumulative.statisticResult.getScore()) {
                        this.accumulative = new ModelWithStatistic(tryingModel, testingStatisticResult);
//                        if (StockDataAnalyzer.debug) {
                            System.out.println("Shrinking accumulative model to: " + this.accumulative.statisticResult);
//                        }
                        updated = true;
                        break;
                    }
                }
            }
        } while (updated);
    }


    private void validateModel(ModelWithStatistic model, StockDataModel[] cvData) {
        if (model.statisticResult.isValid() || StockDataAnalyzer.debug) {
            // validate on cvData
            model.statisticResult = resultCalculator.calcStatisticResult(model.model, cvData);
            System.out.println("//validating result: " + model.statisticResult);

            if (model.statisticResult.isValid() || StockDataAnalyzer.debug) {
                if (StockDataAnalyzer.debug){
                    System.out.println();
                }else if (model.statisticResult.isValid()){
                    System.out.println(model.log);
                }

                System.out.println(Utils.getFieldArrayDefString("offsets", model.model.offsets));
                System.out.println(Utils.getFieldArrayDefString("scales", model.model.scales));
                System.out.println("validated result: " + model.statisticResult);
            }
        }

        if (model.statisticResult.getAccuracy() >= 70.0d){
//            FileHelper.writeResult(PREVIOUS_NUM, GAIN_NUM, PARAMS, offsetParams, scaleParams, resultFromCal, lastLearnString);
        }
    }

}
