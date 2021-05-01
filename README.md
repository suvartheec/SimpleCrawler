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
The crawler will recursively visit each link in the page, and hence the output will contain details of all the pages that are accessible from the original URL 

## Notes and consideration
1. The application will treat links with same host and path, but with different query parameter or hashbangs as the same and will not visit these links recursively.
So https://wiprodigital.com and https://wiprodigital.com?someParam=something and https://wiprodigital.com/#fragment are all considered the same. All of these will be listed in the summary but they will not be visited recursively more than once
2. The application will exactly match the hostname to determine if  a link is internal or external. Hence, for example, https://google.com and https://mail.google.com will be considered as different domains and the links will be handled accordingly
3. Currently only static images are considered as resources that are being captured by the application. However this can be extended and other resources can also be tracked (like html5 multimedia and audio tags etc)
4. Resources loaded by javascript are not captured since the application currently parses only the static contents. Same applied for links that might be accessed by the website's javascript code
5. The application currently does not take into account the content of /robots.txt of the website and crawls everything. This behaviour can be changed if we parse the robots.txt file first to know which URLs are allowed and then visit only the URLs that match those patterns

## Important
The target website, while being crawled, in some cases can consider this as suspicious activity and can block incoming requests from the originating IP. Please use with caution.

