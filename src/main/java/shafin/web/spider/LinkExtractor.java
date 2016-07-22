package shafin.web.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;

import shafin.nlp.util.JsoupParser;

public class LinkExtractor {

	private final String DOMAIN_FILTER;
	private String URL;

	public LinkExtractor(Config config) {
		this.DOMAIN_FILTER = config.getDOMAIN_FILTER_PATTERN();
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

	public List<String> extractURL() {
		Response response = getHTMLFromURL();
		try {
			List<Element> links = response.parse().select("a");
			List<String> urlList = new ArrayList<>();
			for (Element e : links) {
				String link = e.attr("href");
				if (RegexUtil.containsPattern(link, DOMAIN_FILTER) && !link.contains("m.dw.com")) {
					if (!link.startsWith("http://")) {
						link = "http://www.dw.com" + link;
					}
					urlList.add(link);
				}

			}
			return urlList;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
