package shafin.web.crawler.home;

import shafin.web.crawler.parser.ParseController;

public class Controller {
	
	public static void main(String[] args) {
		String linksPath = "D:/home/dw/dw.txt";
		String storagePath = "D:/home/dw/data/";
		ParseController parseController = new ParseController(linksPath, storagePath);
		parseController.parseAndStoreAsTxt();
	}
}
