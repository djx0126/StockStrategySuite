(function(){
	'use strict';
	
	
	angular.module('main-module').controller('transaction-dialog-controller', TransactionDialogController);
	TransactionDialogController.$inject = ['$scope', '$window', '$modal', 'jobFactory', '$modalInstance', 'transactions', 'strategy', 'strategyFactory', 'stategytestJobResultFactory'];
	
	function TransactionDialogController($scope, $window, $modal, jobFactory, $modalInstance, transactions, strategy, strategyFactory, stategytestJobResultFactory){
		var transactionDialog = this;
		transactionDialog.strategy = strategy;
		transactionDialog.strategyProperty = strategyFactory.getStrategy(strategy);

		transactionDialog.filterTypes = prepareFilters();
		transactionDialog.selectedFilterId = 'count_more_than';
		updateTransactionsWithFilter();

		transactionDialog.okClicked = $modalInstance.close;
		transactionDialog.changed = false;
		transactionDialog.onChange = function descriptionChanged(){transactionDialog.changed = true;};
		transactionDialog.showingDetail = {};
		transactionDialog.onSaveClicked = saveStrategyProperty;
		transactionDialog.showDetailList = showDetailList;
		transactionDialog.toggleShowDetailList = toggleShowDetailList;
		transactionDialog.showTransactionStatistics = true;

		transactionDialog.gainMaxCountTransaction = getGainMaxCountTransaction(transactions);
		transactionDialog.lossMaxCountTransaction = getLossMaxCountTransaction(transactions);
		transactionDialog.maxLossTransaction = getMaxLossTransaction(transactions);

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
		///////////////

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
			transactionDialog.transactions = sortTransactionOnDayDesc(_.filter(transactions, function(transactionDateItem) {
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