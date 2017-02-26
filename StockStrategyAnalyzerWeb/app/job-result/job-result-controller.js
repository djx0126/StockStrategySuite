(function(){
	'use strict';
	
	
	angular.module('main-module').directive('jobResult', JobDirective);
	
	function JobDirective(){
		return {
			restrict: 'E',
			scope: {
				job: '='
			},
			templateUrl: 'job-result/job-result.html',
			controller: 'job-result-controller',
			controllerAs: 'vm'
		};
	}
	
	angular.module('main-module').controller('job-result-controller', JobResultController);
	
	JobResultController.$inject = ['$scope', '$http', '$timeout', 'jobFactory', 'jobMetaFactory'];
	
	function JobResultController($scope, $http, $timeout, jobFactory, jobMetaFactory){
		var vm = this;
		var jobTypes = jobMetaFactory.getJobTypes();
		
		vm.job = $scope.job;
		vm.resultReady = false;
		vm.result = null;

		$scope.$watch('vm.job', function (job) {
			if (job && job.state === 2) {
				initLoadResult();
			}
		}, true);

		$scope.$watch('job', function (job) {
			vm.job = $scope.job;
		});

		//initLoadResult();
		
		function initLoadResult(){
			vm.resultReady = false;
			// load result
			jobFactory.getJobResult($scope.job).then(function(result){
				vm.resultReady = true;
				vm.result = result;
			});
		}
		
		
	}
	
	angular.module('main-module').controller('job-result-controller', JobResultController);

})();