package djx.stockdataanalyzer.data;

import djx.stockdataanalyzer.StockDataAnalyzer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dave on 2015/11/15.
 */
public class StatisticResult {
    private double sum;
    private double lost;
    private double avgGain;
    private double rate;
    private int count;
    private double accuracy;
    private double overFit;
    private ScoreFormula scoreFormula = new ScoreFormula();

    protected double score = 0.0d;

    private List<StockDataModel> dataInResult;

    public StatisticResult() {
        this(0, 0, 0, 1, 0, 0, 1.0d, new ArrayList<>());
    }

    public StatisticResult(double sum, double lost, double avgGain, double rate, int count, double accuracy, double overFit, List<StockDataModel> dataInResult) {
        this(new ScoreFormula(), sum, lost, avgGain, rate, count, accuracy, overFit, dataInResult);
    }

    public StatisticResult(ScoreFormula formula, double sum, double lost, double avgGain, double rate, int count, double accuracy, double overFit, List<StockDataModel> dataInResult) {
        this.sum = sum;
        this.lost = lost;
        this.avgGain = avgGain;
        this.rate = rate;
        this.count = count;
        this.accuracy = accuracy;
        this.overFit = overFit;
        this.dataInResult = dataInResult;
        this.scoreFormula = formula;
        calcScore();
    }

    public void setScoreFormula(ScoreFormula scoreFormula) {
        this.scoreFormula = scoreFormula;
        this.score = calcScore();
    }

    private double calcScore() {
        this.score = scoreFormula.calcScore(this);
        return score;
    }

    public double getScore() {
        return score;
    }

    public boolean isValid() {
//        return score>200 && accuracy>=60.0d && count>20 && overFit<0.4d;
//        return score>0 && accuracy>=70.0d && count>10 && overFit<0.4d;
//        return score>0 && accuracy>=70.0d && count>10;

        if (StockDataAnalyzer.LOOSE_VALID) {
            return (score>0 && this.avgGain > 0) && this.count>=1;
        }

        return (score>1.0 && accuracy>=30) && this.count>10 && this.rate > 2.0d && this.avgGain >= StockDataAnalyzer.GAIN/2;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.3f", this.score));
        sb.append("(");
        sb.append("avgGain=").append(String.format("%.3f", avgGain)).append("%");
        sb.append(",sum=").append(String.format("%.3f", sum));
        sb.append(",lost=").append(String.format("%.3f", lost));
        sb.append(",rate=").append(String.format("%.3f", rate));
        sb.append(",count=").append(String.format("%d", count));
        sb.append(",accuracy=").append(String.format("%.3f", accuracy));
        sb.append(",overFit=").append(String.format("%.3f", overFit));
        sb.append(")");
        return sb.toString();
    }

    public double getSum() {
        return sum;
    }


    public double getAvgGain() {
        return avgGain;
    }




    public double getRate() {
        return rate;
    }




    public int getCount() {
        return count;
    }



    public double getLost() {
        return lost;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getOverFit() {
        return this.overFit;
    }

    public List<StockDataModel> getDataInResult() {
        return dataInResult;
    }
}
