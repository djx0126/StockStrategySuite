package djx.stockdataanalyzer.data;

import djx.stockdataanalyzer.StockDataAnalyzer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dave on 2015/11/15.
 */
public class ScoreFormula {

    public double calcScore(StatisticResult statisticResult) {
        if (StockDataAnalyzer.ADJUST_COUNT_BY_DAY) {
            return statisticResult.getScore();
        }

        double score = statisticResult.getAvgGain() * statisticResult.getAvgGain();
        score = statisticResult.getAvgGain() > 0 ? score : -score;

        score = score * statisticResult.getAccuracy();

        double c = statisticResult.getCount();
        double countAdjust = 1.0 - Math.exp(  -(c - 1.0)/50.0  ); //1-e^(-(x-1)/50);

        score = score * countAdjust;
        return score;
    }

    public static double adjustGainByDate(double oriGain, int dayCount) {
        double adjustedGain = oriGain;
//        if (dayCount > 10) {
//            adjustedGain = oriGain * (500*Math.log10( (Math.pow(dayCount, 0.25)+29)/30) + 5)/dayCount; //y=500log((x^0.25+29)/30)+5 x>10
//        }

//        if (oriGain < 0) {
//            adjustedGain *= 5;
//        }
        if (oriGain > 0) {
            adjustedGain = oriGain / dayCount;
        }

        return adjustedGain;
    }

    public static double calcScoreOld(StatisticResult statisticResult) {
        double avgGain =statisticResult.getAvgGain();
        int count = statisticResult.getCount();
        double accuracy = statisticResult.getAccuracy();

        double value;// = avgGain*avgGain*avgGain*Math.pow(count, 0.75f)-Math.pow(Math.log1p(lost), 2.5f);

        value = avgGain*avgGain*avgGain*Math.pow(count, 0.25f);

        return value >0 ?value*accuracy/100 : value;
    }

    public static class CumulativeGainScoreFormula extends ScoreFormula{
        @Override
        public double calcScore(StatisticResult statisticResult) {
            double cumulativeGain = 1.0d;
            List<StockDataModel> dataModels = statisticResult.getDataInResult();
            List<Double> gainList = dataModels.stream().map(StockDataModel::getGain).collect(Collectors.toList());

            if (StockDataAnalyzer.ADJUST_COUNT_BY_DAY) {
                Map<String, List<StockDataModel>> dataModelsByKeyDate = dataModels.stream().collect(Collectors.groupingBy(StockDataModel::getKeyDate));
                gainList = dataModelsByKeyDate.values().stream().map(l -> l.stream().map(StockDataModel::getGain).mapToDouble(Double::new).average().getAsDouble()).collect(Collectors.toList());
            }

            for(Double gain: gainList) {
                cumulativeGain *= gain;
            }

            return cumulativeGain;
        }
    }

    public static class SimpleScoreFormula extends ScoreFormula {
        private double score;

        public SimpleScoreFormula(double score) {this.score = score;}

        @Override
        public double calcScore(StatisticResult statisticResult) {
            return this.score;
        }
    }
}
