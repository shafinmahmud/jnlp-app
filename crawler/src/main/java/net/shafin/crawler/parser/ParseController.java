package net.shafin.crawler.parser;

import net.shafin.common.model.Document;
import net.shafin.common.util.FileUtil;
import net.shafin.common.util.JsonProcessor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
public class ParseController {

    public String URL_LIST_FILE_PATH;
    public String STORAGE_FOLDER_PATH;

    public List<String> urlList;
    public List<String> exploredURLs;
    public List<String> skippedURLs;
    public List<String> errorURLs;

    public ParseController(String linksPath, String storagePath) {
        this.URL_LIST_FILE_PATH = linksPath;
        this.STORAGE_FOLDER_PATH = storagePath;
        this.urlList = FileUtil.readFile(URL_LIST_FILE_PATH);
        this.exploredURLs = FileUtil.readFileOrCreateIfNotExists(STORAGE_FOLDER_PATH + "explored.db");
        this.skippedURLs = FileUtil.readFileOrCreateIfNotExists(STORAGE_FOLDER_PATH + "skipped.db");

        FileUtil.deleteFile(STORAGE_FOLDER_PATH + "error.db");
        this.errorURLs = FileUtil.readFileOrCreateIfNotExists(STORAGE_FOLDER_PATH + "error.db");
    }

    public void parseAndStoreAsTxt() {
        int counter = 0;
        for (String url : this.urlList) {
            if (!exploredURLs.contains(url) && !skippedURLs.contains(url)) {
                try {
                    Document doc = parseDocument(url);

                    if (doc != null) {
                        FileUtil.writeFile(STORAGE_FOLDER_PATH + counter + ". " + doc.getTitle() + ".txt",
                                doc.toString());
                        FileUtil.appendFile(STORAGE_FOLDER_PATH + "explored.db", url + "\n");
                        System.out.println(counter + ": WRITTEN: " + url);
                    } else {
                        FileUtil.appendFile(STORAGE_FOLDER_PATH + "skipped.db", url + "\n");
                        System.out.println(counter + ": SKIPPED: " + url);
                    }
                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                    System.out.println(counter + ": ERROR: " + url);
                    FileUtil.appendFile(STORAGE_FOLDER_PATH + "error.db", url + "\n");
                }
            }
            counter++;
        }
    }

    public void parseAndStoreAsJson() {
        int counter = 0;
        for (String url : this.urlList) {
            if (!exploredURLs.contains(url) && !skippedURLs.contains(url)) {
                try {
                    Document doc = parseDocument(url);
                    if (doc != null) {
                        JsonProcessor jsonProcessor = new JsonProcessor();
                        String jsonString = jsonProcessor.convertToJson(doc);
                        FileUtil.writeFile(STORAGE_FOLDER_PATH + counter++ + ". " + doc.getTitle() + ".json",
                                jsonString);
                        FileUtil.appendFile(STORAGE_FOLDER_PATH + "explored.db", url + "\n");
                        System.out.println(counter + ": WRITTEN: " + url);
                    } else {
                        FileUtil.appendFile(STORAGE_FOLDER_PATH + "skipped.db", url + "\n");
                        System.out.println(counter + ": SKIPPED: " + url);
                    }
                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                    System.out.println(counter + ": ERROR: " + url);
                    FileUtil.appendFile(STORAGE_FOLDER_PATH + "error.db", url + "\n");
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
