package shafin.web.crawler.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shafin.web.crawler.spider.Spider;
import shafin.web.crawler.spider.SpiderConfig;

public class DWSpider {

	public static void main(String[] args) throws IOException {

		/*
		 * DOMAIN will be used for generate URL by concatanating with dynamic
		 * links like '/bn/a-4554790'. You need to observe properly the site
		 * HTML before define this and resrtrict the Spider in the domain.
		 */
		String DOMAIN = "http://www.dw.com";

		/*
		 * FILTER is the Regex pattern to match the URL you will accept for
		 * storing and crawling
		 */
		String FILTER = "\\/bn\\/\\.*";
		
		/*
		 * ExcludeStrings is the List of Strings that will be used to search for
		 * in every URL to check whether it contains any of them. If it contains
		 * then then URL will be excluded. 
		 */
		List<String> excludeStrings = new ArrayList<>();
		excludeStrings.add("m.dw.com");
		excludeStrings.add("/search/");
		excludeStrings.add("/মিডিয়া-সেন্টার/");
		excludeStrings.add("/overlay/media/");
		excludeStrings.add("/bn/gelöscht/");
		
		/*
		 * This is the folder path for Data storage and state storing
		 */
		String outputFolder = "D:/home/dw/";
		SpiderConfig config = new SpiderConfig(DOMAIN, FILTER, excludeStrings, outputFolder);

		Spider spider = new Spider(config);
		spider.process("http://www.dw.com/bn/");
	}
}
