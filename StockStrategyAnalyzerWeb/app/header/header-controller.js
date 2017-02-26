(function(){
	'use strict';
	
	
	angular.module('main-module').controller('header-controller', HeaderController);
	HeaderController.$inject = ['$scope', '$modal', 'jobMetaFactory'];
	
	function HeaderController($scope, $modal, jobMetaFactory){
		var vm = this;
		vm.newSingleStockTestJob = newSingleStockTestJob;
		vm.newFindToBuyJob = newFindToBuyJob;
		vm.newStrategyTestJob = newStrategyTestJob;
		
		///////////////

		function newSingleStockTestJob(){
			newJob(1);
		}

		function newFindToBuyJob(){
			newJob(2);
		}
		
		function newStrategyTestJob(){
			newJob(3);
		}
		
		function newJob(type){
			var dialog = $modal.open({
	            templateUrl: 'new-job-dialog/new-job-dialog.html',
	            backdrop: 'static',
	            controller: 'new-job-dialog-controller',
	            controllerAs: 'vm',
	            resolve:{
					jobParams: function(){
	            		return {
	            			jobType: type
	            		};
	            	}
				}
	        });
			
	        dialog.result.then(function (result) {  
                 console.log(result);  
            });
		}
	}
	

})();