package shafin.nlp.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


/**
 *
 * @author SHAFIN
 */

public class JsoupParser {

	private String USER_AGENT;
	private int TIME_OUT_VALUE;

	public JsoupParser() {
		this.USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) "
				+ "AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";
		this.TIME_OUT_VALUE = 1000 * 60;
	}

	public JsoupParser(String uSER_AGENT, int tIME_OUT_VALUE) {
		super();
		this.USER_AGENT = uSER_AGENT;
		this.TIME_OUT_VALUE = tIME_OUT_VALUE;
	}

	// URL parsing
	public String getHtmlFromGetRequest(String url) throws IOException, MalformedURLException {
		Document htmlDoc = Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIME_OUT_VALUE).get();
		return htmlDoc.html();
	}

	public Document getDocumentFromGetRequest(String url) throws IOException, MalformedURLException {
		return Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIME_OUT_VALUE).get();
	}

	public Response getResponseFromGetRequest(String url) throws IOException, MalformedURLException {
		Response response = Jsoup.connect(url)
					.userAgent(USER_AGENT).followRedirects(false).timeout(TIME_OUT_VALUE)
				.execute();
		return response;
	}

	public Response getRedirectedResponseFromGetRequest(String url) throws IOException, MalformedURLException {
		Response response = Jsoup.connect(url).userAgent(USER_AGENT).followRedirects(true).timeout(TIME_OUT_VALUE)
				.execute();
		return response;
	}

	// JSON parsing
	public String getTextFromGetRequestIgoningContentType(String url) throws IOException {
		return Jsoup.connect(url).userAgent(USER_AGENT).followRedirects(true).timeout(TIME_OUT_VALUE)
				.ignoreContentType(true).execute().body();
	}

	@SuppressWarnings("rawtypes")
	public Response getResponseFromPostRequest(String url, HashMap<String, String> header, HashMap<String, String> body,
			Map<String, String> cookies) throws IOException {
		Connection con = Jsoup.connect(url);

		Iterator headIterator = header.entrySet().iterator();
		while (headIterator.hasNext()) {
			HashMap.Entry data = (HashMap.Entry) headIterator.next();
			con.header(data.getKey().toString(), data.getValue().toString());
		}

		Response response = con.cookies(cookies).data(body).timeout(TIME_OUT_VALUE).method(Connection.Method.POST)
				.execute();
		return response;
	}

	// Parsing methods
	public Element parseElementByFirstFromHtml(String html, String findParameter) throws NullPointerException {
		Document doc = Jsoup.parseBodyFragment(html);
		return doc.select(findParameter).first();
	}

	public Element parseElementByLastFromHtml(String html, String findParameter) throws NullPointerException {
		Document doc = Jsoup.parseBodyFragment(html);
		return doc.select(findParameter).last();
	}

	public String parseDataFromHtml(String html, String findParameter) throws NullPointerException {
		Document doc = Jsoup.parseBodyFragment(html);
		return doc.select(findParameter).first().toString();
	}

	public Iterator<Element> parseDataIteratortFromHtml(String html, String identifier) {
		Document doc = Jsoup.parseBodyFragment(html);
		return doc.select(identifier).iterator();
	}

	public List<Element> parseDataListFromHtml(String html, String identifier) {
		Document doc = Jsoup.parseBodyFragment(html);
		return doc.select(identifier);
	}

	public String parseLinkFromHtmlSegment(String html) throws NullPointerException {
		Document doc = Jsoup.parseBodyFragment(html);
		Element link = doc.getElementsByTag("a").first();
		return link.attr("href");
	}

	public String parseImgSource(String imgTag) {
		Document doc = Jsoup.parseBodyFragment(imgTag);
		Elements img = doc.select("img[src]");
		return img.attr("src");
	}

	public String replaceLiTag(String input, String replaceString) {
		return input.replace("<ul>", "").replace("</ul>", "").replace("<li>", replaceString).replace("</li>", "");
	}
	
	public String replaceHTag(String input, String replaceString) {
		input = input.replace("<h3>", replaceString).replace("</h3>", replaceString);
		return input.replace("<h2>", replaceString).replace("</h2>", replaceString);
	}

	public String replaceStrongTag(String input, String replaceString) {
		return input.replace("<strong>", replaceString).replace("</strong>", replaceString);
	}

	public String replaceBrTag(String input, String replaceString) {
		
		String[] splittedPortions = input.split("<br>");
		StringBuilder stringBuilder = new StringBuilder();
		
		for (String s : splittedPortions) {
			stringBuilder.append(replaceString + s);
		}
		return stringBuilder.toString();
	}

	public String stripHtmlTag(String html) throws IOException {
		return Jsoup.parse(html).text();
	}

	public String stripHtmlTagRemovingSpan(String html) throws IOException{		
		Matcher matcher = Pattern.compile("\\s<span*(.*?)<\\/span>").matcher(html);
		return matcher.find() ? stripHtmlTag(html.replace(matcher.group(0), "")) : stripHtmlTag(html);
	}
	
	public String stripComments(String html) {

		Document doc = Jsoup.parse(html);
		removeComments(doc);
		return doc.html();
	}

	public static Element removeElement(Element contentElement, String removingElementCSS){
		for(Element element : contentElement.select(removingElementCSS)){
			element.remove();
		}
		return contentElement;
	}
	
	private static void removeComments(Node node) {
		
		for (int i = 0; i < node.childNodes().size();) {
			Node child = node.childNode(i);
			if (child.nodeName().equals("#comment"))
				child.remove();
			else {
				removeComments(child);
				i++;
			}
		}
	}

}
