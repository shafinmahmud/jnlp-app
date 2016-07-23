package shafin.web.crawler.parser;

import java.util.List;

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
}
