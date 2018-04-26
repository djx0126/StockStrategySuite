(function(){
	'use strict';
	
	angular.module('main-module').factory('transactionsFactory', ['$http', '$q', '$modal', 'urlService', function($http, $q, $modal, urlService){
		var url = urlService.getBaseUrl() + '/transactions';
		var transactionListMap = {};
		
		var service = {
			getTransactionList: getTransactionList,
            loadTransactions: loadTransactions,
            showTransactionDialog: showTransactionDialog
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

        function showTransactionDialog(jobId, resultItem, index, results){
            var dialog = $modal.open({
                templateUrl: 'job-result/strategytest/transaction-dialog/transaction-dialog.html',
                backdrop: 'static',
                controller: 'transaction-dialog-controller',
                controllerAs: 'transactionDialog',
                windowClass: 'strategy-transaction-dialog',
                resolve:{
                    size: function () {
                        return results.length;
                    },
                    index: function () {
                        return index;
                    },
                    transactions: function(){
                        return loadTransactions(jobId, resultItem);
                    },
                    strategy: function(){
                        return {
                            'name': resultItem.strategy,
                            'next': function (index) {
                                if (index + 1 < results.length) {
                                    var nextResultItem = results[index + 1];
                                    return loadTransactions(jobId, nextResultItem).then(function (transactions) {
                                        return {
                                            'index': index+1,
                                            'strategy': nextResultItem.strategy,
                                            'transactions': transactions
                                        }
                                    });
                                }
                            },
                            'previous': function (index) {
                                if (index - 1 >= 0) {
                                    var preResultItem = results[index - 1];
                                    return loadTransactions(jobId, preResultItem).then(function (transactions) {
                                        return {
                                            'index': index-1,
                                            'strategy': preResultItem.strategy,
                                            'transactions': transactions
                                        }
                                    });
                                }
                            }
                        };
                    }
                }
            });
        }

        function loadTransactions(jobId, resultItem) {
            if (resultItem.transactions) {
                return $q.when(resultItem.transactions);
            }
            return getTransactionList(jobId, resultItem.strategy).then(function(rawTransactionList){
                resultItem.transactions = initTransactionList(rawTransactionList);
                resultItem.lastGainDate = _(resultItem.transactions).filter(function(transactionByDate) {return transactionByDate.avgGain > 0;}).map('buyDate').max().value();
                return resultItem.transactions;
            });
        }

        function initTransactionList(rawTransactionList){
            var resultByDateMap = {};
            angular.forEach(rawTransactionList, function(transcation){
                var buyDate= transcation.buyDate;
                var gain = (transcation.sellPrice-transcation.buyPrice)*100/transcation.buyPrice;
                if (!resultByDateMap[buyDate]){
                    resultByDateMap[buyDate] = {
                        buyDate: buyDate,
                        count:0,
                        sum:0,
                        list:[]
                    };
                }
                var dateList = resultByDateMap[buyDate];
                dateList.count++;
                dateList.sum += gain;
                dateList.list.push(transcation);
            });

            var result = [];
            angular.forEach(resultByDateMap, function(dateList){
                dateList.avgGain = dateList.count>0?dateList.sum/dateList.count:0;
                result.push(dateList);
            });
            result = _.sortBy(result, 'buyDate'); // asc

            calcMaAvgGain(result);

            return result;

            function calcMaAvgGain(result) {
                _.forEach(result, function (dateResult, index) {
                    var year = dateResult.buyDate.substr(0, 4);
                    var month = dateResult.buyDate.substr(4, 2);
                    var day = dateResult.buyDate.substr(6, 2);
                    var buyDate = new Date(year + '-' + month + '-' + day);
                    buyDate.setMonth( buyDate.getMonth() - 1);
                    var month = buyDate.getMonth() + 1;
                    month = month < 10 ? '0' + month : month;
                    var date = buyDate.getDate();
                    date = date < 10 ? '0' + date : date;
                    var lastMonth = '' + buyDate.getFullYear() + month + date;

                    var maCount = 0;
                    var maSum = 0;
                    for (var i = index; i >=0 ; i--) {
                        if (result[i].buyDate < lastMonth) {
                            break;
                        }
                        maCount++;
                        maSum += result[i].avgGain;
                    }
                    dateResult.maAvgGain = maSum / maCount;
                });
            }
        }

	}]);
})();