var mainModule = angular.module('main-module', ['ui.router', 'ui.bootstrap']);

mainModule.constant('BackEndPort', '8081');

mainModule.constant('BackEndServiceName', 'StockStrategyAnalyzer');


mainModule.constant('JobFields', {
    1: ['startDate', 'endDate', 'strategy', 'stockCode'], // one stock test
    2: ['endDate'], // FindToBuy
    3: ['startDate', 'endDate'] // StrategyTest
});