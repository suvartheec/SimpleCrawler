package com.practice.simplecrawler.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class SimpleCrawlerUtil {

	public List<PageDetailsDto> crawl(String urlString) throws IOException {
		return crawl(urlString, new ArrayList<>(),new ArrayList<>());
	}

	private List<PageDetailsDto> crawl(String urlString, List<String> alreadyVisited, List<PageDetailsDto> pageDetails) throws IOException {
		Assert.hasLength(urlString, "url cannot be blank");
		Document doc = Jsoup.connect(urlString).get();
		PageDetailsDto details = getPageDetails(doc);
		pageDetails.add(details);
		for (String internalLink : details.getInternalLinks()) {
			if(!alreadyVisited.contains(internalLink)) {
				alreadyVisited.add(internalLink);
				crawl(internalLink, alreadyVisited, pageDetails);
			}
		}

		return pageDetails;
	}

	private PageDetailsDto getPageDetails(Document doc) {
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
		return new PageDetailsDto(doc.location(), internalLinks, externalLinks, staticResources, LocalDateTime.now());
	}

	private String getBaseUriFromUri(String urlString) throws MalformedURLException {
		URL url = new URL(urlString);
		return url.getProtocol()+"://"+url.getHost();
	}
}



