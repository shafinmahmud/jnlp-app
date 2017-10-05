package net.shafin.nlp.corpus;

import net.shafin.common.model.Document;
import net.shafin.common.util.*;
import net.shafin.nlp.analyzer.BanglaWordAnalyzer;
import net.shafin.nlp.analyzer.FeatureExtractor;
import net.shafin.nlp.analyzer.NGramAnalyzer;
import net.shafin.nlp.corpus.model.TermIndex;
import net.shafin.nlp.db.IndexService;
import net.shafin.nlp.stemmer.BnStemmerLight;
import net.shafin.nlp.tokenizer.BnStopWordFilter;
import net.shafin.nlp.tokenizer.SentenceSpliter;
import net.shafin.nlp.tokenizer.VerbSuffixFilter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class DocumentIndexer {

    private final String TRAIN_CORPUS_DIRECTORY;
    private final String TEST_CORPUS_DIRECTORY;
    private final String EXTENSION = ".json";

    private final int TRAIN_SET = 1000;
    private final int TEST_SET = 200;

    private final boolean RECREATE_FLAG;
    private final boolean NGRAM_FLAG;

    private final int MIN_NGRAM = 2;
    private final int MAX_NGRAM = 3;

    private final BnStopWordFilter stopWordFilter;
    private final BnStemmerLight bnStemmerLight;

    private final IndexService indexService;

    public DocumentIndexer(String trainDir, String testDir, boolean enableNGramTokenize, boolean willRecreate)
            throws IOException, ClassNotFoundException {
        this.NGRAM_FLAG = enableNGramTokenize;
        this.RECREATE_FLAG = willRecreate;

        this.TRAIN_CORPUS_DIRECTORY = trainDir;
        this.TEST_CORPUS_DIRECTORY = testDir;

        this.indexService = new IndexService();
        this.stopWordFilter = new BnStopWordFilter();
        this.bnStemmerLight = new BnStemmerLight();
    }

    public void iterAndIndexDocuments() throws IOException {
        if (RECREATE_FLAG) {
            indexService.recreatIndex();
        }

        indexDirectory(true, TRAIN_CORPUS_DIRECTORY, TRAIN_SET);
        indexDirectory(false, TEST_CORPUS_DIRECTORY, TEST_SET);

		/* Stem Candidate Terms and Optimize scores */
        /*Logger.print("SHRINKING TRAIN SET...");
        List<TermIndex> trainIndex = shrinkStemmedTermFeatures(true);

		Logger.print("SHRINKING TEST SET...");
		List<TermIndex> testIndex = shrinkStemmedTermFeatures(false);

		List<TermIndex> merged = new ArrayList<>(trainIndex);
		merged.addAll(testIndex);

		Logger.print("DELETING INDEXES...");
		indexService.emptyTableTermIndex();

		Logger.print("RE-INSERTING INDEXES...");
		indexService.batchInsertIndex(merged);*/

        indexService.updateDF();
    }

    public void indexDirectory(Boolean isTrain, String DIR, int number) throws IOException {
        int previous = isTrain ? 0 : 0;
        int counter = 1;
        List<String> filePaths = FileHandler.getRecursiveFileList(DIR);

        for (String filePath : filePaths) {
            if (filePath.endsWith(EXTENSION)) {

                if (counter > previous) {
                    String fileName = FileHandler.getFileNameFromPathString(filePath);
                    int docID = Integer.valueOf(RegexUtil.getFirstMatch(fileName, "[0-9]+"));

                    Logger.print(counter + " : " + "INDEXING : " + filePath);
                    JsonProcessor jsonProcessor = new JsonProcessor(new File(filePath));
                    Document document = (Document) jsonProcessor.convertToModel(Document.class);

                    String article = StringTool.cleanPunctuation(document.getArticle());
                    LinkedList<String> SENTENCES = SentenceSpliter.getSentenceTokenListBn(article);

                    createIndex(isTrain, docID, article, SENTENCES);

					/* Insert the Manual Key-Phrases by extracting features */
                    List<String> manualKP = document.getManualKeyphrases();
                    List<TermIndex> newTerms = new ArrayList<>();

                    for (String KP : manualKP) {

                        KP = KP.trim();
                        //String stemmedKP = bnStemmerLight.stem(KP);

                        if (!indexService.isExists(docID, KP)) {
                            TermIndex index = new TermIndex(docID);
                            index.setTrain(isTrain);
                            index.setTerm(KP);

                            int tf = FeatureExtractor.getTermOccurrenceCount(article, KP);

                            if (tf < 1) {
                                indexService.enlistAsZeroFreqTerm(index);
                            } else {
                                double ps = FeatureExtractor.getNormalizedOccurrenceOrderInSentence(SENTENCES, KP);

                                index.setManual(true);
                                index.setTf(tf);
                                index.setPs(ps);
                                index.setNounFreq(FeatureExtractor.getNounFrequency(KP, indexService));
                                newTerms.add(index);
                            }

                        } else {
                            indexService.setAsManualKP(docID, KP);
                        }
                    }

                    indexService.batchInsertIndex(newTerms);
                }
                counter++;
                if (counter > number) {
                    break;
                }

            }
        }

    }


    private List<TermIndex> shrinkStemmedTermFeatures(boolean isTrain) {
        List<Integer> docIds = indexService.getDocIds(isTrain);
        List<TermIndex> newIndexes = new ArrayList<>();

        int count = 0;
        for (Integer docId : docIds) {
            List<String> uniqueTerms = indexService.getDistinctTermsOfDoc(docId);
            for (String term : uniqueTerms) {

                TermIndex shrinkedIndex = new TermIndex(docId);
                shrinkedIndex.setTerm(term);

                int tf = 0;
                double ps = 99999999;
                boolean isManual = false;

                List<TermIndex> indexes = indexService.getTermIndexByDocIdAndTerm(docId, term);
                for (TermIndex index : indexes) {
                    tf = tf + index.getTf();
                    ps = index.getPs() < ps ? index.getPs() : ps;

                    if (index.isManual()) {
                        isManual = true;
                    }
                }

                shrinkedIndex.setTrain(isTrain);
                shrinkedIndex.setManual(isManual);
                shrinkedIndex.setTf(tf);
                shrinkedIndex.setPs(ps);

                newIndexes.add(shrinkedIndex);
            }

            Logger.print("SHRINKED DOC : " + docId + "  [" + count++ + "/" + docIds.size() + "]");
        }
        return newIndexes;
    }

    private void createIndex(boolean isTrain, final int docID, final String TEXT, LinkedList<String> SENTENCES)
            throws IOException {
        Set<String> TOKENS = new HashSet<>();
        for (String sentence : SENTENCES) {

            if (NGRAM_FLAG) {
                List<String> phrases = SentenceSpliter.getPhraseTokenListFromSentence(sentence);
                for (String phrase : phrases) {
                    NGramAnalyzer analyzer = new NGramAnalyzer(new StringReader(phrase), MIN_NGRAM, MAX_NGRAM);
                    TOKENS.addAll(analyzer.getNGramTokens());
                    analyzer.close();
                }
            } else {
                BanglaWordAnalyzer analyzer = new BanglaWordAnalyzer(new StringReader(TEXT));
                TOKENS.addAll(analyzer.getTokenList());
                analyzer.close();
            }
        }

        List<TermIndex> termIndexes = new ArrayList<>();

        for (String token : TOKENS) {
            TermIndex index = new TermIndex(docID);
            index.setTerm(bnStemmerLight.stem(token));
			/*
			 * filter NGram for removing tokens starts or ends with stop-words
			 * filter NGram for removing tokens start or ends with suffixed
			 * verbs
			 */
            if (!this.stopWordFilter.doesContainStopWordInBoundary(token)) {
                if (!VerbSuffixFilter.doesStartOrEndsWithVerbSuffix(token, this.indexService)) {

                    int tf = FeatureExtractor.getTermOccurrenceCount(TEXT, token);
                    index.setTf(tf);
                    index.setManual(false);
                    index.setTrain(isTrain);

                    if (tf < 1) {
                        indexService.enlistAsZeroFreqTerm(index);
                    } else {
                        double ps = FeatureExtractor.getNormalizedOccurrenceOrderInSentence(SENTENCES, token);
                        index.setPs(ps);
                        index.setNounFreq(FeatureExtractor.getNounFrequency(token, indexService));
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
