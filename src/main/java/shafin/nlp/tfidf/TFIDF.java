package shafin.nlp.tfidf;

import java.util.Iterator;
import java.util.List;

@Deprecated
public class TFIDF {

	public static double calculateTF(List<String> totalterms, String termToCheck) {
		int count = 0;
		for (String term : totalterms) {
			count = term.equals(termToCheck) ? ++count : count;
		}
		return  (double)count / totalterms.size();
	}

	public static double calculateIDF(List<List<String>> corpusArray, String termToCheck) {
		int documentFreq = 0;
		Iterator<List<String>> corpusIter = corpusArray.iterator();

		while (corpusIter.hasNext()) {
			List<String> document = corpusIter.next();
			documentFreq = document.contains(termToCheck) ? ++documentFreq : documentFreq;
		}		
		return Math.log( (double)( corpusArray.size() /( documentFreq + 1) ) );
	}

}
