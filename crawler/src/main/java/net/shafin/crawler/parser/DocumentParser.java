package net.shafin.crawler.parser;

import net.shafin.common.model.Document;

import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 7/26/2016
 */
public interface DocumentParser {

    public String parseSource();

    public String parseLang();

    public String parseURL();

    public String parseTitle();

    public String parseDate();

    public String parseWritter();

    public List<String> parseCategories();

    public List<String> parseManualKeyphrases();

    public String parseArticle();

    public Document getParsedDocument();
}
