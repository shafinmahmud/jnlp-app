package shafin.web.crawler.parser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Element;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.util.JsoupParser;


public class DWParser implements DocumentParser{
	
	private final String LAN = "BN";
	private final String SOURCE = "http://www.dw.com/";
	private final String URL;
	private final String HTML;
	private JsoupParser parser;
	
	public DWParser(String url) throws MalformedURLException, IOException {
		this.URL = url;
		this.parser = new JsoupParser();
		url = encodeURL(url);
		this.HTML = parser.getResponseFromGetRequest(url).parse().html();
	}
	
	@Override
	public String parseSource() {
		return SOURCE;
	}

	@Override
	public String parseLang() {
		return LAN;
	}

	@Override
	public String parseURL() {
		return URL;
	}

	@Override
	public String parseTitle() {
		// T#bodyContent h1
		try {
			return parser.stripHtmlTag(parser.parseDataFromHtml(this.HTML, "#bodyContent h1"));
		} catch (NullPointerException | IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String parseDate() {
		// #bodyContent li>strong:contains(তারিখ) -- > then parent
		try {
			Element date = parser.parseElementByFirstFromHtml(this.HTML, "#bodyContent li>strong:contains(তারিখ)");
			return date.parent().text().replace("তারিখ", "");
		} catch (NullPointerException e) {
			return "";
		}	
	}

	@Override
	public String parseWritter() {
		// #bodyContent li>strong:contains(প্রতিবেদন) -- > then parent 
		try {
			Element writter = parser.parseElementByFirstFromHtml(this.HTML, "#bodyContent li>strong:contains(প্রতিবেদন)");
			return writter.parent().text().replace("প্রতিবেদন", "");
		} catch (NullPointerException e) {
			return "";
		}
	}

	@Override
	public List<String> parseCategories() {
		try {
			Element cat = parser.parseElementByFirstFromHtml(this.HTML, "#bodyContent h4.artikel");
			String[] arr = cat.text().split(",");
			return Arrays.asList(arr);
		} catch (NullPointerException e) {
			return  new ArrayList<>();
		}
	}

	@Override
	public List<String> parseManualKeyphrases() {
		//  #bodyContent li>strong:contains(কি-ওয়ার্ডস) -- > then parent  
		try {
			Element kws = parser.parseElementByFirstFromHtml(this.HTML, "#bodyContent li>strong:contains(কি-ওয়ার্ডস)");
			String[] arr = kws.parent().text().replaceAll("কি-ওয়ার্ডস", "").split(",");
			
			Set<String> kwSet = new HashSet<>();
			for(String kw : arr){
				kwSet.add(kw.trim());
			}
			
			List<String> kwList = new ArrayList<>();
			kwList.addAll(kwSet);
			return kwList;
		} catch (NullPointerException e) {
			return  new ArrayList<>();
		}
		
	}

	@Override
	public String parseArticle() {
		// #bodyContent >div.col3 div.group
		// .intro
		// exclude .picBox .gallery 
		// remove <strong>আপনার কি কিছু বলার আছে? লিখুন নীচের মন্তব্যের ঘরে৷</strong>
		try {
			String intro = parser.parseElementByFirstFromHtml(this.HTML, "#bodyContent >div.col3 p.intro").text();
			Element contentElement = parser.parseElementByFirstFromHtml(this.HTML, "#bodyContent >div.col3 div.group");
			contentElement = JsoupParser.removeElement(contentElement, ".picBox");
			contentElement = JsoupParser.removeElement(contentElement, ".gallery");
			contentElement = JsoupParser.removeElement(contentElement, "strong:contains(আপনার কি কিছু বলার আছে?)");
			return intro+" "+contentElement.text();
		} catch (NullPointerException e) {
			return "";
		}
		
	}

	public Document getParsedDocument(){
		Document document = new Document();
		document.setArticle(parseArticle());
		document.setCategories(parseCategories());
		document.setDate(parseDate());
		document.setLang(parseLang());
		document.setSource(parseSource());
		document.setTitle(parseTitle());
		document.setUrl(parseURL());
		document.setWritter(parseWritter());
		document.setManualKeyphrases(parseManualKeyphrases());
		document.setAutomaticKeyphrases(new ArrayList<String>());
		return document;
	}
	
	
	public static String encodeURL(String url){
	    try {
	       String result = URLEncoder.encode(url, "UTF-8")
	    		   	.replaceAll("\\%3A", ":")
	    		   	.replaceAll("\\%2F", "/")
	    		   	.replaceAll("\\%26", "&")
	                .replaceAll("\\+", "%20")
	                .replaceAll("\\%21", "!")
	                .replaceAll("\\%27", "'")
	                .replaceAll("\\%28", "(")
	                .replaceAll("\\%29", ")")
	                .replaceAll("\\%7E", "~");
	       return result;
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	        return url;
	    }
	}
	
	public static void main(String[] args) throws URISyntaxException {
		DWParser parser;
		try {
			String url = "http://www.dw.com/bn/একশ-ইউরোতে-চিড়িয়াখানায়-রাত-কাটানোর-সুযোগ/a-4583689";
			
			parser = new DWParser(url);
			String txt = parser.getParsedDocument().toString();
			System.out.println(txt);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
