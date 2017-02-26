(function(){
	'use strict';
	
	var mainModule = angular.module('main-module');
	mainModule.factory('jobFactory', ['$http', '$q', 'urlService', '$rootScope', function($http, $q, urlService, $rootScope){
		var jobFactory = {};
		
		jobFactory.getJobList = function(){
			var deferred = $q.defer();
			var url = urlService.getBaseUrl();
			url += '/jobs';
			$http.get(url).success(function(jobs){
				var jobs = _.sortBy(jobs, function(job){
					return 0 - parseInt(job.id, 10);
				});
				
				deferred.resolve(jobs);
			});
			return deferred.promise;
		};
		
		jobFactory.getJobResult = function(job){
			var deferred = $q.defer();
			var url = urlService.getBaseUrl();
			url += '/result/' + job.id;
			$http.get(url).success(function(result){
				
				
				deferred.resolve(result);
			});
			return deferred.promise;
		};
		
		jobFactory.deleteJobResult = function(job){
			var deferred = $q.defer();
			var url = urlService.getBaseUrl();
			url += '/result/' + job.id;
			$http({
				url: url,
				method: 'DELETE'
			}).success(function(result){
				deferred.resolve(result);
			});
			return deferred.promise;
		};
		
		jobFactory.createJob = function(job){
			var jobParams = '';
			jobParams += 'type='+job.type;
			if (job.startDate){jobParams += '&startDate='+job.startDate;}
			if (job.endDate){jobParams += '&endDate='+ job.endDate;}
			if (job.strategy){jobParams += '&strategy='+ job.strategy;}
			if (job.stockCode){jobParams += '&stockCode='+ job.stockCode;}
		
			var deferred = $q.defer();
			var url = urlService.getBaseUrl();
			url += '/job' + '?' +jobParams;
			$http({
			    method: 'POST',
			    url: url,
			    headers:{
		        'Content-Type': 'application/x-www-form-urlencoded'
		    	}
		     } ).success(function(newJob){
		    	$rootScope.$broadcast('jobCreated', newJob);
				deferred.resolve(newJob);
			});
			return deferred.promise;
		};
		
		jobFactory.deleteJob = function(job){
			var deferred = $q.defer();
			var url = urlService.getBaseUrl();
			url += '/job/' + job.id;
			$http({
				url: url,
				method: 'DELETE'
			}).success(function(result){
				deferred.resolve(result);
			});
			return deferred.promise;
		};
		
		
		return jobFactory;
	
	}] );

})();