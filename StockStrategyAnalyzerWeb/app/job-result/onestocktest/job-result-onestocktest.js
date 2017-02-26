(function(){
	'use strict';
	
	
	angular.module('main-module').directive('jobResultOnestocktest', JobResultOnestocktestDirective);
	
	function JobResultOnestocktestDirective(){
		return {
			restrict:'E',
			scope:{
				result:'='
			},
			templateUrl: 'job-result/onestocktest/job-result-onestocktest.html',
			controller: 'job-result-onestocktest-controller',
			controllerAs: 'vm'
		};
	}

	angular.module('main-module').controller('job-result-onestocktest-controller', JobResultOnestocktestController);
	
JobResultOnestocktestController.$inject = ['$scope', '$http', '$timeout', 'jobFactory', 'jobMetaFactory'];
	
function JobResultOnestocktestController($scope, $http, $timeout, jobFactory, jobMetaFactory){
	var vm = this;
	
	
	vm.result = $scope.result;
	vm.detailResults = [];
	
	initResults();
	
function initResults(){
	vm.detailResults = vm.result.detailItems;
	angular.forEach(vm.detailResults, function(resultItem){
		resultItem.price = Math.round(resultItem.price*100)/100;
	});
}
	
	
	function toggleExpandResult(result){
		result.expand = !result.expand;
	}
}

})();