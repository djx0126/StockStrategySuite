package com.stockstrategyanalyzer.job.strategytest;

import com.stockstrategy.simulator.SimulatorRunner;
import com.stockstrategy.simulator.aggregate.AbstractAggregate;
import com.stockstrategy.simulator.aggregate.AggregateMgr;
import com.stockstrategy.simulator.aggregate.BuySellDetailCollector;
import com.stockstrategy.simulator.aggregate.BuySellDetailCollector.Transaction;
import com.stockstrategy.statistic.data.AbstractStatisticData;
import com.stockstrategy.statistic.data.StatisticManager;
import com.stockstrategy.statistic.result.StatisticResultManager;
import com.stockstrategyanalyzer.job.Job;
import com.stockstrategyanalyzer.job.JobParam;
import com.stockstrategyanalyzer.result.ITaskResult;
import com.stockstrategyanalyzer.task.ITask;

import java.util.List;
import java.util.Map;

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

            AbstractStatisticData strategy = StatisticManager.getInstance().getStatistic(statisticType);
            String strategyStartDate = strategy.getStartDate();

            int transactionGainCounter = 0;
            int transactionLostCounter = 0;
            float transactionGain = 0.0f;
            float transactionLost = 0.0f;
            for (Transaction t:transactions){
                String buyDate = t.getBuyDate();
                if (buyDate.compareTo(strategyStartDate)<=0){
                    continue;
                }
                double buyPrice = t.getBuyPrice();
                double sellPrice = t.getSellPrice();
                if (sellPrice > buyPrice){
                    transactionGainCounter++;
                    transactionGain+= 100*((sellPrice-buyPrice)/buyPrice);
                }else if(sellPrice < buyPrice){
                    transactionLostCounter++;
                    transactionLost+= 100*((buyPrice - sellPrice)/buyPrice);
                }
            }

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
