package net.shafin.nlp.tokenizer;

import net.shafin.nlp.analyzer.NGramAnalyzer;
import net.shafin.nlp.analyzer.BanglaWordAnalyzer;
import net.shafin.common.util.FileHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class BnStopWordFilter {

    public static final String PROJECT_DIR = "D:/DOCUMENT/PROJECTS/NLP/";
    public final List<String> STOP_WORDS;

    public BnStopWordFilter() {
        List<String> words = FileHandler.readFile(PROJECT_DIR + "nlp/resources/stopword.txt");
        this.STOP_WORDS = new ArrayList<>();
        for (String word : words) {
            this.STOP_WORDS.add(word.trim());
        }
        Collections.sort(this.STOP_WORDS);
        FileHandler.writeListToFile(PROJECT_DIR + "nlp/resources/stopword.txt", this.STOP_WORDS);
    }

    public boolean doesContainStopWordInBoundary(String ngram) {
        BanglaWordAnalyzer wordAnalyzer = new BanglaWordAnalyzer(new StringReader(ngram));
        List<String> wordTokens = wordAnalyzer.getTokenList();
        wordAnalyzer.close();

        int size = wordTokens.size();
        if (size > 0) {
            String firstWord = wordTokens.get(0);
            String lastWord = wordTokens.get(size - 1);

            if (Collections.binarySearch(STOP_WORDS, firstWord) >= 0
                    | Collections.binarySearch(STOP_WORDS, lastWord) >= 0) {
                return true;
            }
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws IOException {
        BnStopWordFilter stopWordFilter = new BnStopWordFilter();

        String text = "কোনো  শেষ ম্যাচের আগেই অস্ট্রেলিয়ান অধিনায়ক স্মিথ বলেছিলেন, বিশেষ  শেষ  ‘ওদের ব্যাটিং তো ভয়ংকর, বিশেষ করে বিরাট কোহলি।’ ওদের মানে ভারত।";
        NGramAnalyzer analyzer = new NGramAnalyzer(new StringReader(text), 2, 3);

        //List<String> ngrams = analyzer.getNGramTokens();
        analyzer.close();

		/*
         * for(String ngram : ngrams){ System.out.println(ngram +
		 * " : "+stopWordFilter.doesContainStopWordInBoundary(ngram)); }
		 */

        String word = "মতো";
        System.out.println(word + " >>>" + stopWordFilter.doesContainStopWordInBoundary(word));
    }
}
