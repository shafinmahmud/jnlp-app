package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import shafin.nlp.analyzer.BanglaWordAnalyzer;
import shafin.nlp.analyzer.FeatureExtractor;
import shafin.nlp.corpus.model.Document;
import shafin.nlp.tokenizer.SentenceSpliter;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;
import shafin.nlp.util.Logger;
import shafin.nlp.util.StringTool;


/*
 * Author : Shafin Mahmud
 * Email  : shafin.mahmud@gmail.com
 * Date	  : 02-10-2016 SUN
 */
public class CorpusAnalysis {
	private final String CORPUS_DIRECTORY;
	private final List<Document> DOC_LIST;

	public CorpusAnalysis() throws IOException {
		this.CORPUS_DIRECTORY = "D:/home/dw/json/QUALIFIED/";
		this.DOC_LIST = getDocumentList();
	}

	public List<Document> getDocumentList() throws IOException {
		List<String> paths = FileHandler.getRecursiveFileList(CORPUS_DIRECTORY);
		List<Document> list = new ArrayList<>();

		for (String filePath : paths) {
			if (filePath.endsWith(".json")) {
				JsonProcessor jsonProcessor = new JsonProcessor(new File(filePath));
				Document document = (Document) jsonProcessor.convertToModel(Document.class);
				list.add(document);
				Logger.print("LOADING : "+filePath);
			}
		}
		return list;
	}

	public Map<Integer, Integer> countManualKPGramFrequency() {
		Map<Integer, Integer> gramFreqMap = new HashMap<>();
		for (Document document : this.DOC_LIST) {
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

	public Map<Integer, Integer> countManualKPFirstOccurrence() {
		Map<Integer, Integer> occurrenceOrderMap = new HashMap<>();
		for (Document document : this.DOC_LIST) {
			
			String article = StringTool.removeUnicodeSpaceChars(new StringBuilder(document.getArticle()));
			LinkedList<String> SENTENCES = SentenceSpliter.getSentenceTokenListBn(article);
			List<String> manualKPs = document.getManualKeyphrases();
			
			for (String kp : manualKPs) {
				int order = FeatureExtractor.getOccurrenceOrderInSentence(SENTENCES, kp);

				if (occurrenceOrderMap.containsKey(order)) {
					int count = occurrenceOrderMap.get(order);
					occurrenceOrderMap.put(order, count + 1);
				} else {
					occurrenceOrderMap.put(order, 1);
				}
			}
			Logger.print("COUNTING: " + document.getTitle());
		}
		return occurrenceOrderMap;
	}
	
	public static void main(String[] args) throws IOException {
		CorpusAnalysis analysis = new CorpusAnalysis();
		Map<Integer, Integer> map = analysis.countManualKPGramFrequency();
		Map<Integer, Integer> order = analysis.countManualKPFirstOccurrence();
		
		System.out.println("GRAM FREQUENCY FOR MANUAL KP");
		for(Map.Entry<Integer, Integer> entry: map.entrySet()){
			System.out.println(entry.getKey()+" gram : "+entry.getValue());
		}
		
		System.out.println("OCCURRENCE ORDER FREQ FOR MANUAL KP");
		for(Map.Entry<Integer, Integer> entry: order.entrySet()){
			System.out.println(entry.getKey()+" order : "+entry.getValue());
		}
	}
}
