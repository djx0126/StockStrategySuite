(function () {
    'use strict';

    angular.module('main-module').service('gainLine', function () {
        this.drawGainLine = function (elem, data) {
            var width=600;
            var height=300;

            // var data = [1.001,1.02,0.95,
            //     0.95,1.001,1.05,1.02,0.95,0.97,1.001,1.02,0.97,0.95,1.02,1.001];

            var cur = 1.0;
            var cData = _.map(data, function (d) {
                return cur *=d;
            });

            var max = _.max(cData);

            var dataset = _.map(cData, function (d, i) {
                return [i+1, d];
            });
            dataset = [{data: dataset}];

            var padding={top:70, right:70, bottom: 70, left:70};

            var xScale=d3.scale.linear()
                .domain([1,cData.length + 1])
                .range([0,width-padding.left-padding.right]);

            var yScale=d3.scale.linear()
                .domain([0,max*1.1])
                .range([height-padding.bottom-padding.top,0]);

            var linePath=d3.svg.line()//创建一个直线生成器
                    .x(function(d){
                        return xScale(d[0]);
                    })
                    .y(function(d){
                        return yScale(d[1]);
                    })
                    .interpolate("basis")//插值模式
                ;

            //定义两个颜色
            var colors=[d3.rgb(0,0,255),d3.rgb(0,255,0)];

            var svg=d3.select(elem)
                .append("svg")
                .attr("width",width)
                .attr("height",height);

            svg.selectAll("path")
                .data(dataset)
                .enter()
                .append("path")
                .attr("transform","translate("+padding.left+","+padding.top+")")
                .attr("d",function(d){
                    return linePath(d.data);
                    //返回线段生成器得到的路径
                })
                .attr("fill","none")
                .attr("stroke-width",3)
                .attr("stroke",function(d,i){
                    return colors[i];
                });

            var xAxis=d3.svg.axis()
                .scale(xScale)
                .ticks(5)
                .tickFormat(d3.format("d"))
                .orient("bottom");

            var yAxis=d3.svg.axis()
                .scale(yScale)
                .orient("left");

            //添加一个g用于放x轴
            svg.append("g")
                .attr("class","axis")
                .attr("transform","translate("+padding.left+","+(height-padding.top)+")")
                .call(xAxis);

            svg.append("g")
                .attr("class","axis")
                .attr("transform","translate("+padding.left+","+padding.top+")")
                .call(yAxis);
        };

        this.clear = function (elem) {
            var ele = angular.element(elem);
            ele.children().remove();
        }
    });
})();