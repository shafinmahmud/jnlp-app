package net.shafin.crawler.spider;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
public class UrlDB {

    private SortedSet<String> uniqueLinks;

    public UrlDB() {
        uniqueLinks = new TreeSet<>();
    }

    public boolean isExists(String url) {
        return uniqueLinks.contains(url);
    }

    public void insert(String url) {
        uniqueLinks.add(url);
    }

    public void delete(String url) {
        uniqueLinks.remove(url);
    }
}
