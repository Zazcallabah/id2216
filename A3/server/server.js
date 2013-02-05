var storage = {};

// web server paste
var http = require("http"),
    url = require("url"),
    path = require("path"),
    fs = require("fs")
    port = process.argv[2] || 8888;
	
var requesthandler = function( request, response ) {
	var pathname = url.parse( request.url ).pathname;

	if(pathname.substring(0,4) === "/api" ) {
		var label = "default";
		if( pathname.length > 4 )
			label = pathname.substring(4);
		if( request.method === "POST" ) {
			var data = "";
			request.on('data', function(chunk) {
				data = data + chunk.toString();
			});
			request.on('end', function() {
				if(!storage.hasOwnProperty(label))
					storage[label] = [];
				storage[label] += JSON.parse( data );
				console.log( "["+label+"] stored "+data );
				response.writeHead(200, "OK", {'Content-Type': 'text/html'});
				response.end();
			});
		} else {
			response.writeHead(200);
			if(storage.hasOwnProperty(label))
				response.write( JSON.stringify( storage[label] ) );
			response.end();
			return;
		}
	} else {
		var uri = "../html" + pathname;
		var filename = path.join(process.cwd(), uri);
		path.exists(filename, function(exists) {
			if(!exists) {
				console.log("404: "+pathname);
				response.writeHead(404, {"Content-Type": "text/plain"});
				response.write("404 Not Found\n");
				response.end();
				return;
			}

			if (fs.statSync(filename).isDirectory()) 
				filename += '/index.html';

			fs.readFile(filename, "binary", function(err, file) {
				if(err) {        
					console.log("500: "+pathname);
					response.writeHead(500, {"Content-Type": "text/plain"});
					response.write(err + "\n");
					response.end();
					return;
				}
				console.log("200: "+pathname);
				response.writeHead(200);
				response.write(file, "binary");
				response.end();
			});
		});
	}
};

http.createServer( requesthandler ).listen(parseInt(port, 10));

console.log("Static file server running at\n  => http://localhost:" + port + "/\nCTRL + C to shutdown");

process.stdin.resume();
process.stdin.setEncoding('utf8');
process.stdin.on('data', function (chunk) {
	current_state = chunk;
});