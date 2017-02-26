(function(){
	'use strict';
	
	
	var mainModule = angular.module('main-module');
	mainModule.controller('main-controller', function($q, $scope, jobMetaFactory, strategyFactory){
		$scope.name = 'Stock';

		$q.all([jobMetaFactory.initialize(), strategyFactory.loadStrategies()]).then(function(){
			$scope.initialized = true;
		});
		
		$scope.context = {};
		
		$scope.setActiveJob = function(job){
			$scope.context.activeJob = job;
		};
	});

})();