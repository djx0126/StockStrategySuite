package djx.stockdataanalyzer.data;

import djx.stockdataanalyzer.StockDataAnalyzer;

/**
 * Created by dave on 2015/11/15.
 */
public class ScoreFormula {

    public static double calcScore(StatisticResult statisticResult) {
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
}
