package shafin.web.spider;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
			this.extractor.setURL(seed);
			List<String> urlList = this.extractor.extractURL();

			for (String url : urlList) {
				if (!db.isExists(url)) {
					urlQueue.add(url);
					db.insert(url);
					System.out.println(url);
				}
			}
			seed = urlQueue.poll();
		} while (!urlQueue.isEmpty());
	}
	
	public static void main(String[] args) {
		Config config = new Config("\\.*");
		Spider spider = new Spider(config);
		spider.process("http://www.dw.com/bn/");
	}
}
