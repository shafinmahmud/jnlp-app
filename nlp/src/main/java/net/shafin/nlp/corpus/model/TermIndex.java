package net.shafin.nlp.corpus.model;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import net.shafin.common.util.JsonProcessor;

import java.io.IOException;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class TermIndex implements Comparable<TermIndex> {

    private final int docId;
    private String term;
    private int tf;
    private int df;
    private double ps;
    private double nounFreq;
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

    public double getNounFreq() {
        return nounFreq;
    }

    public void setNounFreq(double nounFreq) {
        this.nounFreq = nounFreq;
    }

    public String toJsonString() throws JsonGenerationException, JsonMappingException, IOException {
        JsonProcessor processor = new JsonProcessor();
        return processor.convertToJson(this);
    }

    @Override
    public int compareTo(TermIndex o) {
        double scoreT = Math.sqrt(this.getTf()) * this.getDf() + this.getPs();
        double scoreO = Math.sqrt(o.getTf()) * o.getDf() + o.getPs();
        if (scoreO == scoreT) {
            return 0;
        }
        return scoreT > scoreO ? -1 : 1;
    }
}
