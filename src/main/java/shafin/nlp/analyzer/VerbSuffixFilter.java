package shafin.nlp.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shafin.nlp.pos.PosTagger;

public class VerbSuffixFilter {

	private final static String[] VERB_SUFFIX_1 = { " ি", " ো", " ে", "ছ", "ল ", "ত", "ব" };
	private final static String[] VERB_SUFFIX_2 = { " িস", "েন	", "ছি", "েছ", "লে", "লি", "তে", "তি", "বে", "বি" };
	private final static String[] VERB_SUFFIX_3 = { "ছিস", "ছেন", "ছে", " েছি", "েছে", "লাম", "লেন", "ছিল", "তাম", "তেন",
													"বেন" };
	private final static String[] VERB_SUFFIX_4 = { "েছিস", "েছেন", "েছেন", "ছিলে", "ছিলি", "েছিল" };
	private final static String[] VERB_SUFFIX_5 = { " িতেছি ", "ছিলাম", "ছিলেন", "েছিলে", "েছিলি" };
	private final static String[] VERB_SUFFIX_6 = { "েছিলাম", "েছিলেন" };

	public static List<String> getSuffixedVerbTokens(String text) throws ClassNotFoundException, IOException {
		List<String> suffixedVerbsToken = new ArrayList<>();
		PosTagger posTagger = new PosTagger(text);
		List<String> verbTokens = posTagger.findVerbTaggedTokens();
		for (String verb : verbTokens) {
			if (matchSuffix(verb)) {
				suffixedVerbsToken.add(verb);
			}
		}
		return suffixedVerbsToken;
	}

	public static boolean matchSuffix(String token) {

		String lastCharSubs = token.length() > 1 ? token.substring(token.length() - 1) : token.substring(0);
		String secondLastCharSubs = token.length() > 2 ? token.substring(token.length() - 2) : token.substring(0);
		String thirdLastCharSubs = token.length() > 3 ? token.substring(token.length() - 3) : token.substring(0);
		String fourthLastCharSubs = token.length() > 4 ? token.substring(token.length() - 4) : token.substring(0);
		String fifthLastCharSubs = token.length() > 5 ? token.substring(token.length() - 5) : token.substring(0);
		String sixthLastCharSubs = token.length() > 6 ? token.substring(token.length() - 6) : token.substring(0);

		if (stringContainsItemFromList(sixthLastCharSubs, VERB_SUFFIX_6)
				|| stringContainsItemFromList(fifthLastCharSubs, VERB_SUFFIX_5)
				|| stringContainsItemFromList(fourthLastCharSubs, VERB_SUFFIX_4)
				|| stringContainsItemFromList(thirdLastCharSubs, VERB_SUFFIX_3)
				|| stringContainsItemFromList(secondLastCharSubs, VERB_SUFFIX_2)
				|| stringContainsItemFromList(lastCharSubs, VERB_SUFFIX_1)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean stringContainsItemFromList(String inputString, String[] items) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].contains(inputString)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> filterVerbSuffixCandidates(String text, List<String> candidates)
			throws ClassNotFoundException, IOException {
		List<String> filteredList = new ArrayList<>();
		List<String> itchyVerbs = getSuffixedVerbTokens(text);
		for (String candidate : candidates) {
			for (String itchy : itchyVerbs) {
				if (!candidate.startsWith(itchy) && !candidate.endsWith(itchy)) {
					filteredList.add(candidate);
				}
			}
		}
		return filteredList;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		String text = "ভারতীয় টেলিভিশনের জনপ্রিয় ধারাবাহিক ‘বালিকা বধূ’র পরিচিত মুখ বাঙালি অভিনেত্রী প্রত্যুষা বন্দ্যোপাধ্যায়ের মৃত্যুর "
				+ " সঙ্গে নিজের সম্পৃক্ততার কথা অস্বীকার করেছেন তাঁর প্রেমিক অভিনেতা-প্রযোজক রাহুল রাজ সিং। তিনি বলেন, প্রত্যুষাকে বিয়ের জন্য প্রস্তুতিও নিচ্ছিলেন।"
				+ "আজ রোববার এনডিটিভি অনলাইনের এক প্রতিবেদনে বলা হয়েছে, প্রাথমিক জিজ্ঞাসাবাদে পুলিশের কাছে এমনটাই দাবি করেছেন প্রত্যুষার প্রেমিক রাহুল রাজ।";
		List<String> sample = getSuffixedVerbTokens(text);

		System.out.println(text);
		for (String token : sample) {
			System.out.println(token);
		}
	}
}
