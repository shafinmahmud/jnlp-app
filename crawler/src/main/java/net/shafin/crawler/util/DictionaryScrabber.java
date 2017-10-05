package net.shafin.crawler.util;

import net.shafin.common.util.FileHandler;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class DictionaryScrabber {

    public static void main(String[] args) throws MalformedURLException, IOException {

        String file_path = "D:/corncob_lowercase.txt";
        String en_path = "D:/corncob_EN.txt";
        String bn_path = "D:/corncob_BN.txt";

        List<String> lines = FileHandler.readFile(file_path);
        String URL = "http://www.english-bangla.com/dictionary/";

        JsoupParser jsoupParser = new JsoupParser();

        int index = 0;

        for (String line : lines) {
            System.out.print(index++ + ": " + line + " : ");
            try {
                String html = jsoupParser.getHtmlFromGetRequest(URL + line);
                Element meaning = jsoupParser.parseElementByFirstFromHtml(html, "#w_info > span.format1");

                FileHandler.appendFile(en_path, line + "\n");
                FileHandler.appendFile(bn_path, meaning.text() + "\n");

                System.out.println(meaning.text());
            } catch (NullPointerException e) {
                System.out.println(" ?");
            }
        }
    }
}
