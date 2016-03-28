package shafin.nlp.tfidf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TFIDFHelper {

	public static Map<String, Double> getTFvector(List<String> nTermDoc) {
		Map<String, Double> tfVector = new HashMap<>();
		for (String termToCheck : nTermDoc) {
			double tf = TFIDF.calculateTF(nTermDoc, termToCheck);
			tfVector.put(termToCheck, tf);
		}
		return tfVector;
	}

	public static Map<String, Double> getIDFvector(List<List<String>> corpusArray, List<String> nTermDoc) {
		Map<String, Double> idfVector = new HashMap<String, Double>();
		for (String termToCheck : nTermDoc) {
			double tf = TFIDF.calculateIDF(corpusArray, termToCheck);
			idfVector.put(termToCheck, tf);
		}
		return idfVector;
	}

	public static Map<String, Double> getTFIDFvector(Map<String, Double> tfVector, Map<String, Double> idfVector) {
		Map<String, Double> tfidfVector = new HashMap<>();
		for(String term : tfVector.keySet()){
			tfidfVector.put(term, tfVector.get(term)*idfVector.get(term));
		}
		return tfidfVector;
	}

}
