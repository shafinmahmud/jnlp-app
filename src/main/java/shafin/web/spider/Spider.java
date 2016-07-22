package shafin.web.spider;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import shafin.nlp.util.FileHandler;

public class Spider {

	public Queue<String> urlQueue;
	public LinkExtractor extractor;
	public UrlDB db;

	public Spider(Config config) {
		this.extractor = new LinkExtractor(config);
		this.db = new UrlDB();
		this.urlQueue = new LinkedList<>();
	}

	public void process(String seed) {
		do {
			try {
				this.extractor.setURL(seed);
				List<String> urlList = this.extractor.extractURL();

				for (String url : urlList) {
					if (!db.isExists(url)) {
						urlQueue.add(url);
						db.insert(url);
						System.out.println(url);
						FileHandler.appendFile("D:/dw.txt", url+"\n");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			seed = urlQueue.poll();
		} while (!urlQueue.isEmpty());
	}
	
	public static void main(String[] args) {
		Config config = new Config("\\/bn\\/\\.*");
		Spider spider = new Spider(config);
		spider.process("http://www.dw.com/bn/");
	}
}
