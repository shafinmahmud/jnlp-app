package shafin.nlp.pfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhraseFirstOccurrenceHelper {

	public static Map<String, Double> getPhraseFirstOccurrenceVector(String text, List<String> phraseArray) {
		Map<String, Double> pfoVector = new HashMap<>();
		for(String phrase : phraseArray){
			double pfo = FeatureExtractor.getPhraseFirstOccurrence(text, phrase);
			pfoVector.put(phrase, pfo);
		}
		return pfoVector;
	}
}
