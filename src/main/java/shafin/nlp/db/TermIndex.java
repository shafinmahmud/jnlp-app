package shafin.nlp.db;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import shafin.nlp.util.JsonProcessor;

public class TermIndex {

	private final int docId;
	private String term;
	private int tf;
	private int df;
	private int ps;

	public TermIndex(int docId) {
		this.docId = docId;
	}

	public int getDocId() {
		return docId;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}

	public int getDf() {
		return df;
	}

	public void setDf(int df) {
		this.df = df;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public String toJsonString() throws JsonGenerationException, JsonMappingException, IOException{
		JsonProcessor processor = new JsonProcessor();
		return processor.convertToJson(this);
	}
}
