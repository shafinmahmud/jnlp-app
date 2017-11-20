package net.shafin.nlp.corpus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class TermValue implements Comparable<TermValue>, Serializable {

    private static final long serialVersionUID = 1L;

    private int docId;
    private String term;

    private double tf;
    private double idf;
    private double tf_idf;
    private double pfo;
    private double combined;
    private double nounFreq;
    private boolean isManual;

    private double probability;

    public TermValue(TermIndex indexTerm, int numDocs) {
        this.docId = indexTerm.getDocId();
        this.term = indexTerm.getTerm();
        /*
         * TF : Implemented as freq. IDF : Implemented as
		 * log(numDocs/(docFreq+1)).
		 */
        this.tf = Math.sqrt(indexTerm.getTf());
        this.idf = Math.log((double) numDocs / (indexTerm.getDf() + 1)) + 1;
        this.pfo = (double) (1 / Math.sqrt(indexTerm.getPs()));
        this.tf_idf = tf * idf;
        this.combined = this.tf_idf + this.pfo;
        this.isManual = indexTerm.isManual();
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public double getTf() {
        return tf;
    }

    public void setTf(double tf) {
        this.tf = tf;
    }

    public double getIdf() {
        return idf;
    }

    public void setIdf(double idf) {
        this.idf = idf;
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
        return combined;
    }

    public void setCombind(double combind) {
        this.combined = combind;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean isManual) {
        this.isManual = isManual;
    }

    public double getNounFreq() {
        return nounFreq;
    }

    public void setNounFreq(double nounFreq) {
        this.nounFreq = nounFreq;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public static List<TermValue> normalizeTermValueList(List<TermValue> list) {
        List<Double> tflist = new ArrayList<>();
        List<Double> idflist = new ArrayList<>();
        List<Double> tfIdflist = new ArrayList<>();
        List<Double> combList = new ArrayList<>();

        for (TermValue termValue : list) {
            tflist.add(termValue.getTf());
            idflist.add(termValue.getIdf());
            tfIdflist.add(termValue.getTf_idf());
            combList.add(termValue.getCombind());
        }

        double maxTf = Collections.max(tflist);
        double maxIdf = Collections.max(idflist);
        double maxTfidf = Collections.max(tfIdflist);
        double maxComb = Collections.max(combList);

        for (TermValue termValue : list) {
            termValue.setTf(termValue.getTf() / maxTf);
            termValue.setIdf(termValue.getIdf() / maxIdf);
            termValue.setTf_idf(termValue.getTf_idf() / maxTfidf);
            termValue.setCombind(termValue.getCombind() / maxComb);
        }
        return list;
    }

    public String toCsvString() {
        int dataClazz = this.isManual() ? 1 : 0;
        return dataClazz + "," + this.getTf() + "," + this.getIdf() + "," + this.getPfo() + "," + this.getNounFreq();
    }

    @Override
    public String toString() {
        return this.pfo + " : " + tf_idf + " : " + combined;
    }

    @Override
    public int compareTo(TermValue o) {
        if (this.probability == o.probability) {
            return 0;
        }

        return this.probability > o.probability ? -1 : 1;
    }
}
