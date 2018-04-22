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
        return calcStatisticResult(model, testingData, null);
    }

    public StatisticResult calcStatisticResult(ResultModel model, StockDataModel[] testingData, ScoreFormula scoreFormula) {
        List<StockDataModel> dataInResult = Arrays.stream(testingData).filter(d -> model.testOnData(d)).collect(Collectors.toList());

        Map<String, List<StockDataModel>> dataByKeyDate = dataInResult.stream().collect(Collectors.groupingBy(d -> d.getKeyDate()));
        Map<String, Integer> dateCount = dataByKeyDate.keySet().stream().collect(Collectors.toMap(d -> d, d -> dataByKeyDate.get(d).size()));

        List<Double> gainList = dataInResult.stream().map(StockDataModel::getPercentageGain).collect(Collectors.toList());

        int count = dataInResult.size();
        double sum = gainList.stream().mapToDouble(Double::new).sum();
        int countHigherThanTarget = (int) gainList.stream().filter(d -> d>StockDataAnalyzer.TARGET_GAIN ).count();
        double gainSum = gainList.stream().filter(d -> d > 0L).mapToDouble(Double::new).sum();
        double lostSum = gainList.stream().filter(d -> d < 0L).mapToDouble(d -> -d).sum();

        double sumAdjustedByDay = 0d;
        for (StockDataModel data: dataInResult) {
            if (debug){
                System.out.println(data);
            }
            double gain = data.getPercentageGain();

            String keyDate = data.getKeyDate();
            int dayBuyCount = dateCount.get(keyDate);
            double adjustedGain = ScoreFormula.adjustGainByDate(gain, dayBuyCount);
            sumAdjustedByDay += adjustedGain;
        }

        double avgGain = count>0? (sum/count):0;
        double rate = lostSum>0?gainSum/lostSum:1;
        double accuracy = count > 0 ? 100.0f * countHigherThanTarget / count : 0;

        double overFit = getOverFit(dateCount);


        ScoreFormula formula = StockDataAnalyzer.ADJUST_COUNT_BY_DAY ? new ScoreFormula.SimpleScoreFormula(sumAdjustedByDay) : new ScoreFormula();
        if (scoreFormula != null) {
            formula = scoreFormula;
        }

        StatisticResult statisticResult;
        if (StockDataAnalyzer.ADJUST_COUNT_BY_DAY) {
            statisticResult = new StatisticResultAdjustedByDay(formula, sum, lostSum, avgGain, rate, count, accuracy, overFit, sumAdjustedByDay, dataInResult);
        } else {
            statisticResult = new StatisticResult(formula, sum, lostSum, avgGain, rate, count, accuracy, overFit, dataInResult);
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
