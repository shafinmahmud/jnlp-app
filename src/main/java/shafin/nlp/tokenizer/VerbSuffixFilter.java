package shafin.nlp.tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import shafin.nlp.analyzer.BanglaWordAnalyzer;
import shafin.nlp.pos.PosTagger;

public class VerbSuffixFilter {

	private final static String[] VERB_SUFFIX_1 = { " ি", " ো", " ে", "ছ", "ল ", "ত", "ব" };
	private final static String[] VERB_SUFFIX_2 = { " িস", "েন	", "ছি", "েছ", "লে", "লি", "তে", "তি", "বে", "বি" };
	private final static String[] VERB_SUFFIX_3 = { "ছিস", "ছেন", "ছে", " েছি", "েছে", "লাম", "লেন", "ছিল", "তাম", "তেন",
													"বেন" };
	private final static String[] VERB_SUFFIX_4 = { "েছিস", "েছেন", "েছেন", "ছিলে", "ছিলি", "েছিল" };
	private final static String[] VERB_SUFFIX_5 = { " িতেছি ", "ছিলাম", "ছিলেন", "েছিলে", "েছিলি" };
	private final static String[] VERB_SUFFIX_6 = { "েছিলাম", "েছিলেন" };

	private final PosTagger posTagger;
	
	public VerbSuffixFilter() throws ClassNotFoundException, IOException {
		this.posTagger = new PosTagger();
	}

	private static boolean containsVerbSuffix(String token) {

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

	@Deprecated
	public  List<String> getSuffixedVerbTokens(String text) throws ClassNotFoundException, IOException {
		List<String> suffixedVerbsToken = new ArrayList<>();
		this.posTagger.setTEXT(text);

		List<String> verbTokens = posTagger.findVerbTaggedTokens();
		for (String verb : verbTokens) {
			if (containsVerbSuffix(verb)) {
				suffixedVerbsToken.add(verb);
			}
		}
		return suffixedVerbsToken;
	}
	
	public boolean isVerbToken(String token){
		this.posTagger.setTEXT(token);
		List<String> verbs = posTagger.findVerbTaggedTokens();
		return !verbs.isEmpty();
	}
	
	@Deprecated
	public List<String> filterVerbSuffixCandidates(String text, List<String> candidates)
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
	
	public boolean doesStartOrEndsWithVerbSuffix(String token){
		BanglaWordAnalyzer wordAnalyzer = new BanglaWordAnalyzer(new StringReader(token));
		List<String> wordTokens = wordAnalyzer.getTokenList();
		wordAnalyzer.close();

		int size = wordTokens.size();
		if (size > 0) {
			String firstWord = wordTokens.get(0);
			String lastWord = wordTokens.get(size - 1);
			
			if(isVerbToken(firstWord) && containsVerbSuffix(firstWord)){
				return true;
			}
			
			if(isVerbToken(lastWord) && containsVerbSuffix(lastWord)){
				return true;
			}
			
			return false;
		}

		return true;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		VerbSuffixFilter filter = new VerbSuffixFilter();
		String token  = " এমনটাই দাবি করেছেন";
		System.out.println(token+" : "+filter.doesStartOrEndsWithVerbSuffix(token));
	}
}
