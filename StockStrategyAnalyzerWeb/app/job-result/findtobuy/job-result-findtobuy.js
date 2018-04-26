(function(){
	'use strict';
	
	
	angular.module('main-module').directive('jobResultFindtobuy', JobResultFindtobuyDirective);
	
	function JobResultFindtobuyDirective(){
		return {
			restrict:'E',
			scope:{
				result:'='
			},
			templateUrl: 'job-result/findtobuy/job-result-findtobuy.html',
			controller: 'job-result-findtobuy-controller',
			controllerAs: 'vm'
		};
	}

	angular.module('main-module').controller('job-result-findtobuy-controller', JobResultFindtobuyController);
	
JobResultFindtobuyController.$inject = ['$scope', '$http', '$timeout', 'jobFactory', 'jobMetaFactory', 'transactionsFactory'];
	
function JobResultFindtobuyController($scope, $http, $timeout, jobFactory, jobMetaFactory, transactionsFactory){
	var vm = this;
	
	
	vm.result = $scope.result;
	vm.resultFindToBuy = [];
	vm.toggleExpandResult = toggleExpandResult;
	vm.showTransactionDialog = function (strategy, index) {
        var resultItem = {
            strategy: strategy
        };
        loadRecentJobResult().then(function (jobResult) {
			if (jobResult && jobResult.jobId && jobResult.strategyResultMap[strategy]) {
                transactionsFactory.showTransactionDialog(jobResult.jobId, resultItem, index, vm.resultFindToBuy)
			}
        });

        function loadRecentJobResult(){
            return jobFactory.getJobList().then(function(jobs){
            	var descJobs = _.sortBy(jobs, function (job) {
					return -job.id;
                });

            	var recentJob = _.find(descJobs, function (job) {
					return job.state ==2 && job.type == 3;
                });
                if (recentJob){
                    return jobFactory.getJobResult(recentJob);
                }
            });
        }
    };
	initResults();
	
function initResults(){
	angular.forEach(vm.result.strategyFindResults, function(resultItem){
		var strategyResult = {
		        expand: false,
				strategy: resultItem.strategy,
				stockGain: []
		};
		vm.resultFindToBuy.push(strategyResult);
		angular.forEach(resultItem.stockGainMap, function(value, key){
			strategyResult.stockGain.push({
				stockCode: key,
				gain: value
			});
		});
		strategyResult.stockGain = _.sortBy(strategyResult.stockGain, function(stockGainItem){
			return 0 - stockGainItem.gain;
		});
	});
	vm.resultFindToBuy = _.sortBy(vm.resultFindToBuy, 'strategy');
	
}
	
	
	function toggleExpandResult(result){
		result.expand = !result.expand;
	}
}

})();