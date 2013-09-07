SpringOne 2GX 2013 - Election Analytics using Spring XD
=======================================================

## Requirements

*Spring XD* is required to run this application.

## How to Run

Start *Spring XD*. Please ensure that you you enable JMX support using the `--enableJmx` flag. Furthermore, please use Redis for analytics. That way way, collected Twitter data is not lost when shutting down the server.

	$ ./xd-singlenode --transport local --analytics redis --enableJmx

Now we need to setup the necessary Streams, Taps and Counters. For convenience we provide a shell script `keynotesetup` to streamline the process. Assuming Spring XD is running at `http://localhost:8080`, please execute:

	$ ./keynotesetup

This will create the following *Streams*:

	xd:> stream create --name ts --definition "ts --query='#obama OR #romney OR #bieber' | object-to-json | file"
	xd:> stream create --name obamatap --definition ":tap:ts >  filter --expression=payload.entities.hashTags.![text.toLowerCase(T(java.util.Locale).ENGLISH)].contains('obama') | aggregatecounter --name=obamaAggregatedCounter"
	xd:> stream create --name romneytap --definition ":tap:ts >  filter --expression=payload.entities.hashTags.![text.toLowerCase(T(java.util.Locale).ENGLISH)].contains('romney') | aggregatecounter --name=romneyAggregatedCounter"
	xd:> stream create --name biebertap --definition ":tap:ts >  filter --expression=payload.entities.hashTags.![text.toLowerCase(T(java.util.Locale).ENGLISH)].contains('bieber') | aggregatecounter --name=bieberAggregatedCounter"
	xd:> stream create --name tweetStream --definition "twitterstream | file"
	xd:> stream create --name tweetStreamTagCount --definition ":tap:tweetStream > field-value-counter --fieldName=entities.hashtags.text --counterName=hashtags"

Now you can start the web application using:

    $ mvn clean tomcat:run

The web application will startup on port **8081**

Open up your browser at `http://localhost:8081/spring-mvc-samples-elections/`
