package net.shafin.crawler.home;

import net.shafin.crawler.parser.ParseController;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
public class Controller {

    public static void main(String[] args) {
        String linksPath = "D:/home/dw/dw.txt";
        String storagePath = "D:/home/dw/data/";
        ParseController parseController = new ParseController(linksPath, storagePath);
        parseController.parseAndStoreAsTxt();
    }
}
