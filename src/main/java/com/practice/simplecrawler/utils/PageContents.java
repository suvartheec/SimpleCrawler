package com.practice.simplecrawler.utils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author SUVARTHEE
 * Contains the information from a single page
 */
public class PageContents {

	private String url;
	private String parent;
	private List<String> internalLinks;
	private List<String> externalStrings;
	private List<String> staticResources;
	private LocalDateTime parsedOn;
	
	
	public PageContents() {
		super();
	}

	public PageContents(String url, List<String> internalLinks, List<String> externalStrings,
			List<String> staticResources, LocalDateTime parsedOn) {
		super();
		this.url = url;
		this.internalLinks = internalLinks;
		this.externalStrings = externalStrings;
		this.staticResources = staticResources;
		this.parsedOn = parsedOn;
	}
	
	public PageContents(String url, String parent, List<String> internalLinks, List<String> externalStrings,
			List<String> staticResources, LocalDateTime parsedOn) {
		super();
		this.url = url;
		this.parent = parent;
		this.internalLinks = internalLinks;
		this.externalStrings = externalStrings;
		this.staticResources = staticResources;
		this.parsedOn = parsedOn;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getInternalLinks() {
		return internalLinks;
	}
	public void setInternalLinks(List<String> internalLinks) {
		this.internalLinks = internalLinks;
	}
	public List<String> getExternalStrings() {
		return externalStrings;
	}
	public void setExternalStrings(List<String> externalStrings) {
		this.externalStrings = externalStrings;
	}
	public List<String> getStaticResources() {
		return staticResources;
	}
	public void setStaticResources(List<String> staticResources) {
		this.staticResources = staticResources;
	}
	public LocalDateTime getParsedOn() {
		return parsedOn;
	}
	public void setParsedOn(LocalDateTime parsedOn) {
		this.parsedOn = parsedOn;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	
	
	
}
