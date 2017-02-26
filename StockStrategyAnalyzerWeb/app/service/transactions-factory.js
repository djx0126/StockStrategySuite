(function(){
	'use strict';
	
	angular.module('main-module').factory('transactionsFactory', ['$http', '$q', 'urlService', function($http, $q, urlService){
		var url = urlService.getBaseUrl() + '/transactions';
		var transactionListMap = {};
		
		var service = {
			getTransactionList: getTransactionList
		};
		
		return service;
		
		///////////////
		function getTransactionList(jobId, strategy){
			if (transactionListMap[jobId] && transactionListMap[jobId][strategy]){
				return $q.when(transactionListMap[jobId][strategy]);
			}
			
			if (!transactionListMap[jobId]){
				transactionListMap[jobId] = {};
			}
			var deferred = $q.defer();
			var reqUrl = url + '?' +'jobId='+jobId+'&strategy='+strategy;
			$http.get(reqUrl).success(function(transactionList){
				transactionListMap[jobId][strategy] = transactionList;
		    	deferred.resolve(transactionList);
			});
			return deferred.promise;
		}
		
	}]);
})();