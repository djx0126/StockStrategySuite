(function(){
	'use strict';
	angular.module('main-module').directive('arrow', function(){
		return {
			template: '<svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%" version="1.1">'+
							'<path d="M20 0 L0 28 L16 28 L16 90 L24 90 L24 28 L40 28 Z"/>'+
							'</svg>',
			scope: {
				rise: '@'
			}
		};
	});
})();