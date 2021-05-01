package com.practice.simplecrawler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.practice.simplecrawler.utils.PageContents;
import com.practice.simplecrawler.utils.SimpleCrawlerUtil;

@SpringBootTest
class SimplecrawlerApplicationTests {

	@Autowired
	SimpleCrawlerUtil utils;

	@Test
	void contextLoads() {
	}

	@Test 
	void getBaseUriFromUri_testWithProperUrl() throws MalformedURLException{
		String toTest = "http://www.test.com";
		String expecting = "http://www.test.com";
		assertThat(utils.getBaseUriFromUri(toTest)).isEqualTo(expecting);

	}
	@Test 
	void getBaseUriFromUri_testWithImproperUrl() throws MalformedURLException{
		String toTest = "not_a+proper_url";
		assertThatExceptionOfType(MalformedURLException.class).isThrownBy(()->utils.getBaseUriFromUri(toTest));
	}


	@Test 
	void getBaseUriFromUri_testWithProperUrlAndPath() throws MalformedURLException{
		String toTest = "http://www.test.com/some/path";
		String expecting = "http://www.test.com";
		assertThat(utils.getBaseUriFromUri(toTest)).isEqualTo(expecting);
	}

	@Test 
	void getBaseUriFromUri_testWithProperUrlAndHashbang() throws MalformedURLException{
		String toTest = "http://www.test.com#test";
		String expecting = "http://www.test.com";
		assertThat(utils.getBaseUriFromUri(toTest)).isEqualTo(expecting);
	}

	@Test 
	void getBaseUriFromUri_testWithProperUrlAndQuery() throws MalformedURLException{
		String toTest = "http://www.test.com?test=123";
		String expecting = "http://www.test.com";
		assertThat(utils.getBaseUriFromUri(toTest)).isEqualTo(expecting);
	}
	
	
	@Test
	void getLinkWithoutParameters_testWithProperUrl() throws MalformedURLException {
		String toTest = "http://www.test.com/some/path";
		String expecting = "http://www.test.com/some/path";
		assertThat(utils.getLinkWithoutParameters(toTest)).isEqualTo(expecting);
		
	}

	@Test
	void getLinkWithoutParameters_testWithProperUrlAndQueryString() throws MalformedURLException {
		String toTest = "http://www.test.com/some/path?p=param1";
		String expecting = "http://www.test.com/some/path";
		assertThat(utils.getLinkWithoutParameters(toTest)).isEqualTo(expecting);
		
	}
	
	@Test
	void getLinkWithoutParameters_testWithProperUrlAndHashbang() throws MalformedURLException {
		String toTest = "http://www.test.com/some/path#testParam";
		String expecting = "http://www.test.com/some/path";
		assertThat(utils.getLinkWithoutParameters(toTest)).isEqualTo(expecting);
		
	}
	
	
	@Test
	void getPageContents_testWithEmptyDoc() {
		String self = "http://www.test-uri.com";
		String parent = "http://www.test-parent.com";
		Document d = new Document(self);
		PageContents contents = utils.getPageContents(d, parent);
		assertThat(contents.getParent()).isEqualTo(parent);
		assertThat(contents.getStaticResources().size()).isEqualTo(0);
		assertThat(contents.getExternalStrings().size()).isEqualTo(0);
		assertThat(contents.getInternalLinks().size()).isEqualTo(0);
		assertThat(contents.getUrl()).isEqualTo(self);
	}
	
	@Test
	void getPageContents_testWithDocWithLinks() {
		String self = "http://www.test-uri.com/some_link";
		String parent = "http://www.test-uri.com";
		Document d = new Document(self);
		d.appendElement("a").attr("href", "/some_other_link_1");
		d.appendElement("a").attr("href", "/some_other_link_2");
		d.appendElement("a").attr("href", "http://www.external-uri.com");
		PageContents contents = utils.getPageContents(d, parent);
		assertThat(contents.getParent()).isEqualTo(parent);
		assertThat(contents.getStaticResources().size()).isEqualTo(0);
		assertThat(contents.getExternalStrings().size()).isEqualTo(1);
		assertThat(contents.getInternalLinks().size()).isEqualTo(2);
		assertThat(contents.getUrl()).isEqualTo(self);
	}
	
	@Test
	void crawl_testPredefinedLink() throws IOException {
		//This site has been created to test the crawl functionality. 
		String linkToTest = "http://suvartheec.github.io/";
		Map<String, PageContents> crawlResult = utils.crawl(linkToTest);

		//Contains 4 pages (including the index) and the root url hence should return 5
		assertThat(crawlResult.size()).isEqualTo(5);
		assertThat(crawlResult.containsKey("https://suvartheec.github.io"));
		assertThat(crawlResult.containsKey("https://suvartheec.github.io/test1.html"));
		assertThat(crawlResult.containsKey("https://suvartheec.github.io/test2.html"));
		assertThat(crawlResult.containsKey("https://suvartheec.github.io/test3.html"));
		assertThat(crawlResult.containsKey("https://suvartheec.github.io/index.html"));
	}
	
	@Test
	void crawl_testNonExistingLink() throws IOException {
		String linkToTest = "http://suvartheec-non-existing.does-not-exist.io/";
		assertThat(utils.crawl(linkToTest)).isNotNull();
	}
	
}
