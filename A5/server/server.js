var storage = {
"/2131165199": [
	{
	timestamp: (new Date().getTime())+1000*3600, 
	data: "{\"id\":\"aa4a8552-61c3-408c-add6-daf6d6a2d910\",\"displayname\":\"John\",\"fullname\":\"John Spartan\",\"phone\":\"9999999\"}"
	},
	{
	timestamp: (new Date().getTime())+1000*3600,
	data: "{\"id\":\"da4a8612-81c3-408c-add6-daf6d6a2d910\",\"displayname\":\"mike\",\"email\":\"mike@data.tmp\"}"
	},
	{
	timestamp: (new Date().getTime())+1000*3600, 
	data: "{\"id\":\"5a4a8722-91c3-408c-add6-daf6d6a2d910\",\"displayname\":\"dave\",\"fullname\":\"David Davidson\",\"phone\":\"123 44 22\",\"email\":\"d@test.org\"}"
	},
]
};

var halt=false;

// prune daemon
(function remove_old_entries(){
	halt=true;
		for(var key in storage)
		{
			if( storage.hasOwnProperty( key ) )
			{
				var l = storage[key];
				var newlist = [];
				var timestamp = new Date().getTime();
				for( var entry in l )
				{
					if( l.hasOwnProperty( entry ) )
					{
						if( l[entry].timestamp + 1000 * 60 * 5 > timestamp )
						{
							newlist.push( l[entry] );
						}
					}
				}
				
				storage[key]=newlist;
			}
		}

	halt=false;

	setTimeout( remove_old_entries, 1000 * 60 ); // wait a minute
})();

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
				if( halt )
				{
					response.writeHead( 503, "Wait for gc", { 'Retry-After': 1 } );
				}
				else
				{
					storage[label].push( { timestamp: new Date().getTime(), data: data } );
					console.log( "["+label+"] stored "+data );
					response.writeHead(200, "OK", {'Content-Type': 'text/html'});
				}
				response.end();
			});
		} else {
			response.writeHead(200, {"Content-Type": "application/json"});

			if(storage.hasOwnProperty(label))
			{
				var arr = storage[label];
				var parsed = [];
				for( var i = 0; i< arr.length; i++ )
				{
					parsed.push(JSON.parse(arr[i].data));
				}
				var str = JSON.stringify( parsed );
				console.log("["+label+"] fetched " + str);
				response.write(str);
			}
			else
			{
				response.write("[]");
			}
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
