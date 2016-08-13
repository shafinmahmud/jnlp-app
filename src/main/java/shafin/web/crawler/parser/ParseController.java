package shafin.web.crawler.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;

public class ParseController {

	public String URL_LIST_FILE_PATH;
	public String STORAGE_FOLDER_PATH;

	public List<String> urlList;
	public List<String> exploredURLs;
	public List<String> errorURLs;

	public ParseController(String linksPath, String storagePath) {
		this.URL_LIST_FILE_PATH = linksPath;
		this.STORAGE_FOLDER_PATH = storagePath;
		this.urlList = FileHandler.readFile(URL_LIST_FILE_PATH);
		this.exploredURLs = FileHandler.readFileOrCreateIfNotExists(STORAGE_FOLDER_PATH + "explored.db");

		FileHandler.deleteFile(STORAGE_FOLDER_PATH + "skipped.db");
		this.errorURLs = FileHandler.readFileOrCreateIfNotExists(STORAGE_FOLDER_PATH + "error.db");
	}

	public void parseAndStoreAsTxt() {
		int counter = 0;
		for (String url : this.urlList) {
			if (!exploredURLs.contains(url)) {
				try {
					Document doc = parseDocument(url);

					if (doc != null) {
						FileHandler.writeFile(STORAGE_FOLDER_PATH + counter + ". " + doc.getTitle() + ".txt",
								doc.toString());
						FileHandler.appendFile(STORAGE_FOLDER_PATH + "explored.db", url + "\n");
						System.out.println(counter + ": WRITTEN: " + url);
					} else {
						System.out.println(counter + ": SKIPPED: " + url);
					}
				} catch (NullPointerException | IOException e) {
					e.printStackTrace();
					FileHandler.appendFile(STORAGE_FOLDER_PATH + "error.db", url + "\n");
				}
			}
			counter++;
		}
	}

	public void parseAndStoreAsJson() {
		int counter = 0;
		for (String url : this.urlList) {
			if (!exploredURLs.contains(url)) {
				try {
					Document doc = parseDocument(url);
					if (doc != null) {
						JsonProcessor jsonProcessor = new JsonProcessor();
						String jsonString = jsonProcessor.convertToJson(doc);
						FileHandler.writeFile(STORAGE_FOLDER_PATH + counter++ + ". " + doc.getTitle() + ".json",
								jsonString);
						FileHandler.appendFile(STORAGE_FOLDER_PATH + "explored.db", url + "\n");
						System.out.println(counter + ": WRITTEN: " + url);
					} else {
						System.out.println(counter + ": SKIPPED: " + url);
					}
				} catch (NullPointerException | IOException e) {
					e.printStackTrace();
					FileHandler.appendFile(STORAGE_FOLDER_PATH + "error.db", url + "\n");
				}
			}
			counter++;
		}
	}

	public Document parseDocument(String url) throws MalformedURLException, IOException {
		DocumentParser parser = new DWParser(url);
		if (!parser.parseManualKeyphrases().isEmpty()) {
			return parser.getParsedDocument();
		}
		return null;
	}
}
