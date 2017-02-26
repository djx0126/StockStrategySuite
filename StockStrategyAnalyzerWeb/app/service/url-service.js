(function(){
	'use strict';
	
	var mainModule = angular.module('main-module');
	mainModule.factory('urlService', ['$location', '$q', 'BackEndPort', 'BackEndServiceName', function($location, $q, BackEndPort, BackEndServiceName){
		var urlService = {};
		
		
		urlService.getBaseUrl = function(){
			var url;
			function getBackEndServiceUrl(){
				var reg = new RegExp($location.port()+'.*');
				var newUrl = $location.absUrl().replace(reg, BackEndPort);
				newUrl += '/' + BackEndServiceName;
				return newUrl;
			}
			
			if (!url){
				url = getBackEndServiceUrl();
			}

			return url;
		};
		
		
		return urlService;
	
	}] );

})();