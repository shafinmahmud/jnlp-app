package shafin.nlp.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerbSuffixFilter {

	private static String[] VERB_SUFFIX_1 = { " ি", " ো", " ে", "ছ", "ল ", "ত", "ব" };
	private static String[] VERB_SUFFIX_2 = { " িস", "েন	", "ছি", "েছ", "লে", "লি", "তে", "তি", "বে", "বি" };
	private static String[] VERB_SUFFIX_3 = { "ছিস", "ছেন", "ছে", " েছি", "েছে", "লাম", "লেন", "ছিল", "তাম", "তেন",
			"বেন" };
	private static String[] VERB_SUFFIX_4 = { "েছিস", "েছেন", "েছেন", "ছিলে", "ছিলি", "েছিল" };
	private static String[] VERB_SUFFIX_5 = { " িতেছি ", "ছিলাম", "ছিলেন", "েছিলে", "েছিলি" };
	private static String[] VERB_SUFFIX_6 = { "েছিলাম", "েছিলেন" };

	public static void main(String[] args) {

		List<String> sample = new ArrayList<>();
		sample.add("ক");
		sample.add("চলো");
		sample.add("করেন");
		sample.add("করিস");
		sample.add("করে");
		sample.add("বই");
		sample.add("বলসি");

		for (String token : sample) {
			matchSuffix(token);

		}
	}

	public static String matchSuffix(String token) {

		String lastCharSubs = token.length() > 1 ? token.substring(token.length() - 1) : token.substring(0);
		String secondLastCharSubs = token.length() > 2 ? token.substring(token.length() - 2) : token.substring(0);
		String thirdLastCharSubs = token.length() > 3 ? token.substring(token.length() - 3) : token.substring(0);
		String fourthLastCharSubs = token.length() > 4 ? token.substring(token.length() - 4) : token.substring(0);
		String fifthLastCharSubs = token.length() > 5 ? token.substring(token.length() - 5) : token.substring(0);
		String sixthLastCharSubs = token.length() > 6 ? token.substring(token.length() - 6) : token.substring(0);

		if (Arrays.asList(VERB_SUFFIX_6).contains(sixthLastCharSubs)) {
			return "6 " + token;
		} else if (Arrays.asList(VERB_SUFFIX_5).contains(fifthLastCharSubs)) {
			return "5 " + token;
		} else if (Arrays.asList(VERB_SUFFIX_4).contains(fourthLastCharSubs)) {
			return "4 " + token;
		} else if (Arrays.asList(VERB_SUFFIX_3).contains(thirdLastCharSubs)) {
			return "3 " + token;
		} else if (Arrays.asList(VERB_SUFFIX_2).contains(secondLastCharSubs)) {
			return "2 " + token;
		} else if (Arrays.asList(VERB_SUFFIX_1).contains(lastCharSubs)) {
			return "1 " + token;
		} else {
			return "0 " + token;
		}
	}
}
