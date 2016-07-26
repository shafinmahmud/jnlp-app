package shafin.web.crawler.parser;

public class Controller {
	
	public static void main(String[] args) {
		String linksPath = "D:/home/dw/dw-refined.txt";
		String storagePath = "D:/home/dw/data/";
		DWCrawler crawler = new DWCrawler(linksPath, storagePath);
		crawler.crawlAndStoreAsTxt();
	}
}
