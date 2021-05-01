package com.practice.simplecrawler.utils;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author SUVARTHEE
 * Contains the information from a single page
 */
public class PageContents {

	private String url;
	private String parent;
	private Collection<String> internalLinks;
	private Collection<String> externalStrings;
	private Collection<String> staticResources;
	private LocalDateTime parsedOn;
	
	
	public PageContents() {
		super();
	}

	public PageContents(String url, Collection<String> internalLinks, Collection<String> externalStrings,
			Collection<String> staticResources, LocalDateTime parsedOn) {
		super();
		this.url = url;
		this.internalLinks = internalLinks;
		this.externalStrings = externalStrings;
		this.staticResources = staticResources;
		this.parsedOn = parsedOn;
	}
	
	public PageContents(String url, String parent, Collection<String> internalLinks, Collection<String> externalStrings,
			Collection<String> staticResources, LocalDateTime parsedOn) {
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
	public Collection<String> getInternalLinks() {
		return internalLinks;
	}
	public void setInternalLinks(Collection<String> internalLinks) {
		this.internalLinks = internalLinks;
	}
	public Collection<String> getExternalStrings() {
		return externalStrings;
	}
	public void setExternalStrings(Collection<String> externalStrings) {
		this.externalStrings = externalStrings;
	}
	public Collection<String> getStaticResources() {
		return staticResources;
	}
	public void setStaticResources(Collection<String> staticResources) {
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
