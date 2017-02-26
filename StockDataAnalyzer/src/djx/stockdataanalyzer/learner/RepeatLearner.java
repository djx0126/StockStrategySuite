package djx.stockdataanalyzer.learner;

import com.djx.stockgainanalyzer.FileHelper;
import djx.stockdataanalyzer.StockDataAnalyzer;
import djx.stockdataanalyzer.data.ModelWithStatistic;
import djx.stockdataanalyzer.data.StockDataModel;

/**
 * Created by dave on 2016/5/1.
 */
public class RepeatLearner extends HierarchyLearner {
    public static int TEST_RUN_TIMES = StockDataAnalyzer.TEST_RUN_TIMES;

    @Override
    public ModelWithStatistic learnForModelAndStatistic(StockDataModel[] trainData, StockDataModel[] cvData) {
        String logs = "";
        for (int i=0;i<TEST_RUN_TIMES; i++) {
            ILeaner leaner = this.buildSubLeaner();
            ModelWithStatistic modelWithStatistic = leaner.learnForModelAndStatistic(trainData, cvData);
            System.out.println("**************RUN[" + i + "]  End iterative leaner with results:");
            System.out.println(modelWithStatistic.statisticResult);
            System.out.println(modelWithStatistic.model);

            if (modelWithStatistic.statisticResult.getScore() > 0) {
                logs += modelWithStatistic.statisticResult.toString() + "\n";
            }

            if ( i>0 && i % 10 == 0) {
                FileHelper.writeLog(logs);
            }
        }
        FileHelper.writeLog(logs);
        return null;
    }
}
