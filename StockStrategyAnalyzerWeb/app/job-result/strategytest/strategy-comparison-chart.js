(function(){
	angular.module('main-module').directive('strategyComparisonChart', StrategyComparisonChart);

	function StrategyComparisonChart(){
		return {
			restrict: 'E',
			replace: true,
			scope: {
				data: '='
			}, 
			template: "<div id='chartdiv'  class='strategy-comparison-chart'></div",
			controller: StrategyComparisonChartController,
			controllerAs: 'vm'
		};
		
		StrategyComparisonChartController.$inject = ['$scope', '$element'];
		
		function StrategyComparisonChartController($scope, $element){
			var vm = this;
			vm.data = $scope.data;
			

			var strategyCatelog = _.pluck(vm.data, 'strategy');
			var avgGain = _.pluck(vm.data, 'transactionAvgGain');
			var rate = _.pluck(vm.data, 'transactionRate');
			var accuracy = _.pluck(vm.data, 'transactionAccuracy');
			
			var score = _.pluck(vm.data, 'transScore');
			
			
			
			$element.highcharts({
        chart: {
            //zoomType: 'xy'
			height: 600,
            alignTicks: false
        },
        title: {
            text: 'Strategy Comparison'
        },
        subtitle: {
            //text: 'Source: WorldClimate.com'
        },
        xAxis: [{
            categories: strategyCatelog,
            labels: {
            	//align: 'center',
            	y:5,
            	x: -5,
        		rotation: 90
        	}
        }],
        yAxis: [{ // avgGain
            labels: {
                format: '{value} %',
                style: {
                    color: Highcharts.getOptions().colors[0]
                }
            },
            title: {
                text: 'Avg Gain',
                style: {
                    color: Highcharts.getOptions().colors[0]
                }
            },
            
            min: 0.1,
            tickInterval: 0.1,
            type: 'logarithmic'

        }, { // rate
            gridLineWidth: 0,
            title: {
                text: 'Rate',
                style: {
                    color: Highcharts.getOptions().colors[1]
                }
            },
            labels: {
                format: '{value}',
                style: {
                    color: Highcharts.getOptions().colors[1]
                }
            },
            min: 0.5,
            tickInterval: 0.1,
            type: 'logarithmic'

        }, { // accuracy
            gridLineWidth: 0,
            title: {
                text: 'Accuracy',
                style: {
                    color: Highcharts.getOptions().colors[2]
                }
            },
            labels: {
                format: '{value} %',
                style: {
                    color: Highcharts.getOptions().colors[2]
                }
            },
            max: 100,
            min: 0,
            opposite: true
        }, { // score
        	gridLineWidth: 0,
        	title: {
            	text: 'Score',
            	style: {
                	color: Highcharts.getOptions().colors[3]
            	}
        	},
        	labels: {
        		format: '{value}',
        		style: {
                	color: Highcharts.getOptions().colors[3]
            	}
        	},
//        	max: 100,
//        	min: 0,
        	opposite: true
        }],
        tooltip: {
            shared: true
        },
        legend: {
            layout: 'vertical',
            align: 'left',
            x: 10,
            verticalAlign: 'top',
            y: 500,
            floating: true,
            backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
        },
        series: [{
            name: 'Avg Gain',
            type: 'column',
            yAxis: 0,
            data: avgGain,
            tooltip: {
                valueSuffix: ' %'
            }

        }, {
            name: 'Rate',
            type: 'spline',
            yAxis: 1,
            data: rate,
            marker: {
                enabled: false
            },
            dashStyle: 'shortdot',
            tooltip: {
                //valueSuffix: ' mb'
            }

        }, {
            name: 'Accuracy',
            type: 'spline',
            data: accuracy,
            yAxis: 2,	
            tooltip: {
                valueSuffix: ' %'
            }
        },{
            name: 'Score',
            type: 'spline',
            data: score,
            yAxis: 3,	
            tooltip: {
            	//valueSuffix: ' %'
        	}
        } 
        ]
    });
			
		}
	}
})();