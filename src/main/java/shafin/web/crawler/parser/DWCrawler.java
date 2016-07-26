package shafin.web.crawler.parser;

import java.io.IOException;
import java.util.List;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;

public class DWCrawler {

	public String FOLDER_PATH = "D:/home/dw/";
	public String URL_LIST_FILE_PATH = FOLDER_PATH + "";
	public String STORAGE_FOLDER_PATH = FOLDER_PATH + "/data/";

	public List<String> urlList;

	public DWCrawler(String linksPath, String storagePath) {
		this.URL_LIST_FILE_PATH = linksPath;
		this.STORAGE_FOLDER_PATH = storagePath;
		this.urlList = FileHandler.readFile(URL_LIST_FILE_PATH);
	}

	public void crawlAndStoreAsTxt() {
		int counter = 0;
		for (String url : this.urlList) {
			try {
				Document doc = parseDocument(url);
				System.out.println(doc.toString());
				if (!doc.getManualKeyphrases().isEmpty()) {
					FileHandler.writeFile(STORAGE_FOLDER_PATH + counter++ + ". " + doc.getTitle() + ".txt",
							doc.toString());
					System.out.println(counter + ": WRITTEN: " + url);
				} else {
					System.out.println(counter + ": SKIPPED: " + url);
				}
			} catch (NullPointerException e) {

			}
		}
	}

	public void crawlAndStoreAsJson() {
		int counter = 0;
		for (String url : this.urlList) {
			try {
				Document doc = parseDocument(url);
				if (!doc.getManualKeyphrases().isEmpty()) {
					JsonProcessor jsonProcessor = new JsonProcessor();
					String jsonString = jsonProcessor.convertToJson(doc);
					FileHandler.writeFile(STORAGE_FOLDER_PATH + counter++ + ". " + doc.getTitle() + ".json",
							jsonString);
					System.out.println(counter + ": WRITTEN: " + url);
				} else {
					System.out.println(counter + ": SKIPPED: " + url);
				}
			} catch (NullPointerException | IOException e) {

			}
		}
	}


	public Document parseDocument(String url) {
		try {
			DWParser parser = new DWParser(url);
			return parser.getParsedDocument();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
