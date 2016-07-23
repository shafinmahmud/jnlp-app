package shafin.web.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;

import shafin.nlp.util.JsoupParser;

public class LinkExtractor {

	private final String DOMAIN;
	private final String DOMAIN_FILTER;
	private final List<String> EXCLUDE_STRINGS;
	private String URL;

	public LinkExtractor(Config config) {
		this.DOMAIN_FILTER = config.getDOMAIN_FILTER_PATTERN();
		this.DOMAIN = config.getROOT_DOMAIN();
		this.EXCLUDE_STRINGS = config.getEXCLUDE_STRINGS();
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	private Response getHTMLFromURL() {
		try {
			JsoupParser jsoupParser = new JsoupParser();
			return jsoupParser.getRedirectedResponseFromGetRequest(this.URL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean excludeFilter(String url) {
		for (String e : this.EXCLUDE_STRINGS) {
			if (url.contains(e))
				return false;
		}
		return true;
	}

	public List<String> extractURL() throws IOException {
		Response response = getHTMLFromURL();
		List<String> urlList = new ArrayList<>();
		if (response != null) {
			List<Element> links = response.parse().select("a");

			for (Element e : links) {
				String link = e.attr("href");
				if (RegexUtil.containsPattern(link, DOMAIN_FILTER)) {
					if (excludeFilter(link)) {
						if (!link.startsWith("http://")) {
							link = DOMAIN + link;
						}
						if (link.startsWith(DOMAIN)) {
							urlList.add(link);
						}
					}
				}
			}
		}
		return urlList;
	}
}
