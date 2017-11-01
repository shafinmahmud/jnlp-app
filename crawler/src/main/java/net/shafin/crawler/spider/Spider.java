package net.shafin.crawler.spider;

import net.shafin.common.util.FileUtil;
import net.shafin.crawler.model.DomainSetup;
import net.shafin.crawler.model.EnvSetup;
import net.shafin.crawler.model.UrlQueue;
import org.jsoup.HttpStatusException;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
public class Spider {

    public String HOTLINK_PATH;
    public String HISTORY_PATH;
    public String STORAGE_PATH;
    public String FAILED_LINK_PATH;

    public Queue<String> urlQueue;
    public LinkExtractor extractor;
    public UrlQueue db;

    private int counter;

    public Spider(DomainSetup config, EnvSetup env) {
        this.extractor = new LinkExtractor(config);
        this.db = new UrlQueue();
        this.urlQueue = new LinkedList<>();

        this.HOTLINK_PATH = env.getHOTLINK_PATH();
        this.HISTORY_PATH = env.getHISTORY_PATH();
        this.STORAGE_PATH = env.getSTORAGE_PATH();
        this.FAILED_LINK_PATH = env.getFAILED_LINK_PATH();
    }

    private void loadStoredLinksInQueue() {
        File file = new File(STORAGE_PATH);
        List<String> hotLinks = FileUtil.readFile(HOTLINK_PATH);
        String queueHead = FileUtil.readFileAsSingleString(HISTORY_PATH);
        boolean headFound = false;

        if (file.exists()) {
            for (String hot : hotLinks) {
                this.urlQueue.add(hot);
            }

            List<String> list = FileUtil.readFile(STORAGE_PATH);
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
                            FileUtil.appendFile(STORAGE_PATH, url + "\n");
                        } else {
                            System.out.println("EXISTING: " + counter + " Q["
                                    + this.urlQueue.size() + "] " + url);
                        }
                    }
                } catch (HttpStatusException e) {
                    System.out.println("EXCEPTION : " + e.getMessage());
                    FileUtil.appendFile(FAILED_LINK_PATH, seed + "\n");
                }

                seed = urlQueue.poll();
                FileUtil.writeFile(HISTORY_PATH, seed);
            } while (!urlQueue.isEmpty());
        } catch (UnknownHostException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
