package shafin.nlp.corpus.model;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import shafin.nlp.util.JsonProcessor;

/*
 * Author : Shafin Mahmud
 * Email  : shafin.mahmud@gmail.com
 * Date	  : 02-10-2016 SUN
 */
public class TermIndex {

	private final int docId;
	private String term;
	private int tf;
	private int df;
	private double ps;
	private boolean isManual;
	private boolean isTrain;

	public TermIndex(int docId) {
		this.docId = docId;
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

	public double getPs() {
		return ps;
	}

	public void setPs(double ps) {
		this.ps = ps;
	}

	public boolean isManual() {
		return isManual;
	}

	public void setManual(boolean isManual) {
		this.isManual = isManual;
	}

	public int getDocId() {
		return docId;
	}

	public boolean isTrain() {
		return isTrain;
	}

	public void setTrain(boolean isTrain) {
		this.isTrain = isTrain;
	}

	public String toJsonString() throws JsonGenerationException, JsonMappingException, IOException {
		JsonProcessor processor = new JsonProcessor();
		return processor.convertToJson(this);
	}
}
