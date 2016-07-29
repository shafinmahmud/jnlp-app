package shafin.web.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;

import shafin.nlp.util.JsoupParser;
import shafin.nlp.util.RegexUtil;

public class LinkExtractor {

	private final String DOMAIN;
	private final String DOMAIN_FILTER;
	private final List<String> EXCLUDE_STRINGS;
	private String URL;

	public LinkExtractor(SpiderConfig config) {
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

	private Response getHTMLFromURL() throws IOException {
		JsoupParser jsoupParser = new JsoupParser();
		return jsoupParser.getRedirectedResponseFromGetRequest(encodeURL(this.URL));
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

	public static String encodeURL(String url) {
		try {
			String result = URLEncoder.encode(url, "UTF-8").replaceAll("\\%3A", ":").replaceAll("\\%2F", "/")
					.replaceAll("\\%26", "&").replaceAll("\\+", "%20").replaceAll("\\%21", "!").replaceAll("\\%27", "'")
					.replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return url;
		}
	}
}
