package net.shafin.crawler.boot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.shafin.crawler.model.DomainSetup;
import net.shafin.crawler.model.EnvSetup;
import net.shafin.crawler.spider.Spider;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
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
        String acceptingPattern = "\\/bn\\/\\.*";
		
		/*
		 * ExcludeStrings is the List of Strings that will be used to search for
		 * in every URL to check whether it contains any of them. If it contains
		 * then then URL will be excluded. 
		 */
        DomainSetup domain = DomainSetup.init(DOMAIN)
                .accepting(acceptingPattern)
                .exclude("m.dw.com",
                        "/search/",
                        "/মিডিয়া-সেন্টার/",
                        "/overlay/media/",
                        "/bn/gelöscht/");
		
		/*
		 * This is the folder path for Data storage and state storing
		 */
        String outputFolder = "D:/home/dw/";
        EnvSetup config = new EnvSetup(outputFolder);

        Spider spider = new Spider(domain, config);
        spider.process("http://www.dw.com/bn/");
    }
}
