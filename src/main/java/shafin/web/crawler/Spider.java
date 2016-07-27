package shafin.web.crawler;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.HttpStatusException;

import shafin.nlp.util.FileHandler;

public class Spider {

	public String FOLDER_PATH = "D:/home/dw/";
	public String HOTLINK_PATH = FOLDER_PATH + "dw.hot";
	public String HISTORY_PATH = FOLDER_PATH + "dwtest.q";
	public String STORAGE_PATH = FOLDER_PATH + "dwtest.txt";
	
	public Queue<String> urlQueue;
	public LinkExtractor extractor;
	public UrlDB db;

	private int counter;

	public Spider(Config config) {
		this.extractor = new LinkExtractor(config);
		this.db = new UrlDB();
		this.urlQueue = new LinkedList<>();
	}

	private void loadStoredLinksInQueue() {
		File file = new File(STORAGE_PATH);	
		List<String> hotLinks = FileHandler.readFile(HOTLINK_PATH);
		String queueHead = FileHandler.readFileAsSingleString(HISTORY_PATH);
		boolean headFound = false;

		if (file.exists()) {
			for(String hot : hotLinks){
				this.urlQueue.add(hot);
			}
			
			List<String> list = FileHandler.readFile(STORAGE_PATH);
			for (String l : list) {
				this.db.insert(l);

				if (l.equals(queueHead)) {
					headFound = true;
				}
				if (headFound) {
					this.urlQueue.add(l);
				}

				System.out.println("LOADING: " + ++counter + " Q[" + this.urlQueue.size() + "] " + l);
			}
		}
	}

	public void process(String seed) throws IOException {

		loadStoredLinksInQueue();
		try {
			do {
				try {
					this.extractor.setURL(seed);
					List<String> urlList = this.extractor.extractURL();

					for (String url : urlList) {
						if (!db.isExists(url)) {
							urlQueue.add(url);
							db.insert(url);
							System.out.println("INSERTING: " + ++counter + " Q[" + this.urlQueue.size() + "] " + url);
							FileHandler.appendFile(STORAGE_PATH, url + "\n");
						} else {
							//System.out.println("EXISTING: " + counter + " Q[" + this.urlQueue.size() + "] " + url);
						}
					}
				} catch (HttpStatusException e) {
					e.printStackTrace();
				}
				
				seed = urlQueue.poll();
				FileHandler.writeFile(HISTORY_PATH, seed);
			} while (!urlQueue.isEmpty());
		} catch (UnknownHostException | NullPointerException  e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		String DOMAIN = "http://www.dw.com";
		String FILTER = "\\/bn\\/\\.*";
		List<String> excludeStrings = new ArrayList<>();
		excludeStrings.add("m.dw.com");
		excludeStrings.add("/search/");
		excludeStrings.add("/মিডিয়া-সেন্টার/মাল্টিমিডিয়া/");

		Config config = new Config(DOMAIN, FILTER, excludeStrings);

		Spider spider = new Spider(config);
		spider.process("http://www.dw.com/bn/");
	}
}
