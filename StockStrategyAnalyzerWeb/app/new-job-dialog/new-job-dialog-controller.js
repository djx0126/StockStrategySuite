(function(){
	'use strict';
	
	
	angular.module('main-module').controller('new-job-dialog-controller', NewJobDialogController);
	NewJobDialogController.$inject = ['$scope', '$window', '$modal', 'jobFactory', '$modalInstance', 'jobParams', 'JobFields', 'jobMetaFactory'];
	
	function NewJobDialogController($scope, $window, $modal, jobFactory, $modalInstance, jobParams, JobFields, jobMetaFactory){
		var vm = this;
		
		
		vm.type = jobParams.jobType;
		vm.types = initJobTypesList();
		
		
		vm.startDate;
		vm.endDate = new Date();
		vm.strategy = jobParams.strategy || null;
		vm.stockCode = jobParams.stockCode || null;

		vm.cancelClicked = cancelClicked;
		vm.okClicked = okClicked;
		
		vm.fieldNeeded = fieldNeeded;
		
		initJobParams(vm.type);
		
		///////////////
		
		function cancelClicked(){
			saveOnestocktestParam(vm.type);
			$modalInstance.close({result: 'Cancel'});
		}
		
		function okClicked(){
			var jobParams = {
				type: vm.type
			};
			var fieldsNeeded =  JobFields[vm.type];
			angular.forEach(fieldsNeeded, function(field){
				jobParams[field] = vm[field];
			});

			jobFactory.createJob(jobParams).then(function(newJob){
				saveOnestocktestParam(vm.type);
				$modalInstance.close({result: 'Ok', job: newJob});
			});
			
		}
		
		function fieldNeeded(type, field){
			if (!type){
				return false;
			}
		
			var fieldsNeeded =  JobFields[type];
			return _.indexOf(fieldsNeeded , field) >=0;
		}
		
		function formatDateStr(date){
			var year = date.getFullYear().toString();
			var month = (parseInt(date.getMonth(), 10)+1).toString();
			month = month.length<2? '0'+month: month;
			
			var date = date.getDate().toString();
			date = date.length<2? '0'+date: date;
			return year+month+date;
		}
		
		function initEndDate(){
			var date = new Date();
			vm.endDate = formatDateStr(date);
		}

		function initOnestocktestParam(){
			vm.startDate = $window.localStorage.onestocktest_startDate || '20070101';
			vm.strategy = vm.strategy || $window.localStorage.onestocktest_strategy || '';
			vm.stockCode = vm.stockCode || $window.localStorage.onestocktest_stockCode || '';
		}
		
		function initStrategytestParam(){
			vm.startDate = $window.localStorage.strategytest_startDate || '20070101';
		}
		
		function initJobParams(type){
			initEndDate();
			
			switch(type){
			case 1:
				initOnestocktestParam();
				break;
			case 2:
				break;
			case 3:
				initStrategytestParam();
				break;
			}
		}
		
		function saveOnestocktestParam(){
			if (vm.startDate)$window.localStorage.onestocktest_startDate = vm.startDate;
			if (vm.strategy)$window.localStorage.onestocktest_strategy = vm.strategy;
			if (vm.stockCode)$window.localStorage.onestocktest_stockCode = vm.stockCode;
		}
		
		function saveStrategytestParam(){
			if (vm.startDate)$window.localStorage.strategytest_startDate = vm.startDate;
		}
		
		function saveParamsToLocalStorage(type){
			switch(type){
			case 1:
				saveOnestocktestParam();
				break;
			case 2:
				break;
			case 3:
				saveStrategytestParam();
				break;
			}
		
			
		}
		
		function initJobTypesList(){
			var jobTypes = jobMetaFactory.getJobTypes();
			var types = [];
			angular.forEach(jobTypes, function(typeName, typeId){
				types.push({
					id: typeId,
					name: typeName
				});
			});
			return types;
		}

	}
	

})();