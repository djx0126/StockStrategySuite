(function(){
	'use strict';
	
	var mainModule = angular.module('main-module');
	mainModule.factory('jobMetaFactory', ['$http', '$q', 'urlService', function($http, $q, urlService){
		var jobMetaFactory = {};
		
		
		var jobTypes;

		function getJobTypes(){
			var deferred = $q.defer();	
			jobTypes = {};
			var url = urlService.getBaseUrl();
			url += '/jobtypes';
	
			
			$http.get(url).success(function(data){
				angular.forEach(data, function(value, key){
					jobTypes[key] = value;
				});
				deferred.resolve(jobTypes);
			});
			
			return deferred.promise;
		};
		
		
		var jobStates;
		function getJobStates(){
			var deferred = $q.defer();	
			jobStates = {};
			var url = urlService.getBaseUrl();
			url += '/jobstates';
			$http.get(url).success(function(data){
				angular.forEach(data, function(value, key){
					jobStates[key] = value;
				});
				deferred.resolve(jobStates);
			});
			return deferred.promise;
		};
		
		jobMetaFactory.initialize = function initialize(){
			var jobTypesPromise = getJobTypes();
			var jobStatessPromise = getJobStates();
			
			return $q.all([jobTypesPromise, jobStatessPromise]);
		};
		
		jobMetaFactory.getJobTypes = function(){
			return jobTypes;
		};
		
		jobMetaFactory.getJobStates = function(){
			return jobStates;
		};

		return jobMetaFactory;
	
	}] );

})();