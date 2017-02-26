
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


var port = 8081;

var appDir = __dirname + '/app';

// all environments
app.set('port', port);
app.set('views', appDir);
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(appDir));
//app.use(bodyParser.json());

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/StockStrategyAnalyzer/*', stockStrategyAnalyzer.crud);
app.post('/StockStrategyAnalyzer/*', stockStrategyAnalyzer.crud);
app.delete('/StockStrategyAnalyzer/*', stockStrategyAnalyzer.crud);

http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
});
