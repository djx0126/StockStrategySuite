(function(){
	'use strict';
	
	
	angular.module('main-module').controller('transaction-dialog-controller', TransactionDialogController);
	TransactionDialogController.$inject = ['$scope', '$window', '$modal', '$timeout', 'jobFactory', '$modalInstance', 'transactions', 'strategy', 'size', 'index', 'strategyFactory', 'stategytestJobResultFactory', 'gainLine'];
	
	function TransactionDialogController($scope, $window, $modal, $timeout, jobFactory, $modalInstance, transactions, strategy, size, index, strategyFactory, stategytestJobResultFactory, gainLine){
		var transactionDialog = this;
        transactionDialog.initiated = false;
        transactionDialog.index = index;
		transactionDialog.filterTypes = prepareFilters();
		transactionDialog.selectedFilterId = 'count_more_than';
        transactionDialog.showByDay = true;
        init(strategy.name, transactions);

		transactionDialog.okClicked = $modalInstance.close;
		transactionDialog.onChange = function descriptionChanged(){transactionDialog.changed = true;};

		transactionDialog.onSaveClicked = saveStrategyProperty;
		transactionDialog.showDetailList = showDetailList;
		transactionDialog.toggleShowDetailList = toggleShowDetailList;

        transactionDialog.next = function () {
            // transactionDialog.initiated = false;
        	strategy.next(transactionDialog.index).then(function (result) {
                transactionDialog.index++;
                init(result.strategy, result.transactions)
            });
        };

        transactionDialog.previous = function () {
            // transactionDialog.initiated = false;
            strategy.previous(transactionDialog.index).then(function (result) {
                transactionDialog.index--;
                init(result.strategy, result.transactions)
            });
        };

		$scope.$watch('transactionDialog.selectedFilterId', function(newValue, oldValue) {
			if (newValue !== oldValue) {
				updateTransactionsWithFilter();
			}
		});

		$scope.$watch('transactionDialog.filterTypes[transactionDialog.selectedFilterId].value', function(newFilterValue, oldFilterValue) {
			if (newFilterValue !== oldFilterValue) {
				updateTransactionsWithFilter();
			}
		});

		$scope.$watch('transactionDialog.showStrategyProperty', function (show) {
			if (show) {
                drawGainLine();
			}
        });
		///////////////
		
		function init(strategy, transactions) {
            transactionDialog.transactions = transactionDialog.transactions || [];
            transactionDialog.transactions.length = 0;
            Array.prototype.push.apply(transactionDialog.transactions, transactions);
            transactionDialog.strategy = strategy;
            transactionDialog.strategyProperty = strategyFactory.getStrategy(transactionDialog.strategy);
            transactionDialog.changed = false;
            updateTransactionsWithFilter();
            transactionDialog.gainMaxCountTransaction = getGainMaxCountTransaction(transactions);
            transactionDialog.lossMaxCountTransaction = getLossMaxCountTransaction(transactions);
            transactionDialog.maxLossTransaction = getMaxLossTransaction(transactions);
            transactionDialog.showingDetail = {};
            transactionDialog.nextDisabled = (transactionDialog.index + 1 >= size);
            transactionDialog.previousDisabled = (transactionDialog.index - 1 < 0);
            transactionDialog.initiated = true;

            if (transactionDialog.showStrategyProperty) {
                drawGainLine();
			}
        }

        function drawGainLine() {
            $timeout(function () {
            	var orderedTransactions = _.sortBy(transactionDialog.transactions, function (t) {
					return t.buyDate;
                });

            	var strategyCreationDate = transactionDialog.strategyProperty && transactionDialog.strategyProperty.creationDate || '20170901';

                var transactionsBeforeStrategyCreation = _.filter(orderedTransactions, function (t) {
					return t.buyDate <= strategyCreationDate;
                });

                var transactionsAfterStrategyCreation = _.filter(orderedTransactions, function (t) {
                    return t.buyDate > strategyCreationDate;
                });

                var dailyAvgGain1 = _.map(transactionsBeforeStrategyCreation, function (t) {
                    return (t.avgGain+100) / 100;
                });

                var dailyAvgGain2 = _.map(transactionsAfterStrategyCreation, function (t) {
                    return (t.avgGain+100) / 100;
                });
                gainLine.clear("#visualisation");
                gainLine.drawGainLine("#visualisation", [dailyAvgGain1, dailyAvgGain2]);
            });
        }

        function getLossMaxCountTransaction(transactions) {
            var gainItems = _.filter(transactions, function (transactionDateItem) {
                return transactionDateItem.avgGain < 0;
            });
            return _.max(gainItems, 'count');
        }

		function getGainMaxCountTransaction(transactions) {
			var lossItems = _.filter(transactions, function (transactionDateItem) {
				return transactionDateItem.avgGain > 0;
            });
			return _.max(lossItems, 'count');
        }

        function getMaxLossTransaction(transactions) {
            return _.min(transactions, 'sum');
        }

		function updateTransactionsWithFilter() {
			var filterValue = transactionDialog.filterTypes[transactionDialog.selectedFilterId].value;
			var filter = transactionDialog.filterTypes[transactionDialog.selectedFilterId].filter;
			transactionDialog.transactions = sortTransactionOnDayDesc(_.filter(transactionDialog.transactions, function(transactionDateItem) {
				return filter(transactionDateItem, filterValue);
			}));
			updateStatistics(transactionDialog.transactions);
		}

		function updateStatistics(transactions) {
			transactionDialog.transactionStatistics = stategytestJobResultFactory.calcTransactionStatistics(_.flatten(_.pluck(transactions, 'list')));
			transactionDialog.dayStatistics = stategytestJobResultFactory.calcTransactionStatistics(_.values(transactions), function(transactionDateItem) {return transactionDateItem.count > 0 ? transactionDateItem.sum/transactionDateItem.count : 0;});
		}

		function sortTransactionOnDayDesc(transactions) {
			return _.sortBy(transactions, function(transactionDateItem) {
				return -parseInt(transactionDateItem.buyDate, 10);
			})
		}

		function prepareFilters() {
			return {
				'no_filter': {
					id: 'no_filter',
					label: 'No filter',
					value: undefined,
					filter: function() {return true;}
				},
				'count_more_than': {
					id: 'count_more_than',
					label: 'Count >= ',
					type: 'number',
					value: 1,
					filter: function(transactionDateItem, value) {return transactionDateItem.count >= value;}
				},
				'count_equals': {
					id: 'count_equals',
					label: 'Count = ',
					type: 'number',
					value: 1,
					filter: function(transactionDateItem, value) {return transactionDateItem.count == value;}
				},
				'start_from_date': {
					id: 'start_from_date',
					label: 'Buy Date >= ',
					type: 'text',
					value: '20140701', // start date set in AbstractStatisticData
					filter: function(transactionDateItem, value) {return transactionDateItem.buyDate >= value;}
				}
			};
		}

		function saveStrategyProperty() {
			strategyFactory.saveStrategy(transactionDialog.strategyProperty).then(function descriptionSaved() {
				transactionDialog.changed = false;
			});
		}
		
		function showDetailList(trasactionDateItem){
			var date = trasactionDateItem.buyDate;
			if (angular.isUndefined(transactionDialog.showingDetail[date])){
				return false;
			}else{
				return transactionDialog.showingDetail[date];
			}
		}
		
		function toggleShowDetailList(trasactionDateItem){
			var date = trasactionDateItem.buyDate;
			if (angular.isUndefined(transactionDialog.showingDetail[date])){
				transactionDialog.showingDetail[date] = true;
			}else{
				transactionDialog.showingDetail[date] = !transactionDialog.showingDetail[date];
			}
		}
		
		
		
	}
	

})();