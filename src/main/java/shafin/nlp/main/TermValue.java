package shafin.nlp.main;

import shafin.nlp.db.TermIndex;

public class TermValue {

	private final double tf;
	private final double idf;
	private double tf_idf;
	private double pfo;
	private double combind;
	private final boolean isManual;

	public TermValue(TermIndex indexTerm, int numDocs) {
		/*
		 * TF : Implemented as freq. 
		 * IDF : Implemented as log(numDocs/(docFreq+1)).
		 */
		this.tf = Math.sqrt(indexTerm.getTf());
		this.idf = Math.log((double) numDocs / (indexTerm.getDf() + 1)) + 1;
		this.pfo = (double) (1 / Math.sqrt(indexTerm.getPs()));
		this.tf_idf = tf * idf;
		this.combind = this.tf_idf + this.pfo;
		this.isManual = indexTerm.isManual();
	}

	public double getTf_idf() {
		return tf_idf;
	}

	public void setTf_idf(double tf_idf) {
		this.tf_idf = tf_idf;
	}

	public double getPfo() {
		return pfo;
	}

	public void setPfo(double pfo) {
		this.pfo = pfo;
	}

	public double getCombind() {
		return combind;
	}

	public void setCombind(double combind) {
		this.combind = combind;
	}

	public double getTf() {
		return tf;
	}

	public double getIdf() {
		return idf;
	}

	public boolean isManual() {
		return isManual;
	}

	@Override
	public String toString() {
		return this.pfo + " : " + tf_idf + " : " + combind;
	}
}
