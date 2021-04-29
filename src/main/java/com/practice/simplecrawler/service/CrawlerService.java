package com.practice.simplecrawler.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.practice.simplecrawler.utils.SimpleCrawlerUtil;

@RestController
@RequestMapping("/rest/crawler")
public class CrawlerService {
	
	@Autowired
	SimpleCrawlerUtil util;

	@ResponseBody
	@RequestMapping("/getAll")
	public List<String> getAllSites(){
		return null;
	}
	
	@RequestMapping("/crawl")
	public ResponseEntity<?> crawl(@RequestParam(name="url", defaultValue = "https://suvartheec.github.io") String urlString){
		if(!StringUtils.hasLength(urlString))
			return ResponseEntity.badRequest().body("url missing");
		try {
			URL url = new URL(urlString);
			
			return ResponseEntity.ok(util.crawl(urlString));
			
//			return ResponseEntity.ok("parsing of "+urlString+" complete");
		} catch (MalformedURLException e) {
			return ResponseEntity.badRequest().body("invalid url:"+urlString);
		} catch (IOException e) {
			return ResponseEntity.badRequest().body("Unable to process:"+urlString);
		}
	}
}
