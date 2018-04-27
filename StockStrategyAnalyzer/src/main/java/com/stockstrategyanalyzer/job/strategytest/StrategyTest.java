package com.stockstrategyanalyzer.job.strategytest;

import com.stockstrategy.simulator.SimulatorRunner;
import com.stockstrategy.simulator.aggregate.AbstractAggregate;
import com.stockstrategy.simulator.aggregate.AggregateMgr;
import com.stockstrategy.simulator.aggregate.BuySellDetailCollector;
import com.stockstrategy.simulator.aggregate.BuySellDetailCollector.Transaction;
import com.stockstrategy.statistic.data.AbstractStrategyStatisticData;
import com.stockstrategy.statistic.data.StatisticManager;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobParam;
import com.stockstrategyanalyzer.result.ITaskResult;
import com.stockstrategyanalyzer.task.ITask;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StrategyTest extends SimulatorRunner implements ITask {
	private long jobId;
	private String startDate;
	private String endDate;
	
	private StrategyTestResult strategyTestResult;
	
	public StrategyTest(Job job, JobParam jobParam){
		this.jobId = job.getId();

		this.startDate = jobParam.getStartDate();
		this.endDate = jobParam.getEndDate();
	}

	@Override
	public ITaskResult runTask() {
		strategyTestResult = new StrategyTestResult(jobId);
		
		AbstractAggregate buySellDetailCollector = new BuySellDetailCollector();
		AggregateMgr.getInstance().addAggregate(buySellDetailCollector);
		
		this.init(startDate, endDate);
		this.run();
        this.resultInit();
        
        
        
        this.buildDetailResult();
        this.buildResult();
        
        
        @SuppressWarnings("unchecked")
		Map<String, List<Transaction>> statisticsTransactionMap = (Map<String, List<Transaction>>) buySellDetailCollector.getResult();
        this.buildTransactionResult(statisticsTransactionMap);
        
        AggregateMgr.getInstance().releaseAggregates();
        
		return strategyTestResult;
	}
	
	private void buildTransactionResult(Map<String, List<Transaction>> statisticsTransactionMap){
        resultList.stream().parallel().forEach(statisticType -> {
            OneStrategyResult strategyResult= strategyTestResult.getStrategyResultMap().get(statisticType);

            List<Transaction> transactions = statisticsTransactionMap.get(statisticType);

            AbstractStrategyStatisticData strategy = (AbstractStrategyStatisticData) StatisticManager.getInstance().getStatistic(statisticType);
            String strategyStartDate = strategy.getStartDate();

            List<Transaction> transactionsAfterCreationDate = transactions.stream().filter(t -> t.getBuyDate().compareTo(strategyStartDate) > 0).collect(Collectors.toList());

            List<Double> gainList = transactionsAfterCreationDate.stream().map(t -> (t.getSellPrice()-t.getBuyPrice())/t.getBuyPrice()).collect(Collectors.toList());

            if (strategy.isAvgByDay()) {
                Map<String, List<Transaction>> transactionsByDate = transactionsAfterCreationDate.stream().collect(Collectors.groupingBy(Transaction::getBuyDate));
                List<Double> gainListAvgByDay = transactionsByDate.keySet().stream().map(d -> transactionsByDate.get(d).stream().mapToDouble(t -> (t.getSellPrice()-t.getBuyPrice())/t.getBuyPrice()).average().getAsDouble()).collect(Collectors.toList());
                gainList = gainListAvgByDay;
            }

            List<Double> winList = gainList.stream().filter(d -> d > 0).collect(Collectors.toList());
            int transactionGainCounter = winList.size();
            float transactionGain = (float) winList.stream().mapToDouble(d -> 100*d).sum();
            List<Double> lostList = gainList.stream().filter(d -> d < 0).collect(Collectors.toList());
            int transactionLostCounter = lostList.size();
            float transactionLost = (float) lostList.stream().mapToDouble(d -> 100*d).sum();

            strategyResult.setTransactionGainCounter(transactionGainCounter).setTransactionLostCounter(transactionLostCounter).setTransactionGain(transactionGain).setTransactionLost(transactionLost);
//                System.out.println(strategyResult);
            strategyResult.setTransactions(transactions);
        });
	}
	
	
	private void buildDetailResult(){
		System.out.println("===detail result=== " + resultList.size());
        for (int i = 0; i < resultList.size(); i++) {
            String statisticType = resultList.get(i);
//                System.out.println(statisticType + ": ");

            OneStrategyResultDetail strategyResultDetail = new OneStrategyResultDetail(statisticType);
            strategyTestResult.addStrategyResultDetail(strategyResultDetail);
        }
	}
	
	private void buildResult(){
		for (int i = 0; i < resultList.size(); i++) {
            String statisticType = resultList.get(i);

            OneStrategyResult strategyResult= new OneStrategyResult(statisticType);
            strategyTestResult.addStrategyResult(strategyResult);

            float meanGain = meanGainList.get(i);
            float allLost = allLostList.get(i);

            int allGainCounter = 0;
            int allLostCounter = 0;
            for (String stock : StatisticResultManager.getInstance()
                    .getStatisticResults().keySet()) {
                if (StatisticResultManager.getInstance()
                        .getResult(stock)!=null && StatisticResultManager.getInstance()
                        .getResult(stock).getResult(statisticType)!=null){
                    float gain = StatisticResultManager.getInstance()
                            .getResult(stock).getResult(statisticType)
                            .getGain();
                    if (gain > 100f) {
                        allGainCounter++;
                    } else if (gain < 100f) {
                        allLostCounter++;
                    }
                }
            }

            float netGain = allNetGainList.get(i);


            strategyResult.setGainCounter(allGainCounter).setLostCounter(allLostCounter).setMeanGain(meanGain).setAllLost(allLost).setNetGain(netGain);

        }
	}

}
