package net.shafin.common.model;

import net.shafin.common.util.JsonProcessor;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("docID")
    private Integer docID;

    @JsonProperty("source")
    private String source;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("url")
    private String url;

    @JsonProperty("title")
    private String title;

    @JsonProperty("date")
    private String date;

    @JsonProperty("writter")
    private String writter;

    @JsonProperty("categories")
    private List<String> categories;

    @JsonProperty("manualKeyPhrases")
    private List<String> manualKeyphrases;

    @JsonProperty("automaticKeyPhrases")
    private List<String> automaticKeyphrases;

    @JsonProperty("article")
    private String article;

    public Integer getDocID() {
        return docID;
    }

    public void setDocID(Integer docID) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWritter() {
        return writter;
    }

    public void setWritter(String writter) {
        this.writter = writter;
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

    public List<String> getAutomaticKeyphrases() {
        return automaticKeyphrases;
    }

    public void setAutomaticKeyphrases(List<String> automaticKeyphrases) {
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
        return "docID : " + docID + "\nsource : " + source + "\nlang : " + lang + "\nurl : " + url + "\ntitle : "
                + title + "\ndate : " + date + "\nwritter : " + writter + "\ncategories : " + categories
                + "\nmanualKeyphrases : " + manualKeyphrases.toString() + "\nautomaticKeyphrases : "
                + automaticKeyphrases.toString() + "\narticle : " + article;
    }

    public String toJsonString() throws JsonGenerationException, JsonMappingException, IOException {
        JsonProcessor processor = new JsonProcessor();
        return processor.convertToJson(this);
    }
}
