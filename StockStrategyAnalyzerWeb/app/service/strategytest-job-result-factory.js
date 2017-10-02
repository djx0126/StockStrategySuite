(function() {
    'use strict';

    angular.module('main-module').factory('stategytestJobResultFactory', function() {
        return {
            buildStrategyTestJobResult: buildStrategyTestJobResult,
            calcTransactionStatistics: calcTransactionStatistics
        };

        function buildStrategyTestJobResult(result) {
            var tempRate = result.allLost >0 ?result.netGain / result.allLost: 9999;
            var tempAccuracy= 100 * result.gainCounter/(result.gainCounter+result.lostCounter);

            var transactionStatistics = buildTransactionStatistics(result.transactionGainCounter, result.transactionGain, result.transactionLostCounter, result.transactionLost);

            return {
                strategy: result.strategy,
                meanGain: result.meanGain,
                netGain: result.netGain,
                allLost: result.allLost,
                accuracy: tempAccuracy,
                rate: tempRate,
                gainCounter: result.gainCounter,
                lostCounter: result.lostCounter,
                transactionCounter: transactionStatistics.counter,
                transactionAccuracy: transactionStatistics.accuracy,
                transactionAvgGain: transactionStatistics.avgGain,
                transactionRate: transactionStatistics.rate,
                transScore: transactionStatistics.score,
                transactions: undefined
            };
        }

        function calcTransactionStatistics(transactions, gainExtractFn) {
            var gainCounter = 0, gainAll = 0;
            var lostCounter = 0, lostAll = 0;
            var multiGain = 1.0;
            _.forEach(transactions, function(transaction) {
                var gain = angular.isFunction(gainExtractFn)? gainExtractFn(transaction) : (transaction.sellPrice-transaction.buyPrice)*100/transaction.buyPrice;
                if (gain > 0) {
                    gainCounter++;
                    gainAll += gain;
                } else {
                    lostCounter++;
                    lostAll += (-gain);
                }
                multiGain *= (gain + 100) / 100;
            });
            var transactionStatistics = buildTransactionStatistics(gainCounter, gainAll, lostCounter, lostAll);

            return _.extend(transactionStatistics, {
                multiGain: multiGain
            });
        }

        function buildTransactionStatistics(transactionGainCounter, transactionGain, transactionLostCounter, transactionLost) {
            var tempTransactionRate = transactionLost >0? transactionGain/transactionLost:9999;
            var tempTransCounter = transactionGainCounter + transactionLostCounter;
            var tempTransAccuracy= 100*transactionGainCounter/(tempTransCounter);
            var tempTransAvgGain = (transactionGain-transactionLost)/(transactionGainCounter + transactionLostCounter);

            var tempScore = tempTransAvgGain * tempTransAvgGain * tempTransAccuracy * (1 - Math.exp(  -((tempTransCounter) - 1.0)/50  ));
            tempScore = tempTransAvgGain > 0? tempScore : -tempScore;

            var kaily = (tempTransAccuracy/100 * tempTransactionRate - (1 - tempTransAccuracy/100)) / tempTransactionRate;
            return {
                counter:tempTransCounter,
                accuracy: tempTransAccuracy,
                rate: tempTransactionRate,
                avgGain: tempTransAvgGain,
                score: tempScore,
                kaily: kaily
            };
        }
    });
})();
