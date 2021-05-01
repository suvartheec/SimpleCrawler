# SimpleCrawler
This is a simple crawler written in Java

This is built on Spring boot and uses JSoup library to connect to and parse HTML pages 


## To build from source code:
1. Either download as zip and expand, or clone the project(using IDE or git client) in your machine.
2. If using an IDE, you can run as Spring boot application in IDE
3. Alternatively execute the following from inside the project root:
> mvn clean package spring-boot:repackage
> java -jar target/simplecrawler-0.0.1-SNAPSHOT.jar

## Using the application
1. Open browser and go to: http://localhost:8080/rest/crawler/crawl
The application will crawl the url http://suvartheec.github.io/ by default
2. To crawl a specific website (for ex: https://wiprodigital.com), hit the following url: http://localhost:8080/rest/crawler/crawl?url=https://wiprodigital.com
3. Likewise, replace the `url` parameter with any site


## The output
The application will return a JSON response in a key-value structure (i.e. a Map)
Each key is the URLs accessible from the supplied (or default) URL.
Each value is the a summary of that link (the key). It wil have 3 lists: 
1. List of all the internal URLs present in that page
2. List of all the external URLs present in that page
3. List of all the static images present in that page

Apart from that the summary will have its own URL and the URL of that of the parent through which it was accessed



