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
	
JobResultFindtobuyController.$inject = ['$scope', '$http', '$timeout', 'jobFactory', 'jobMetaFactory'];
	
function JobResultFindtobuyController($scope, $http, $timeout, jobFactory, jobMetaFactory){
	var vm = this;
	
	
	vm.result = $scope.result;
	vm.resultFindToBuy = [];
	vm.toggleExpandResult = toggleExpandResult;
	
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