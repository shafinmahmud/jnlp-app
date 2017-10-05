package shafin.web.crawler.spider;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.jsoup.HttpStatusException;

import shafin.nlp.util.FileHandler;

public class Spider {

	public String HOTLINK_PATH;
	public String HISTORY_PATH;
	public String STORAGE_PATH;
	public String FAILED_LINK_PATH;

	public Queue<String> urlQueue;
	public LinkExtractor extractor;
	public UrlDB db;

	private int counter;

	public Spider(SpiderConfig config) {
		this.extractor = new LinkExtractor(config);
		this.db = new UrlDB();
		this.urlQueue = new LinkedList<>();
		
		this.HOTLINK_PATH = config.getHOTLINK_PATH();
		this.HISTORY_PATH = config.getHISTORY_PATH();
		this.STORAGE_PATH = config.getSTORAGE_PATH();
		this.FAILED_LINK_PATH = config.getFAILED_LINK_PATH();		
	}

	private void loadStoredLinksInQueue() {
		File file = new File(STORAGE_PATH);
		List<String> hotLinks = FileHandler.readFile(HOTLINK_PATH);
		String queueHead = FileHandler.readFileAsSingleString(HISTORY_PATH);
		boolean headFound = false;

		if (file.exists()) {
			for (String hot : hotLinks) {
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
							 System.out.println("EXISTING: " + counter + " Q["
							 + this.urlQueue.size() + "] " + url);
						}
					}
				} catch (HttpStatusException e) {
					System.out.println("EXCEPTION : "+ e.getMessage());
					FileHandler.appendFile(FAILED_LINK_PATH, seed + "\n");
				}

				seed = urlQueue.poll();
				FileHandler.writeFile(HISTORY_PATH, seed);
			} while (!urlQueue.isEmpty());
		} catch (UnknownHostException | NullPointerException e) {
			e.printStackTrace();
		}
	}

}
