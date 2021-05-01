package com.practice.simplecrawler.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SimpleCrawlerUtil {
	private Logger logger = LogManager.getLogger();

	/**
	 * @param urlString to crawl
	 * @return the result as Map of urls that are accessible from the provided input and the summary of each url
	 * @throws IOException
	 */
	public Map<String, PageContents> crawl(String urlString) throws IOException {
		return crawl(urlString, null, new ConcurrentHashMap<>());
	}

	/**
	 * @param urlString to crawl recursively
	 * @param parent of the current page that is being analyzed
	 * @param pageContentsMap to hold the contents
	 * @return the result of this page
	 * @throws IOException
	 */
	public Map<String, PageContents> crawl(String urlString, String parent, Map<String, PageContents> pageContentsMap) {
		Assert.hasLength(urlString, "url cannot be blank");
		logger.debug("processing:{}",urlString);
		try {
			Document doc = Jsoup.connect(urlString).get();
			//process the url
			PageContents details = getPageContents(doc, parent);
			pageContentsMap.put(getLinkWithoutParameters(urlString), details);

			//process all its children..
			details.getInternalLinks().stream()
				.filter(s-> !pageContentsMap.containsKey(getLinkWithoutParameters(s))) //only if not processed already
//				.parallel() //TODO this exception while using parallel stream: javax.net.ssl.SSLException: No PSK available. Unable to resume.
				.forEach(link->crawl(link, urlString,pageContentsMap));
		} catch (IOException e) {
			// in case JSoup fails to connect to any url, log and move on
			logger.error(e);
		}
		return pageContentsMap;
	}

	/**
	 * @param link any given link
	 * @return the link without the extra hashbangs and querystrings to avoid repeatations  
	 */
	public String getLinkWithoutParameters(String link)  {
		try {
			URL url = new URL(link);
			return url.getProtocol()+"://"+url.getHost()+url.getPath();
		} catch (MalformedURLException e) {
			logger.error("not a proper url:"+link);
			return link;
		}
	}

	/**
	 * @param doc the document to analyze
	 * @param parent of the document
	 * @return the summary as {@link PageContents}
	 */
	public PageContents getPageContents(Document doc, String parent) {
		Set<String> internalLinks = new ConcurrentSkipListSet<>();
		Set<String> externalLinks = new ConcurrentSkipListSet<>();
		Set<String> staticResources = new ConcurrentSkipListSet<>();
		String location = null;
		if(doc!=null) {
			try {
				location = doc.location();
				String baseUriOfDoc = getBaseUriFromUri(doc.baseUri());

				internalLinks = doc.select("a[href]").parallelStream().filter(element->element.absUrl("href").startsWith(baseUriOfDoc)).map(e->e.absUrl("href")).collect(Collectors.toSet());
				externalLinks = doc.select("a[href]").parallelStream().filter(element->!element.absUrl("href").startsWith(baseUriOfDoc)).map(e->e.absUrl("href")).collect(Collectors.toSet());
				staticResources = doc.select("img").parallelStream().filter(element->!element.absUrl("src").startsWith(baseUriOfDoc)).map(e->e.absUrl("src")).collect(Collectors.toSet());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return new PageContents(location,parent, internalLinks, externalLinks, staticResources, LocalDateTime.now());
	}

	public String getBaseUriFromUri(String urlString) throws MalformedURLException {
		URL url = new URL(urlString);
		return url.getProtocol()+"://"+url.getHost();
	}
}



