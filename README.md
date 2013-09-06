SpringOne 2GX 2013 - Election Analytics using Spring XD
=======================================================

## Requirements

*Spring XD* is required to run this application.

## How to Run

Start *Spring XD*. Please ensure that you you enable JMX support using the `--enableJmx` flag. Furthermore, while not required, it is recommended to start *Spring XD* using the Redis transport `--transport redis`. That way way, collected Twitter data is not lost when shutting down the server.

	$ ./xd-container --transport redis --enableJmx
	$ ./xd-admin --transport redis

Now we need to setup the necessary Streams, Taps and Counters. For convenience we provide a shell script `keynotesetup` to streamline the process. Assuming Spring XD is running at `http://localhost:8080`, please execute:

	$ ./keynotesetup

Now you can start the web application using:

    $ mvn clean tomcat:run

The web application will startup on port **8081**

Open up your browser at `http://localhost:8081/spring-mvc-samples-elections/`
