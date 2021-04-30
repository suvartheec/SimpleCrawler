package com.practice.simplecrawler.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SimpleCrawlerUtil {
	private Logger logger = LogManager.getLogger();

	public Map<String, PageContents> crawl(String urlString) throws IOException {
		return crawl(urlString, null, Collections.synchronizedMap(new HashMap<>()));
	}

	private Map<String, PageContents> crawl(String urlString, String parent, Map<String, PageContents> pageContentsMap) throws IOException {
		Assert.hasLength(urlString, "url cannot be blank");
		logger.debug("processing:{}",urlString);
		
		Document doc = Jsoup.connect(urlString).get();
		
		//process the url
		PageContents details = getPageContents(doc, parent);
		pageContentsMap.put(getLinkWithoutParameters(urlString), details);
		
		//process all its children..
		for (String internalLink : details.getInternalLinks()) {
			//...but only if not done already
			if(!pageContentsMap.containsKey(getLinkWithoutParameters(internalLink))) {
				crawl(internalLink, urlString, pageContentsMap);
			}
		}
		return pageContentsMap;
	}

	private String getLinkWithoutParameters(String internalLink) throws MalformedURLException {
		URL url = new URL(internalLink);
		return url.getProtocol()+"://"+url.getHost()+url.getPath();
	}

	private PageContents getPageContents(Document doc, String parent) {
		List<String> internalLinks = new ArrayList<>();
		List<String> externalLinks = new ArrayList<>();
		List<String> staticResources = new ArrayList<>();
		
		try {
			String baseUriOfDoc = getBaseUriFromUri(doc.baseUri());

			for (Element element : doc.select("a[href]")) {
				if(element.absUrl("href").startsWith(baseUriOfDoc))
					internalLinks.add(element.absUrl("href"));
				else
					externalLinks.add(element.absUrl("href"));
			}
			for(Element element : doc.select("img")) {
				staticResources.add(element.absUrl("src"));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new PageContents(doc.location(),parent, internalLinks, externalLinks, staticResources, LocalDateTime.now());
	}

	private String getBaseUriFromUri(String urlString) throws MalformedURLException {
		URL url = new URL(urlString);
		return url.getProtocol()+"://"+url.getHost();
	}
}



