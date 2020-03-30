var http = require('http');
var url = require('url');
var fs = require('fs');


http.createServer(function (req, res) {
  var q = url.parse(req.url, true);
  console.log(q.pathname);
  if(q.pathname == '/'){
   q.pathname = '/index.html';
  }
  var filename = "." + q.pathname;



  fs.readFile(filename, function(err, data) {
    if (err) {
      res.writeHead(404);
      return res.end("404 Not Found");
    } 
    res.writeHead(200);
    res.write(data);
    return res.end();
  });
}).listen(8080); 