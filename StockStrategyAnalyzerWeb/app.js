
/**
 * Module dependencies.
 */

var express = require('express')
  , routes = require('./routes')
  , user = require('./routes/user')
  , stockStrategyAnalyzer = require('./routes/stockStrategyAnalyzer')
  , http = require('http')
  , path = require('path');

var bodyParser = require('body-parser');

var app = express();

var args = process.argv.slice(2);
var inport = parseArg(args, 'inport') || 8081;
var outport = parseArg(args, 'outport') || 8080;

var port = inport;

var appDir = __dirname + '/app';

// all environments
app.set('port', inport);
app.set('views', appDir);
app.set('view engine', 'jade');
// app.use(express.favicon());
// app.use(express.logger('dev'));
// app.use(express.bodyParser());
// app.use(express.methodOverride());
// app.use(app.router);
app.use(express.static(appDir));
//app.use(bodyParser.json());

// development only
// if ('development' == app.get('env')) {
  // app.use(express.errorHandler());
// }

app.get('/StockStrategyAnalyzer/*', stockStrategyAnalyzer.crud.bind(null, outport));
app.post('/StockStrategyAnalyzer/*', stockStrategyAnalyzer.crud.bind(null, outport));
app.delete('/StockStrategyAnalyzer/*', stockStrategyAnalyzer.crud.bind(null, outport));

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});

function parseArg(args, argName) {
    var values = args.map(function (s) {
            return s.split('=');
        }).filter(function (a) {
            return a.length == 2 && a[0] === argName;
        }).map(function (a) {
        return a[1];
    });
    return values && values.length > 0 && values[0];
}
