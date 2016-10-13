package shafin.nlp.corpus;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

	private final String TRAIN_CORPUS_DIRECTORY;
	private final String TEST_CORPUS_DIRECTORY;
	private final String EXTENSION = ".json";

	private final int TRAIN_SET = 500;
	private final int TEST_SET = 100;

	private final boolean RECREATE_FLAG;
	private final boolean NGRAM_FLAG;

	private final int MIN_NGRAM = 2;
	private final int MAX_NGRAM = 3;

	private final BnStopWordFilter stopWordFilter;
	private final VerbSuffixFilter verbSuffixFilter;

	private final IndexService indexService;

	public DocumentIndexer(String trainDir, String testDir, boolean enableNGramTokenize, boolean willRecreate)
			throws IOException, ClassNotFoundException {
		this.NGRAM_FLAG = enableNGramTokenize;
		this.RECREATE_FLAG = willRecreate;

		this.TRAIN_CORPUS_DIRECTORY = trainDir;
		this.TEST_CORPUS_DIRECTORY = testDir;

		this.indexService = new IndexService();
		this.stopWordFilter = new BnStopWordFilter();
		this.verbSuffixFilter = new VerbSuffixFilter();
	}

	public void iterAndIndexDocuments() throws IOException {
		if (RECREATE_FLAG) {
			indexService.recreatIndex();
		}

		indexDirectory(true, TRAIN_CORPUS_DIRECTORY, TRAIN_SET);
		indexDirectory(false, TEST_CORPUS_DIRECTORY, TEST_SET);
	}

	public void indexDirectory(Boolean isTrain, String DIR, int number) throws IOException {
		int counter = 1;
		List<String> filePaths = FileHandler.getRecursiveFileList(DIR);

		for (String filePath : filePaths) {
			if (filePath.endsWith(EXTENSION)) {

				String fileName = FileHandler.getFileNameFromPathString(filePath);
				int docID = Integer.valueOf(RegexUtil.getFirstMatch(fileName, "[0-9]+"));

				Logger.print(counter + " : " + "INDEXING : " + filePath);
				JsonProcessor jsonProcessor = new JsonProcessor(new File(filePath));
				Document document = (shafin.nlp.corpus.model.Document) jsonProcessor
						.convertToModel(shafin.nlp.corpus.model.Document.class);

				String article = StringTool.removeUnicodeSpaceChars(new StringBuilder(document.getArticle()));
				LinkedList<String> SENTENCES = SentenceSpliter.getSentenceTokenListBn(article);

				createIndex(isTrain, docID, article, SENTENCES);

				/* Insert the Manual Key-Phrases by extracting features */
				List<String> manualKP = document.getManualKeyphrases();
				List<TermIndex> newTerms = new ArrayList<>();

				for (String KP : manualKP) {
					if (!indexService.isExists(docID, KP)) {
						TermIndex index = new TermIndex(docID);
						index.setTrain(isTrain);
						KP = KP.trim();
						index.setTerm(KP);

						int tf = FeatureExtractor.getTermOccurrenceCount(article, KP);

						if (tf < 1) {
							indexService.enlistAsZeroFreqTerm(index);
						} else {
							double ps = FeatureExtractor.getNormalizedOccurrenceOrderInSentence(SENTENCES, KP);

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

				counter++;
				if (counter > number) {
					break;
				}

			}
		}
		indexService.updateDF();
	}

	private void createIndex(boolean isTrain, final int docID, final String TEXT, LinkedList<String> SENTENCES)
			throws IOException {
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
					index.setTrain(isTrain);

					if (tf < 1) {
						indexService.enlistAsZeroFreqTerm(index);
					} else {
						double ps = FeatureExtractor.getNormalizedOccurrenceOrderInSentence(SENTENCES, token);
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

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String train = "D:/home/dw/json/QUALIFIED/TRAIN/";
		String test = "D:/home/dw/json/QUALIFIED/TEST/";
		DocumentIndexer indexer = new DocumentIndexer(train, test, true, true);
		indexer.iterAndIndexDocuments();

	}
}
