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
	
	JobResultStrategytestController.$inject = ['$scope', '$q', '$http', '$timeout', '$modal', 'jobFactory', 'jobMetaFactory', 'transactionsFactory', 'stategytestJobResultFactory'];
	
	function JobResultStrategytestController($scope, $q, $http, $timeout, $modal, jobFactory, jobMetaFactory, transactionsFactory, stategytestJobResultFactory){
		var jobResultStrategytest = this;
		
		jobResultStrategytest.results = initResults($scope.result.strategyResultMap);
		
		jobResultStrategytest.showTransactionDialog = showTransactionDialog;

        jobResultStrategytest.filterResultItem = filterResultItem;
		
		jobResultStrategytest.previousResults = [];

		//loadPreviousJobResult(parseInt($scope.jobId, 10));


		//////////////////////////////////////////////////////////////////////////////
		
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
			return true;
        }
		
		function showTransactionDialog(resultItem){
			var dialog = $modal.open({
	            templateUrl: 'job-result/strategytest/transaction-dialog/transaction-dialog.html',
	            backdrop: 'static',
	            controller: 'transaction-dialog-controller',
	            controllerAs: 'transactionDialog',
	            windowClass: 'strategy-transaction-dialog',
				resolve:{
					transactions: function(){
						return loadTransactions(resultItem);
	            	},
	            	strategy: function(){
	            		return resultItem.strategy;
	            	}
				}
	        });
		}

		function loadTransactions(resultItem) {
            if (resultItem.transactions) {
                return $q.when(resultItem.transactions);
            }
            return transactionsFactory.getTransactionList($scope.jobId, resultItem.strategy).then(function(rawTransactionList){
                resultItem.transactions = initTransactionList(rawTransactionList);
                resultItem.lastGainDate = _(resultItem.transactions).filter(function(transactionByDate) {return transactionByDate.avgGain > 0;}).map('buyDate').max().value();
                return resultItem.transactions;
            });
        }
		
		function initResults(rawResults){
			var results = _.map(rawResults, stategytestJobResultFactory.buildStrategyTestJobResult);
			results = _.sortBy(results, 'strategy');
			if (results.length > 0) {
            	$timeout(function () {
                    preloadTransactionsForResultItemByIndex(results, 0);
                })
            }
			return results;
		}

		function preloadTransactionsForResultItemByIndex(results, index) {
			var resultItem = results[index];
            loadTransactions(resultItem).then(function () {
				if (index < results.length - 1) {
					$timeout(function () {
                        preloadTransactionsForResultItemByIndex(results, index + 1);
                    });
				}
            });
        }
		
		function initTransactionList(rawTransactionList){
			var result = [];
			var resultByDateMap = {};
			angular.forEach(rawTransactionList, function(transcation){
				var buyDate= transcation.buyDate;
				var gain = (transcation.sellPrice-transcation.buyPrice)*100/transcation.buyPrice;
				if (!resultByDateMap[buyDate]){
					resultByDateMap[buyDate] = {
						buyDate: buyDate,
					    count:0,
					    sum:0,
					    list:[]
					};
				}
				var dateList = resultByDateMap[buyDate];
				dateList.count++;
				dateList.sum += gain;
				dateList.list.push(transcation);
			});
			
			angular.forEach(resultByDateMap, function(dateList){
				dateList.avgGain = dateList.count>0?dateList.sum/dateList.count:0;
				result.push(dateList);
			});
			result = _.sortBy(result, 'buyDate');
			return result;
		}
		
		
	}
}


})();