var http = require('http');

exports.crud = function (req, res) {
    // ��ȡ�����headers��ȥ��host��connection
    var getHeader = function (req) {
        var ret = {};
        for (var i in req.headers) {
            if (!/host|connection/i.test(i)) {
                ret[i] = req.headers[i];
            }
        }
        return ret;
    };

    // ��ȡ�����·��
    var getPath = function (req) {
        var url = req.url;
        if (url.substr(0, 7).toLowerCase() === 'http://') {
            var i = url.indexOf('/', 7);
            if (i !== -1) {
                url = url.substr(i);
            }
        }
        return url;
    };

    var newHost = 'localhost'; //req.headers.host.replace('8081', '8080');
    var newPort = 8080;
    console.log('receive ' + req.method + ':' + req.url + ',request to:' + newHost + getPath(req));

    var opt = {
        host: newHost,
        port: newPort,
        path: getPath(req),
        method: req.method,
        headers: getHeader(req)
    };
    var req2 = http.request(opt, function (res2) {
        res.writeHead(res2.statusCode, res2.headers);
        res2.pipe(res);
        res2.on('end', function () {
        });
    });

    if (/POST|PUT/i.test(req.method)) {
        if (!req.readable) {
            req2.write(JSON.stringify(req.body));
            req2.end();
        } else {
            req.pipe(req2);
        }
    } else {
        console.log('end req2');
        req2.end();
    }
    req2.on('error', function (err) {
        console.log('get error: ' + err.toString());
        res.end(err.stack);
    });
};