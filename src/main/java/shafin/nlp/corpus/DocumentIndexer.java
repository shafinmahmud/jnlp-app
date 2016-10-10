package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import shafin.nlp.analyzer.BanglaWordAnalyzer;
import shafin.nlp.analyzer.NGramAnalyzer;
import shafin.nlp.corpus.model.Document;
import shafin.nlp.db.IndexService;
import shafin.nlp.db.TermIndex;
import shafin.nlp.pfo.FeatureExtractor;
import shafin.nlp.tokenizer.BnStopWordFilter;
import shafin.nlp.tokenizer.SentenceSpliter;
import shafin.nlp.tokenizer.VerbSuffixFilter;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;
import shafin.nlp.util.Logger;
import shafin.nlp.util.RegexUtil;
import shafin.nlp.util.StringTool;

/*
 * Author : Shafin Mahmud
 * Email  : shafin.mahmud@gmail.com
 * Date	  : 02-10-2016 SUN
 */
public class DocumentIndexer {

	public static final String ID = "id";
	public static final String CONTENT = "content";
	public static final String POSITION = "position";

	public static final int DOC_CRITERIA_SENTENCE_NUM = 30;
	public static final int DOC_CRITERIA_MKP_NUM = 5;

	private final String CORPUS_DIRECTORY;
	private final String TRAIN_DIRECTORY;
	private final String EXTENSION = ".json";

	private final boolean NGRAM_FLAG;
	private final int MIN_NGRAM = 2;
	private final int MAX_NGRAM = 3;

	private final BnStopWordFilter stopWordFilter;
	private final VerbSuffixFilter verbSuffixFilter;

	private final IndexService indexService;

	public DocumentIndexer(String corpusDir, boolean enableNGramTokenize) throws IOException, ClassNotFoundException {
		this.NGRAM_FLAG = enableNGramTokenize;

		this.CORPUS_DIRECTORY = corpusDir;
		this.TRAIN_DIRECTORY = (corpusDir.endsWith("/") || corpusDir.endsWith("\\")) ? corpusDir + "train/"
				: corpusDir + "/train/";
		File dir = new File(TRAIN_DIRECTORY);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		this.indexService = new IndexService();
		this.stopWordFilter = new BnStopWordFilter();
		this.verbSuffixFilter = new VerbSuffixFilter();
	}

	public void iterAndIndexDocuments() throws JsonParseException, JsonMappingException, IOException {
		indexService.recreatIndex();

		List<String> filePaths = FileHandler.getRecursiveFileList(CORPUS_DIRECTORY);
		for (String filePath : filePaths) {
			if (filePath.endsWith(EXTENSION)) {

				String fileName = FileHandler.getFileNameFromPathString(filePath);
				int docID = Integer.valueOf(RegexUtil.getFirstMatch(fileName, "[0-9]+"));

				Logger.print("INDEXING : " + filePath);
				JsonProcessor jsonProcessor = new JsonProcessor(new File(filePath));
				Document document = (shafin.nlp.corpus.model.Document) jsonProcessor
						.convertToModel(shafin.nlp.corpus.model.Document.class);

				String article = StringTool.removeUnicodeSpaceChars(new StringBuilder(document.getArticle()));
				LinkedList<String> SENTENCES = SentenceSpliter.getSentenceTokenListBn(article);

				document = qualifyDocuemnt(document, SENTENCES);

				if (document != null) {
					createIndex(docID, article, SENTENCES);

					/* Insert the Manual Key-Phrases by extracting features */
					List<String> manualKP = document.getManualKeyphrases();
					List<TermIndex> newTerms = new ArrayList<>();

					for (String KP : manualKP) {
						if (!indexService.isExists(docID, KP)) {
							TermIndex index = new TermIndex(docID);
							index.setTerm(KP);

							int tf = FeatureExtractor.getTermOccurrenceCount(article, KP);

							if (tf < 1) {
								indexService.enlistAsZeroFreqTerm(index);
							} else {
								int ps = FeatureExtractor.getOccurrenceOrderInSentence(SENTENCES, KP);

								index.setManual(true);
								index.setTf(tf);
								index.setPs(ps);
								newTerms.add(index);
							}

						} else {
							indexService.setAsManualKP(docID, KP);
						}
					}

					indexService.batchInsertIndex(newTerms);
					indexService.writeDocumentToDisk(document, TRAIN_DIRECTORY + fileName + ".json");

				} else {
					Logger.print("DISQUALIFIED : " + filePath);
				}
			}
		}
		indexService.updateDF();
	}

	/*
	 * DOC CRITERIA 1: Document has to contains at least 30 Sentences; DOC
	 * CRITERIA 2: All Manual KP must have minimum Term-Frequency 1. If not then
	 * the certain KP will be discarded. DOC CRITERIA 3: Remaining number of KP
	 * has to be minimum 5.
	 */
	private Document qualifyDocuemnt(Document doc, LinkedList<String> SENTENCES) {
		if (doc.getArticle().trim().isEmpty()) {
			return null;
		}

		if (SENTENCES.size() < DOC_CRITERIA_SENTENCE_NUM) {
			return null;
		}

		List<String> manualKP = doc.getManualKeyphrases();
		List<String> trueKP = new ArrayList<>();
		for (String KP : manualKP) {
			int freq = FeatureExtractor.getTermOccurrenceCount(doc.getArticle(), KP);
			if (freq > 0) {
				trueKP.add(KP);
			}
		}

		if (trueKP.size() < DOC_CRITERIA_MKP_NUM) {
			return null;
		}

		doc.setManualKeyphrases(trueKP);
		return doc;
	}

