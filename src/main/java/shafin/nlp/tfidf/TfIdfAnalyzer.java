package shafin.nlp.tfidf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shafin.nlp.analyzer.WrdAnalyzer;
import shafin.nlp.util.MapUtil;

public class TfIdfAnalyzer {

	private final String TEXT;

	public TfIdfAnalyzer(String text) {
		this.TEXT = text;
	}

	public Map<String, Double> getTermFrequencies() {
		Map<String, Double> tfMap = new HashMap<>();

		WrdAnalyzer wordAnalyzer = new WrdAnalyzer(TEXT);
		List<String> list = wordAnalyzer.getUniqueWordTokens();

		for (String word : list) {
			double tf = TFIDF.calculateTF(list, word);
			tfMap.put(word, tf);
		}

		return MapUtil.sortByValueDecending(tfMap);
	}
}
