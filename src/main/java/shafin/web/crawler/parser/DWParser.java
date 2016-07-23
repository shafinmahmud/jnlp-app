package shafin.web.crawler.parser;

import java.util.List;

import shafin.nlp.corpus.model.Document;


public class DWParser implements DocumentParser{
	
	private final String HTML;
	
	public DWParser(String html) {
		this.HTML = html;
	}
	
	@Override
	public String parseSource() {
		return null;
	}

	@Override
	public String parseLang() {
		return null;
	}

	@Override
	public String parseURL() {
		return null;
	}

	@Override
	public String parseTitle() {
		// T#bodyContent h1
		return null;
	}

	@Override
	public String parseDate() {
		// #bodyContent li>strong:contains(তারিখ) -- > then parent
		return null;
	}

	@Override
	public String parseWritter() {
		// #bodyContent li>strong:contains(প্রতিবেদন) -- > then parent 
		return null;
	}

	@Override
	public List<String> parseCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> parseManualKeyphrases() {
		//  #bodyContent li>strong:contains(কি-ওয়ার্ডস) -- > then parent  
		return null;
	}

	@Override
	public String parseArticle() {
		// #bodyContent >div.col3
		// .intro
		// exclude .picBox .gallery
		// remove <strong>আপনার কি কিছু বলার আছে? লিখুন নীচের মন্তব্যের ঘরে৷</strong>
		return null;
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
		return document;
	}
}
