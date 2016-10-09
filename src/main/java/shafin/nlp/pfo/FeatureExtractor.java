package shafin.nlp.pfo;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import shafin.nlp.analyzer.BanglaWordAnalyzer;
import shafin.nlp.tokenizer.SentenceSpliter;
import shafin.nlp.util.RegexUtil;

public class FeatureExtractor {

	private static final String BOUNDARY = "\\s|\\(|\\)|\\.|\\'|\"|!|,|\\?|;";
	public static final String WORD_BOUNDARY_START = "(^|"+BOUNDARY+")";
	public static final String WORD_BOUNDARY_END = "($|"+BOUNDARY+")";
	
	public static final String NON_WORD_FORMING_CHARS_REGEX = "[.,’‘';:\\(\\)\\/\\s]+";

	public static int getOccurrenceOrderInSentence(LinkedList<String> sentences, String phrase) {
		String wildPhrase = getWildPhraseRegex(phrase);	
		for (int i = 0; i < sentences.size(); i++) {
			if (RegexUtil.containsPattern(sentences.get(i), wildPhrase)) {
				return i + 1;
			}
		}
		return 0;
	}

	public static int getTermOccurrenceCount(String text, String term){
		String wildPhrase = WORD_BOUNDARY_START+getWildPhraseRegex(term)+WORD_BOUNDARY_END;
		return RegexUtil.countMatches(text, wildPhrase);	
	}
	
	
	public static String getWildPhraseRegex(String phrase){
		BanglaWordAnalyzer wordAnalyzer = new BanglaWordAnalyzer(new StringReader(phrase));
		List<String> wordTokens = wordAnalyzer.getTokenList();
		wordAnalyzer.close();
		
		int size = wordTokens.size();

		StringBuffer wildPhrase = new StringBuffer();
		for (int i = 0; i < size; i++) {
			wildPhrase.append(wordTokens.get(i));
			if (i != size-1) {
				wildPhrase.append(NON_WORD_FORMING_CHARS_REGEX);
			}
		}
		return wildPhrase.toString();
	}
	
	public static double getPhraseFirstOccurrence(List<String> sentenceArray, String phrase) {
		int SP = 0;
		for (int i = 0; i < sentenceArray.size(); i++) {
			if (sentenceArray.get(i).contains(phrase)) {
				SP = i + 1;
				return (double) (1 / Math.sqrt(SP));
			}
		}
		return SP;
	}

	
	public static double getPhraseFirstOccurrence(String text, String phrase) {

		int SP = 0;
		/* sentence analysis */
		List<String> sentenceArray = SentenceSpliter.getSentenceTokenListBn(text);
		for (int i = 0; i < sentenceArray.size(); i++) {
			if (sentenceArray.get(i).contains(phrase)) {
				SP = i + 1;
				return (double) (1 / Math.sqrt(SP));
			}
		}
		return SP;
	}

	public static void main(String[] args) {
		String text = "শেষ ম্যাচের আগেই অস্ট্রেলিয়ান অধিনায়ক স্মিথ বলেছিলেন, বিশেষ  শেষ  ‘ওদের ব্যাটিং তো ভয়ংকর, বিশেষ করে বিরাট কোহলি।’ ওদের মানে ভারত।";
				
		String phrase = "শেষ ";
		System.out.println(getTermOccurrenceCount(text, phrase));
	}
}
