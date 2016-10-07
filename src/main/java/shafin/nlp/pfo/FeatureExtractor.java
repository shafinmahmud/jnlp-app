package shafin.nlp.pfo;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import shafin.nlp.analyzer.BanglaWordAnalyzer;
import shafin.nlp.tokenizer.SentenceSpliter;
import shafin.nlp.util.RegexUtil;

public class FeatureExtractor {

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
		String wildPhrase = getWildPhraseRegex(term);
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
		String text = "ম্যাচের আগেই অস্ট্রেলিয়ান অধিনায়ক স্মিথ বলেছিলেন, ‘ওদের ব্যাটিং তো ভয়ংকর, বিশেষ করে বিরাট কোহলি।’ ওদের মানে ভারত।"
				+ "সেই ভারতের ব্যাটিং নিয়ে কথা বলতে গিয়ে স্মিথ কেন আলাদা করে কোহলির নামটা বলেছিলেন, ম্যাচ শেষে মনে হয় তা বুঝে গেছেন সবাই। মোহালির আইএস বিন্দ্রা স্টেডিয়ামে কাল ভারত তো আসলে"
				+ "শুধু নামেই অস্ট্রেলিয়ার প্রতিপক্ষ ছিল। খেললেন তো কোহলিই। ৫১ বলে অপরাজিত ৮২ রানের অসাধারণ এক ইনিংস। অধিনায়ক স্টিভেন স্কোরকার্ড বলছে, ১৬০ রান করেও অস্ট্রেলিয়া ৬ উইকেটে হেরেছে ভারতের কাছে।"
				+ "কিন্তু আসলে তো হারটা কোহলির কাছেই!কোহলির ব্যাটে ভর করে পাওয়া এই দুর্দান্ত জয়ে টি-টোয়েন্টি বিশ্বকাপের সেমিফাইনালেও পৌঁছে গেছে ভারত। শেষ চারে ৩১ মার্চ মুম্বাইয়ে ধোনির দলের প্রতিপক্ষ ওয়েস্ট ইন্ডিজ।";
		String phrase = "অধিনায়ক স্টিভেন";
		System.out.println(getPhraseFirstOccurrence(text, phrase));
	}
}
