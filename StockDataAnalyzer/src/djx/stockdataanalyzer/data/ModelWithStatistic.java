package djx.stockdataanalyzer.data;

/**
 * Created by dave on 2015/11/18.
 */
public class ModelWithStatistic {
    public ResultModel model;
    public StatisticResult statisticResult;
    public String log;

    public ModelWithStatistic(ResultModel model, StatisticResult statisticResult){
        this.model = model;
        this.statisticResult = statisticResult;
    }
    

}
