package com.practice.simplecrawler.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practice.simplecrawler.utils.PageContents;
import com.practice.simplecrawler.utils.SimpleCrawlerUtil;

@RestController
@RequestMapping("/rest/crawler")
public class CrawlerService {
	
	Logger logger = LogManager.getLogger();
	
	@Autowired
	SimpleCrawlerUtil util;

	
	@RequestMapping("/crawl")
	public ResponseEntity<?> crawl(@RequestParam(name="url", defaultValue = "https://suvartheec.github.io") String urlString){
		if(!StringUtils.hasLength(urlString))
			return ResponseEntity.badRequest().body("url missing");
		try {
			//checking if proper url
			new URL(urlString);
			
			//initializing stopwatch to check performance
			StopWatch s = new StopWatch();
			
			//perform the crawl
			s.start();
			Map<String, PageContents> result = util.crawl(urlString);
			s.stop();
			
			//log and return result
			logger.debug("Time taken in millies:"+s.getTotalTimeMillis());
			return ResponseEntity.ok(result);
		} catch (MalformedURLException e) {
			return ResponseEntity.badRequest().body("invalid url:"+urlString);
		} catch (IOException e) {
			logger.error(e);
			return ResponseEntity.badRequest().body("Unable to process:"+urlString);
		}
	}
}
