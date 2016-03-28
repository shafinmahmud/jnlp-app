package shafin.nlp.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import shafin.nlp.analyzer.NGramCandidate;
import shafin.nlp.corpus.model.Document;
import shafin.nlp.pfo.PhraseFirstOccurrenceHelper;
import shafin.nlp.tfidf.TFIDFHelper;
import shafin.nlp.tokenizer.SentenceTokenizer;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;
import shafin.nlp.util.MapUtil;

public class NGramRanking {

	private String CORPUS_LOC = "";
	List<List<String>> COURPUS_PHRASE;
	List<String> COURPUS_TEXT;

	private double SENTENCE_FREQUENCY;

	public NGramRanking(String coupusLoc) throws IOException {
		this.CORPUS_LOC = coupusLoc;
		this.COURPUS_PHRASE = new ArrayList<>();
		this.COURPUS_TEXT = new ArrayList<>();
		preprocessingCorpus(2, 3);
		System.out.println("AVG sentence: " + SENTENCE_FREQUENCY);
	}

	private void preprocessingCorpus(int nGramMin, int nGramMax) throws IOException {

		int numberOfDocument = 0;
		int numberOfSenctence = 0;

		Iterator<String> fileIterator = FileHandler.getRecursiveFileList(CORPUS_LOC).iterator();

		while (fileIterator.hasNext()) {
			File jsonFile = new File(fileIterator.next());

			if (jsonFile.getName().endsWith(".json")) {

				JsonProcessor processor = new JsonProcessor(jsonFile);
				Document doc = (Document) processor.convertToModel(Document.class);
				String text = doc.getArticle();
				COURPUS_TEXT.add(text);

				/* sentence analysis */
				List<String> sentenceTokens = SentenceTokenizer.getSentenceTokenListBn(text);
				numberOfDocument++;
				numberOfSenctence += sentenceTokens.size();

				/* generate candidates tokenizing terms and finding N-grams.
				 * remove candidates having stop-words at beginning and ending.*/
				NGramCandidate nCandidates = new NGramCandidate(text);
				List<String> candidates = nCandidates.getNGramCandidateKeysFilteringSW(nGramMin, nGramMax);
				COURPUS_PHRASE.add(candidates);

				System.out.println(
						"pre processing: sentence > " + sentenceTokens.size() + " candidates > " + candidates.size());
			}
		}
		this.SENTENCE_FREQUENCY = numberOfSenctence / numberOfDocument;
	}

	public static void main(String[] args) throws IOException {

		String filePath = "D:/home/corpus/test/sample/";

		NGramRanking nGramRanking = new NGramRanking(filePath);
		List<String> nTermDoc = nGramRanking.COURPUS_PHRASE.get(1);

		/* scoring vector for each term of a document */
		Map<String, List<Double>> featureVector = new HashMap<String, List<Double>>();

		
		
		/* generating TF-IDF score (S_TFIDF) for each candidate */
		Map<String, Double> tfVector = TFIDFHelper.getTFvector(nTermDoc);
		Map<String, Double> idfVector = TFIDFHelper.getIDFvector(nGramRanking.COURPUS_PHRASE, nTermDoc);
		Map<String, Double> tfidfVector = TFIDFHelper.getTFIDFvector(tfVector, idfVector);
		tfidfVector = MapUtil.sortByValueDecending(tfidfVector);
		//System.out.println(tfidfVector.toString());

			
		
		/* generating Phrase First Occurrence score (S_P) for each candidate */
		Map<String, Double> psoVector = PhraseFirstOccurrenceHelper
				.getPhraseFirstOccurrenceVector(nGramRanking.COURPUS_TEXT.get(1), nTermDoc);
		psoVector = MapUtil.sortByValueDecending(psoVector);
		
		System.out.println(psoVector);
		
	}
}
