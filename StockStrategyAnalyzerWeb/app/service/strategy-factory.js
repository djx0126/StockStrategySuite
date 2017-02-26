(function() {
    'use strict';

    angular.module('main-module').factory('strategyFactory', ['$http', '$q', 'urlService', '$rootScope', function($http, $q, urlService, $rootScope){
        var strategyFactory = {
            loadStrategies: loadStrategies,
            getStrategies: getStrategies,
            getStrategy: getStrategy,
            saveStrategy: saveStrategy
        };
        var strategiesCache;

        function getStrategies() {
            return strategiesCache;
        }

        function getStrategy(name) {
            return strategiesCache[name];
        }

        function loadStrategies() {
            var deferred = $q.defer();
            var url = urlService.getBaseUrl();
            url += '/strategies';
            $http.get(url).success(function(strategies){
                strategiesCache = strategies;
                deferred.resolve(strategies);
            });
            return deferred.promise;
        }

        function saveStrategy(strategy) {
            var deferred = $q.defer();
            var url = urlService.getBaseUrl();
            url += '/strategies';
            $http.post(url, strategy).success(function(){
                deferred.resolve();
            });
            return deferred.promise;
        }

        return strategyFactory;
    }]);
})();
