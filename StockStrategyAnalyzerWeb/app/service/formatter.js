(function(){
	angular.module('main-module').filter('formatInt2' , function(){
		return function(data){
			return Math.round(100 * data)/100;
		}
	}).filter('formatInt3' , function(){
		return function(data){
			return Math.round(1000 * data)/1000;
		}
	});
})();