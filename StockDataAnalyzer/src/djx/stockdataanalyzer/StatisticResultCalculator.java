package djx.stockdataanalyzer;

import com.djx.stockgainanalyzer.Result;
import djx.stockdataanalyzer.data.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dave on 2015/11/15.
 */
public class StatisticResultCalculator {
    private boolean debug = false;

    public StatisticResultCalculator() {}

    public StatisticResultCalculator(boolean debug) {
        this.debug = debug;
    }

    public StatisticResult calcStatisticResult(ResultModel model, StockDataModel[] testingData){
        double sum = 0d;
        double sumAdjustedByDay = 0d;
        double gainSum = 0;
        double lostSum = 0;
        int count = 0;
        int countHigherThanTarget = 0;
        Map<String, Integer> dateCount = new HashMap<>();
        List<StockDataModel> dataInResult = Arrays.stream(testingData).filter(d -> model.testOnData(d)).collect(Collectors.toList());

        dataInResult.stream().forEach(data -> {
            String keyDate = data.getKeyDate();
            if (!dateCount.containsKey(keyDate)) {
                dateCount.put(keyDate, 1);
            } else {
                int oldDateCount = dateCount.get(keyDate);
                dateCount.replace(keyDate, oldDateCount + 1);
            }
        });

        for (StockDataModel data: dataInResult) {
            if (debug){
                System.out.println(data);
            }
            double gain = data.getPercentageGain();

            if (gain>0){
                gainSum+=gain;
            }else{
                lostSum+= (-gain);
            }

            sum += gain;

            String keyDate = data.getKeyDate();
            int dayBuyCount = dateCount.get(keyDate);
            double adjustedGain = ScoreFormula.adjustGainByDate(gain, dayBuyCount);
            sumAdjustedByDay += adjustedGain;

            count++;
            if (gain > StockDataAnalyzer.TARGET_GAIN){
                countHigherThanTarget++;
            }

        }

        double avgGain = count>0? (sum/count):0;
        double rate = lostSum>0?gainSum/lostSum:1;
        double accuracy = count > 0 ? 100.0f * countHigherThanTarget / count : 0;

        double overFit = getOverFit(dateCount);

        StatisticResult statisticResult;
        if (StockDataAnalyzer.ADJUST_COUNT_BY_DAY) {
            statisticResult = new StatisticResultAdjustedByDay(sum, lostSum, avgGain, rate, count, accuracy, overFit, sumAdjustedByDay, dataInResult);
        } else {
            statisticResult = new StatisticResult(sum, lostSum, avgGain, rate, count, accuracy, overFit, dataInResult);
        }

        return statisticResult;
    }

    private double getOverFit(Map<String, Integer> dateCount) {
        Integer[] tempValues = new Integer[dateCount.size()];
        dateCount.values().toArray(tempValues);

        int[] values = new int[dateCount.size()];
        for (int i=0;i<tempValues.length;i++) {
            values[i] = tempValues[i];
        }
        Arrays.sort(values);

        int length = values.length;
        if (length <=2) {
            return 1;
        } else {
            int mostPart = values[length-1] + values[length-2];
            int sum = Arrays.stream(values).sum();

            return ((double)mostPart)/((double)sum);
        }
    }

}
