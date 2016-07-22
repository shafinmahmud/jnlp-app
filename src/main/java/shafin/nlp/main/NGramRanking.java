package shafin.nlp.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import shafin.nlp.analyzer.NGramCandidate;
import shafin.nlp.analyzer.VerbSuffixFilter;
import shafin.nlp.corpus.model.Document;
import shafin.nlp.pfo.PhraseFirstOccurrenceHelper;
import shafin.nlp.stemmer.StemmerHelper;
import shafin.nlp.tfidf.TFIDFHelper;
import shafin.nlp.tokenizer.SentenceTokenizer;
import shafin.nlp.util.FMeasure;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;
import shafin.nlp.util.MapUtil;

public class NGramRanking {

	private String CORPUS_LOC = "";
	List<List<String>> COURPUS_PHRASE;
	List<Document> COURPUS_DOC;

	private double SENTENCE_FREQUENCY;

	public NGramRanking(String coupusLoc) throws IOException, ClassNotFoundException {
		this.CORPUS_LOC = coupusLoc;
		this.COURPUS_PHRASE = new ArrayList<>();
		this.COURPUS_DOC = new ArrayList<Document>();
		preprocessingCorpus(2, 3);
		System.out.println("AVG sentence: " + SENTENCE_FREQUENCY);
	}

	private void preprocessingCorpus(int nGramMin, int nGramMax) throws IOException, ClassNotFoundException {

		int numberOfDocument = 0;
		int numberOfSenctence = 0;

		Iterator<String> fileIterator = FileHandler.getRecursiveFileList(CORPUS_LOC).iterator();

		int i = 0;
		
		while (fileIterator.hasNext()) {
			File jsonFile = new File(fileIterator.next());
			
			MaxentTagger tagger = new MaxentTagger("taggers/bengaliModelFile.tagger");
			
			if (jsonFile.getName().endsWith(".json")) {

				JsonProcessor processor = new JsonProcessor(jsonFile);
				Document doc = (Document) processor.convertToModel(Document.class);
				String text = doc.getArticle();
				COURPUS_DOC.add(doc);

				/* sentence analysis */
				List<String> sentenceTokens = SentenceTokenizer.getSentenceTokenListBn(text);
				numberOfDocument++;
				numberOfSenctence += sentenceTokens.size();

				/*
				 * generate candidates tokenizing terms and finding N-grams.
				 * remove candidates having stop-words at the beginning and
				 * ending.
				 */
				NGramCandidate nGram = new NGramCandidate(text);
				List<String> candidates = nGram.getNGramCandidateKeysFilteringSW(nGramMin, nGramMax);
				List<String> sCandidates = StemmerHelper.getStemmedPhraseList(candidates);
				/*
				 * remove candidates having verb-suffix at the beginning and
				 * ending.
				 */
				List<String> fCandidates = VerbSuffixFilter.filterVerbSuffixCandidates(text, sCandidates, tagger);
				COURPUS_PHRASE.add(fCandidates);

				System.out.println(i++ +" : "+jsonFile.getName()+" sentence: " + sentenceTokens.size() + " candidates: " + sCandidates.size() + " ");
			}
		}
		this.SENTENCE_FREQUENCY = numberOfSenctence / numberOfDocument;
	}

	public Map<String, Double> generateTFIDFfeature(List<String> nTermDoc, List<List<String>> corpusPhrase) {
		/* generating TF-IDF score (S_TFIDF) for each candidate */
		Map<String, Double> tfVector = TFIDFHelper.getTFvector(nTermDoc);
		Map<String, Double> idfVector = TFIDFHelper.getIDFvector(corpusPhrase, nTermDoc);
		Map<String, Double> tfidfVector = TFIDFHelper.getTFIDFvector(tfVector, idfVector);
		tfidfVector = MapUtil.normalizeMapValue(tfidfVector);
		return tfidfVector;
	}

	public Map<String, Double> generatePFOfeature(List<String> nTermDoc, String documentText) {
		/* generating Phrase First Occurrence score (S_P) for each candidate */
		Map<String, Double> pfoVector = PhraseFirstOccurrenceHelper.getPhraseFirstOccurrenceVector(documentText,
				nTermDoc);
		pfoVector = MapUtil.normalizeMapValue(pfoVector);
		return pfoVector;
	}

	public Map<String, Double> combineFeatures(Map<String, Double> featureOne, Map<String, Double> featureTwo) {
		Map<String, Double> combinedFeature = new HashMap<>();
		for(String phrase : featureOne.keySet()){
			Double featureOneVal = featureOne.get(phrase);
			Double featureTwoVal = featureTwo.get(phrase);
			Double comboValue = featureOneVal+featureTwoVal;
			
			System.out.println(phrase+" : one>"+featureOneVal+" two>"+featureTwoVal+" combo>"+comboValue);
			combinedFeature.put(phrase, comboValue);
		}
		return MapUtil.normalizeMapValue(combinedFeature);
	}
	
	
	private List<String> printKeyPhrases(Map<String, Double> scoreVector, int KPNumber){
		int i = 1;
		ArrayList<String> outputKP = new ArrayList<>();
		System.out.println("\n\n");
		for(Map.Entry<String, Double> keyPhrases : scoreVector.entrySet()){
			String kp = keyPhrases.getKey();
			outputKP.add(kp);
			System.out.println("["+kp+" : "+keyPhrases.getValue()+"]");
			if(i == KPNumber)
				break;
			i++;
		}
		return outputKP;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		int docNum = 8;
		String filePath = "D:/home/corpus/test/sample/international/";
		NGramRanking nGramRanking = new NGramRanking(filePath);
		List<String> nTermDoc = nGramRanking.COURPUS_PHRASE.get(docNum);

		/* generating TF-IDF score (S_TFIDF) for each candidate */
		Map<String, Double> tfidfVector = nGramRanking.generateTFIDFfeature(nTermDoc, nGramRanking.COURPUS_PHRASE);
		/* generating Phrase First Occurrence score (S_P) for each candidate */
		Map<String, Double> pfoVector = nGramRanking.generatePFOfeature(nTermDoc, nGramRanking.COURPUS_DOC.get(docNum).getArticle());
		/* ranking Key phrases combining both S_TFIDF and S_P for each candidate */
		Map<String, Double> comboVector = nGramRanking.combineFeatures(tfidfVector, pfoVector);
		
		
		List<String> autoKP = nGramRanking.printKeyPhrases(MapUtil.sortByValueDecending(comboVector),15);
		List<String> manualKP = nGramRanking.COURPUS_DOC.get(docNum).getManualKeyphrases();
		
		System.out.println("\n"+nGramRanking.COURPUS_DOC.get(docNum).getArticle());
		
		System.out.println("\nprecison: "+FMeasure.generatePrecision(manualKP, autoKP));
		System.out.println("recall: "+FMeasure.generateRecall(manualKP, autoKP));
	}
}
