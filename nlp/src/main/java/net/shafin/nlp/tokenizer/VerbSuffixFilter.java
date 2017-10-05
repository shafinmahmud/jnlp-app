package net.shafin.nlp.tokenizer;

import net.shafin.nlp.analyzer.BanglaWordAnalyzer;
import net.shafin.nlp.db.IndexService;
import net.shafin.nlp.pos.PosTagger;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class VerbSuffixFilter {

    private final static String[] VERB_SUFFIX_1 = {" ি", " ো", " ে", "ছ", "ল ", "ত", "ব"};
    private final static String[] VERB_SUFFIX_2 = {" িস", "েন	", "ছি", "েছ", "লে", "লি", "তে", "তি", "বে", "বি"};
    private final static String[] VERB_SUFFIX_3 = {"ছিস", "ছেন", "ছে", " েছি", "েছে", "লাম", "লেন", "ছিল", "তাম", "তেন",
            "বেন"};
    private final static String[] VERB_SUFFIX_4 = {"েছিস", "েছেন", "েছেন", "ছিলে", "ছিলি", "েছিল"};
    private final static String[] VERB_SUFFIX_5 = {" িতেছি ", "ছিলাম", "ছিলেন", "েছিলে", "েছিলি"};
    private final static String[] VERB_SUFFIX_6 = {"েছিলাম", "েছিলেন"};

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


    public boolean isVerbToken(String token) {
        this.posTagger.setTEXT(token);
        List<String> verbs = posTagger.findVerbTaggedTokens();
        return !verbs.isEmpty();
    }


    public boolean doesStartOrEndsWithVerbSuffix(String token) {
        BanglaWordAnalyzer wordAnalyzer = new BanglaWordAnalyzer(new StringReader(token));
        List<String> wordTokens = wordAnalyzer.getTokenList();
        wordAnalyzer.close();

        int size = wordTokens.size();
        if (size > 0) {
            String firstWord = wordTokens.get(0);
            String lastWord = wordTokens.get(size - 1);

            if (isVerbToken(firstWord) && containsVerbSuffix(firstWord)) {
                return true;
            }

            if (isVerbToken(lastWord) && containsVerbSuffix(lastWord)) {
                return true;
            }

            return false;
        }

        return true;
    }

    public static boolean doesStartOrEndsWithVerbSuffix(String token, IndexService indexService) {
        BanglaWordAnalyzer wordAnalyzer = new BanglaWordAnalyzer(new StringReader(token));
        List<String> wordTokens = wordAnalyzer.getTokenList();
        wordAnalyzer.close();

        int size = wordTokens.size();
        if (size > 0) {
            String firstWord = wordTokens.get(0);
            String lastWord = wordTokens.get(size - 1);

            if (indexService.isExistVerb(firstWord) && containsVerbSuffix(firstWord)) {
                return true;
            }

            if (indexService.isExistVerb(lastWord) && containsVerbSuffix(lastWord)) {
                return true;
            }

            return false;
        }

        return true;
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {
        IndexService service = new IndexService();
        String token = " এমনটাই দাবি করেছেন";
        System.out.println(token + " : " + VerbSuffixFilter.doesStartOrEndsWithVerbSuffix(token, service));
    }
}
