(function(){
	'use strict';
	
	
	var mainModule = angular.module('main-module');
	mainModule.controller('job-list-controller', function($scope, $http, $timeout, jobFactory, jobMetaFactory){
		$scope.loading = false;
		$scope.jobs = [];
		$scope.jobTypes = jobMetaFactory.getJobTypes();
		$scope.jobStates = jobMetaFactory.getJobStates();
		
		function needToTrackOnJob(jobs){
			return _.any(jobs, function(job){
				return job.state != '2' && job.state != '3';
			});
		}
		
		function trackOnJob(time){
			var timer = null;
			(function(){
				if (timer){
					$timeout.cancel(timer);
				}
				timer = $timeout($scope.loadJobList, time);
			})();
		}

		$scope.loadJobList = function(){
			jobFactory.getJobList().then(function(jobs){
				$scope.jobs = jobs;
				
				if ($scope.context.activeJob){
					var activeJobUpdated = _.find(jobs, {id: $scope.context.activeJob.id});
					$scope.setActiveJob(activeJobUpdated);
				}
				

				if (needToTrackOnJob(jobs)){
					trackOnJob(5000);
				}else{
					trackOnJob(2*60*1000);
				}
			});
		};
		
		$scope.activeJob = function(job){
			$scope.setActiveJob(job);
		};
		$scope.isJobActive = function(job){
			return $scope.context.activeJob && $scope.context.activeJob.id === job.id;
		}
		
		$scope.removeJob = function(job){
			jobFactory.deleteJobResult(job).then(function(){
				jobFactory.deleteJob(job).then(function(){
					$scope.setActiveJob(null);
					$scope.loadJobList();
				});
			});
		};
		
        $scope.$on('jobCreated', $scope.loadJobList);
		
		$scope.loadJobList();
	});

})();