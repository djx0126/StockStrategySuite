(function(){
	
angular.module('main-module').directive('jobResultStrategytest', JobResultStrategyTest);

function JobResultStrategyTest(){
	return {
		restrict: 'E',
		replace: true,
		scope: {
			jobId: '@',
			result: '='
		},
		templateUrl: 'job-result/strategytest/job-result-strategytest.html',
		controller: JobResultStrategytestController,
		controllerAs: 'jobResultStrategytest',
		bindToController: true
	};
	
	JobResultStrategytestController.$inject = ['$scope', '$q', '$http', '$timeout', '$modal', 'jobFactory', 'jobMetaFactory', 'transactionsFactory', 'stategytestJobResultFactory', 'strategyFactory'];
	
	function JobResultStrategytestController($scope, $q, $http, $timeout, $modal, jobFactory, jobMetaFactory, transactionsFactory, stategytestJobResultFactory, strategyFactory){
		var jobResultStrategytest = this;
		
		jobResultStrategytest.results = initResults($scope.result.strategyResultMap);
		
		jobResultStrategytest.showTransactionDialog = showTransactionDialog;

        jobResultStrategytest.filterResultItem = filterResultItem;

        jobResultStrategytest.markVisibleAsReviewed = markVisibleAsReviewed;
		
		jobResultStrategytest.previousResults = [];

		jobResultStrategytest.expandAggregatingStrategies = expandAggregatingStrategies;
		jobResultStrategytest.collapseAggregatingStrategies = collapseAggregatingStrategies;

		//loadPreviousJobResult(parseInt($scope.jobId, 10));


		//////////////////////////////////////////////////////////////////////////////
		
		function getAggregatingStrategy(resultItem) {
			var strategyName = resultItem.strategy;

            var strategy = strategyFactory.getStrategy(strategyName);
            if (isAggregatedStrategy(strategy)) {
                return null;
            }

            var stategyies = strategyFactory.getStrategies();
            var aggregatingStrategy = _.find(stategyies, function(aStrategy) {
                if (isAggregatedStrategy(aStrategy)) {
                    if (strategyName.startsWith(aStrategy.strategyprefix)) {
                        return true;
                    }
                }
            });
            return aggregatingStrategy;
        }

        function isAggregatedStrategy(strategy) {
            return !!strategy && !!strategy.strategyprefix;
        }
		
        function markVisibleAsReviewed() {
            _.forEach(jobResultStrategytest.results, function (resultItem) {
                if (resultItem.transactions && filterResultItem(resultItem)) {
                    resultItem.reviewed = true;
                }
            })
        }
        
		function loadPreviousJobResult(jobId){
			jobFactory.getJobList().then(function(jobs){
				var currentJob = _.find(jobs, {id: jobId});
				var endDateYear = parseInt(currentJob.param.endDate.substr(0, 4), 10);
				var endDateMonth = parseInt(currentJob.param.endDate.substr(4, 2), 10);
				var endDateDay = currentJob.param.endDate.substr(6, 2);
				var previousMonthDate = (endDateMonth === 1? (endDateYear-1).toString()+'12': endDateYear.toString()+(endDateMonth<=10? '0'+(endDateMonth-1).toString() : (endDateMonth-1).toString()))+endDateDay;
				var previousJobs = _.filter(jobs, function(job){
					return job.type === currentJob.type && job.param.startDate === currentJob.param.startDate && job.param.endDate<=previousMonthDate;
				});
				if (previousJobs.length > 0){
					jobFactory.getJobResult(previousJobs[0]).then(function(result){
						jobResultStrategytest.previousResults.push(initResults(result.strategyResultMap));
					});
				}
			});
		}

		function filterResultItem(resultItem) {
			if (!jobResultStrategytest.transactionRatefilterOff && resultItem.transactionRate < 5.0) {
				return false;
			}
			if (!jobResultStrategytest.transactionAccuracyfilterOff && resultItem.transactionAccuracy < 70) {
				return false;
			}
			if (!jobResultStrategytest.transactionAccuracySteadyFilterOff) {
			    var diff = Math.abs(resultItem.transactionAccuracy - resultItem.accuracy);
                var base = Math.max(resultItem.transactionAccuracy, resultItem.accuracy);
                if (diff > base * 0.1) {
                    return false;
                }
            }
            if (!jobResultStrategytest.transactionMaxLossCountFilterOff) {
                var gainTransactions = _.filter(resultItem.transactions, function(transactionsByDate) {
                    return transactionsByDate.sum > 0;
                });
			    var lossTransactions = _.filter(resultItem.transactions, function(transactionsByDate) {
			        return transactionsByDate.sum < 0;
                });
			    var maxGainCount = _.max(gainTransactions, 'count').count;
			    var maxLossCount = _.max(lossTransactions, 'count').count;
                var maxLoss = _.min(lossTransactions, 'sum').sum;
                var countForMaxLoss = _.min(lossTransactions, 'sum').count;
                if (maxLossCount > 0.25 * maxGainCount && maxLossCount > 50
                    || countForMaxLoss > 0.25 * maxGainCount && maxLoss < -150) {
                    return false;
                }

            }
			return true;
        }
		
		function showTransactionDialog(resultItem, index){
			transactionsFactory.showTransactionDialog($scope.jobId, resultItem, index, jobResultStrategytest.results);
		}

		function initResults(rawResults){
			var results = _.map(rawResults, stategytestJobResultFactory.buildStrategyTestJobResult);
			results = _.sortBy(results, 'strategy');
			if (results.length > 0) {
            	$timeout(function () {
                    preloadTransactionsForResultItemByIndex(results, 0);
                })
            }

            _.forEach(results, function(resultItem) {
                var strategyName = resultItem.strategy;
                var strategy = strategyFactory.getStrategy(strategyName);
                resultItem.isAggregatedStrategy = isAggregatedStrategy(strategy);
                resultItem.aggregatingExpanded = false;
                var aggregatingStrategy = getAggregatingStrategy(resultItem);
                if (aggregatingStrategy) {
                    resultItem.aggregatingStrategyName = aggregatingStrategy.name;
                }
            });

			return results;
		}
		
		function expandAggregatingStrategies(resultItem) {
            var strategyName = resultItem.strategy;
            _.forEach(jobResultStrategytest.results, function (resultItem) {
                if (resultItem.aggregatingStrategyName === strategyName) {
                    resultItem.aggregatingExpanded = true;
                }
            });
            resultItem.aggregatingExpanded = true;
        }

        function collapseAggregatingStrategies(resultItem) {
            var strategyName = resultItem.strategy;
            _.forEach(jobResultStrategytest.results, function (resultItem) {
                if (resultItem.aggregatingStrategyName === strategyName) {
                    resultItem.aggregatingExpanded = false;
                }
            });
            resultItem.aggregatingExpanded = false;
        }

		function preloadTransactionsForResultItemByIndex(results, index) {
			var resultItem = results[index];
            transactionsFactory.loadTransactions($scope.jobId, resultItem).then(function () {
				if (index < results.length - 1) {
					$timeout(function () {
                        preloadTransactionsForResultItemByIndex(results, index + 1);
                    });
				}
            });
        }
		
	}
}


})();