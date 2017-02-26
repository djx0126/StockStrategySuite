package djx.stockdataanalyzer.data;

import java.util.List;

/**
 * Created by dave on 2017/1/15.
 */
public class StatisticResultAdjustedByDay extends StatisticResult{
    private double sumAdjustedByDay;

    public StatisticResultAdjustedByDay(double sum, double lost, double avgGain, double rate, int count, double accuracy, double overFit, double sumAdjustedByDay, List<StockDataModel> dataInResult) {
        super(sum, lost, avgGain, rate, count, accuracy, overFit, dataInResult);
        this.sumAdjustedByDay = sumAdjustedByDay;
        this.score = this.sumAdjustedByDay;
    }

}
