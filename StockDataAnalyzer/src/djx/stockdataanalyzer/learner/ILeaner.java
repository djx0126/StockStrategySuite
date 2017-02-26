package djx.stockdataanalyzer.learner;

import djx.stockdataanalyzer.StatisticResultCalculator;
import djx.stockdataanalyzer.data.ModelWithStatistic;
import djx.stockdataanalyzer.data.ResultModel;
import djx.stockdataanalyzer.data.StatisticResult;
import djx.stockdataanalyzer.data.StockDataModel;

/**
 * Created by dave on 2015/11/16.
 */
public interface ILeaner {
    ModelWithStatistic learnForModelAndStatistic(StockDataModel[] trainData, StockDataModel[] cvData);

    default <T extends ResultModel> StatisticResult test(T model, StockDataModel[] testData){
        StatisticResultCalculator resultCalculator = new StatisticResultCalculator();
        StatisticResult statisticResult = resultCalculator.calcStatisticResult(model, testData);
        return statisticResult;
    }
}