	private void createIndex(final int docID, final String TEXT, LinkedList<String> SENTENCES) throws IOException {
		Set<String> TOKENS = new HashSet<>();
		for (String sentence : SENTENCES) {

			if (NGRAM_FLAG) {
				NGramAnalyzer analyzer = new NGramAnalyzer(new StringReader(sentence), MIN_NGRAM, MAX_NGRAM);
				TOKENS.addAll(analyzer.getNGramTokens());
				analyzer.close();
			} else {
				BanglaWordAnalyzer analyzer = new BanglaWordAnalyzer(new StringReader(TEXT));
				TOKENS.addAll(analyzer.getTokenList());
				analyzer.close();
			}
		}

		List<TermIndex> termIndexes = new ArrayList<>();

		for (String token : TOKENS) {
			TermIndex index = new TermIndex(docID);
			index.setTerm(token);
			/*
			 * filter NGram for removing tokens starts or ends with stop-words
			 * filter NGram for removing tokens start or ends with suffixed
			 * verbs
			 */
			if (!this.stopWordFilter.doesContainStopWordInBoundary(token)) {
				if (!this.verbSuffixFilter.doesStartOrEndsWithVerbSuffix(token)) {

					int tf = FeatureExtractor.getTermOccurrenceCount(TEXT, token);
					index.setTf(tf);
					index.setManual(false);

					if (tf < 1) {
						indexService.enlistAsZeroFreqTerm(index);
					} else {
						int ps = FeatureExtractor.getOccurrenceOrderInSentence(SENTENCES, token);
						index.setPs(ps);

						termIndexes.add(index);
					}
				} else {
					indexService.enlistAsVerbSuffixedTerm(index);
				}
			} else {
				indexService.enlistAsStopWordContainedTerm(index);
			}
		}

		indexService.batchInsertIndex(termIndexes);
	}

	public Map<String, TermValue> getFeatureVector(int docId) throws IOException {
		Map<String, TermValue> termVector = new HashMap<>();
		List<TermIndex> terms = indexService.getIndexTerm(docId);
		int numDocs = indexService.countDocs();

		for (TermIndex term : terms) {
			termVector.put(term.getTerm(), new TermValue(term, numDocs));
		}

		return termVector;
	}

	class TermValue {
		private final double tf;
		private final double idf;
		private final double pfo;

		public TermValue(TermIndex indexTerm, int numDocs) {
			/*
			 * TF : Implemented as sqrt(freq). IDF : Implemented as
			 * log(numDocs/(docFreq+1)) + 1.
			 */
			this.tf = Math.sqrt(indexTerm.getTf());
			this.idf = Math.log((double) numDocs / (indexTerm.getDf() + 1)) + 1;
			this.pfo = (double) (1 / Math.sqrt(indexTerm.getPs()));
		}

		public double getTf() {
			return tf;
		}

		public double getIdf() {
			return idf;
		}

		public double getPfo() {
			return pfo;
		}

		@Override
		public String toString() {
			return this.tf + " : " + idf + " : " + pfo;
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String path = "D:/home/dw/json/test/";
		DocumentIndexer indexer = new DocumentIndexer(path, true);
		indexer.iterAndIndexDocuments();

		Map<String, TermValue> values = indexer.getFeatureVector(0);
		for (Map.Entry<String, TermValue> entry : values.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue().toString());
		}

		/*
		 * String string =
		 * "আইএস তাদের সদস্যদের ‘খিলাফতের সৈনিক’ বলে সম্বোধন করে। গুলশান হামলায় জড়িত ও পরে অভিযানে নিহত পাঁচ জঙ্গিকে তারা একই সম্বোধন করে এবং হামলার দায় স্বীকার করে। যদিও বাংলাদেশের আইনশৃঙ্খলা রক্ষাকারী বাহিনীর কর্মকর্তারা বলেছেন, গুলশান হামলায় আইএস নয়, নব্য জেএমবি জড়িত। তামিম চৌধুরী এই নব্য জেএমবির নেতা এবং ১ জুলাই গুলশানের হলি আর্টিজানে হামলার অন্যতম সমন্বয়ক ও পরিকল্পনাকারী। গত ২৭ আগস্ট নারায়ণগঞ্জে জঙ্গিবিরোধী পুলিশের এক অভিযানে তামিম ও তাঁর দুই সহযোগী নিহত হন।"
		 * ; LinkedList<String> SENTENCES =
		 * SentenceSpliter.getSentenceTokenListBn(string); Set<String> TOKENS =
		 * new HashSet<>();
		 * 
		 * for(String sentence : SENTENCES){ NGramAnalyzer analyzer = new
		 * NGramAnalyzer(new StringReader(sentence), 2, 3);
		 * System.out.println(sentence);
		 * 
		 * List<String> list = analyzer.getNGramTokens(); TOKENS.addAll(list);
		 * 
		 * analyzer.close(); }
		 * 
		 * for(String string2 : TOKENS){ System.out.println(string2 +
		 * " : "+PhraseFirstOccurrence.getOccurrenceOrderInSentence(SENTENCES,
		 * string2)); }
		 */
	}
}
