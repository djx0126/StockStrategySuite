package djx.stockdataanalyzer.learner;

import com.djx.stockgainanalyzer.Utils;
import djx.stockdataanalyzer.Normalizer;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.data.ModelWithStatistic;
import djx.stockdataanalyzer.data.ResultModel;
import djx.stockdataanalyzer.data.StatisticResult;
import djx.stockdataanalyzer.data.StockDataModel;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dave on 2015/12/10.
 */
public class OffsetMeansLearner implements ILeaner {
    private static final int MAX_ITER = 100;

    private ResultModel startParam;

    public OffsetMeansLearner(ResultModel startParam){
        this.startParam = new ResultModel(startParam);
    }

    @Override
    public ModelWithStatistic learnForModelAndStatistic(StockDataModel[] trainData, StockDataModel[] cvData) {
        ILeaner learner = buildLearnerForScale(startParam);
        ModelWithStatistic modelWithStatistic = learner.learnForModelAndStatistic(trainData, cvData);
        ModelWithStatistic bestModel = modelWithStatistic;
        boolean needContinue = true;

        int iter = 1;

        do{
            List<StockDataModel> dataInResult = modelWithStatistic.statisticResult.getDataInResult();
            List<StockDataModel> highGainData = dataInResult.stream().filter(d -> d.getPercentageGain() > StockDataAnalyzer.TARGET_HIGH_GAIN).collect(Collectors.toList());

            if (highGainData.size() == 0) {
                break;
            }

            StockDataModel[] dataInResultArray = highGainData.toArray(new StockDataModel[highGainData.size()]);
            double[] mean = Normalizer.getMean(dataInResultArray);
            ResultModel newModel = new ResultModel(Utils.toNegative(mean), modelWithStatistic.model.scales);

            if (StockDataAnalyzer.debug) {
                System.out.println("[OffsetMeansLearner] iter="+iter+", move offset: " + Arrays.toString(newModel.offsets)+", old offset:"+Arrays.toString(modelWithStatistic.model.offsets));
            }

            ILeaner iterLearner = buildLearnerForScale(newModel);
            modelWithStatistic = iterLearner.learnForModelAndStatistic(trainData, cvData);
            if (modelWithStatistic.statisticResult.getScore() > bestModel.statisticResult.getScore()) {
                bestModel = modelWithStatistic;
                needContinue = true;
            } else {
                needContinue = false;
            }
        } while (needContinue && iter++<MAX_ITER);

        return bestModel;
    }

    private ILeaner buildLearnerForScale(ResultModel startParam){
        return new ParamLearner(startParam, false, true);
    }

}
