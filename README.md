WebServer
=========

java -jar [jar-name] [port] [log level] [number threads]

[jar-name]:
	The name of the WebServer jar.

[port]
	An integer value representing the port to accept incoming requests
	for the proxy server. Default is 1337
	
[log-level]
    One of the following values:
    - Error
    - Warn
    - Info (Default)
    - Debug
    - Trace
    
[number threads]
    The number of threads to use in the thread pool. Defaults to 10.

Notes:
If the [port], [log level], and [number threads] arguments are ignored, 
the system defaults to localhost (127.0.0.1), port number 1337, INFO logging level, and 10 threads.

There were a few assumptions made according to the language of the requirements document. 
First, the web server is a separate component from the javascript application, and therefore
do not interact with each other. Second, the only libraries used were logging and commons-io for
a few simple tasks. I assumed using libraries would trivialize the assignment, and therefore kept it to a minimum.
Lastly, the server is designed to be run from the commandline. Using servlets would again allow many facilities,
which would in turn trivialize the assignment. 
	