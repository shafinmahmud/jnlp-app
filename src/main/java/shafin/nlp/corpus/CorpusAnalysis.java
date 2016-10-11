package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shafin.nlp.analyzer.BanglaWordAnalyzer;
import shafin.nlp.corpus.model.Document;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;
import shafin.nlp.util.Logger;

public class CorpusAnalysis {
	private final String CORPUS_DIRECTORY;

	public CorpusAnalysis() {
		this.CORPUS_DIRECTORY = "D:/home/dw/json/QUALIFIED/";
	}

	public List<Document> getDocumentList() throws IOException {
		List<String> paths = FileHandler.getRecursiveFileList(CORPUS_DIRECTORY);
		List<Document> list = new ArrayList<>();

		for (String filePath : paths) {
			if (filePath.endsWith(".json")) {
				JsonProcessor jsonProcessor = new JsonProcessor(new File(filePath));
				Document document = (Document) jsonProcessor.convertToModel(Document.class);
				list.add(document);
			}
		}
		return list;
	}

	public Map<Integer, Integer> countManualKPGramFrequency(List<Document> list) {
		Map<Integer, Integer> gramFreqMap = new HashMap<>();
		for (Document document : list) {
			List<String> manualKPs = document.getManualKeyphrases();
			for (String kp : manualKPs) {
				BanglaWordAnalyzer analyzer = new BanglaWordAnalyzer(new StringReader(kp));
				int gram = analyzer.getTokenList().size();
				analyzer.close();

				if (gramFreqMap.containsKey(gram)) {
					int count = gramFreqMap.get(gram);
					gramFreqMap.put(gram, count + 1);
				} else {
					gramFreqMap.put(gram, 1);
				}
			}
			Logger.print("COUNTING: " + document.getTitle());
		}
		return gramFreqMap;
	}

	public static void main(String[] args) throws IOException {
		CorpusAnalysis analysis = new CorpusAnalysis();
		Map<Integer, Integer> map = analysis.countManualKPGramFrequency(analysis.getDocumentList());
		for(Map.Entry<Integer, Integer> entry: map.entrySet()){
			System.out.println(entry.getKey()+" gram : "+entry.getValue());
		}
	}
}
