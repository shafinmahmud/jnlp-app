package shafin.nlp.corpus.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class Document implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("docID")
	private int docID;

	@JsonProperty("source")
	private String source;

	@JsonProperty("lang")
	private String lang;

	@JsonProperty("url")
	private String url;

	@JsonProperty("title")
	private String title;

	@JsonProperty("categories")
	private List<String> categories;

	@JsonProperty("manualKeyPhrases")
	private List<String> manualKeyphrases;
	
	@JsonProperty("automaticKeyPhrases")
	private Map<String, List<String>> automaticKeyphrases;

	@JsonProperty("article")
	private String article;

	public Document() {
		super();
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getManualKeyphrases() {
		return manualKeyphrases;
	}

	public void setManualKeyphrases(List<String> manualKeyphrases) {
		this.manualKeyphrases = manualKeyphrases;
	}

	public Map<String, List<String>> getAutomaticKeyphrases() {
		return automaticKeyphrases;
	}

	public void setAutomaticKeyphrases(Map<String, List<String>> automaticKeyphrases) {
		this.automaticKeyphrases = automaticKeyphrases;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	@Override
	public String toString() {
		return "Document [docID=" + docID + ", source=" + source + ", lang=" + lang + ", url=" + url + ", title="
				+ title + ", categories=" + categories + ", manualKeyphrases=" + manualKeyphrases
				+ ", automaticKeyphrases=" + automaticKeyphrases + ", article=" + article + "]";
	}
	
	
}
