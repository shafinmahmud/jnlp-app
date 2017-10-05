package net.shafin.nlp.tokenizer;

import net.shafin.common.util.RegexUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class SentenceSpliter {

    public static final String ENG_SPLIT_REGEX = "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s";
    public static final String BAN_SPLIT_REGEX = "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\?|৷|।|!)";

    public static final String PHRASE_BOUNDARY_REGEX = "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\(|\\)|,|;|\\{|\\}|\\[|\\])";

    public static String[] getSentenceTokenArrayBn(String text) {
        List<String> list = getSentenceTokenListBn(text);
        String[] array = {};
        return list.toArray(array);
    }

    public static LinkedList<String> getSentenceTokenListBn(String text) {
        LinkedList<String> list = RegexUtil.getSplittedTokens(text, BAN_SPLIT_REGEX);
        return list;
    }

    public static List<String> getPhraseTokenListFromSentence(String sentence) {
        List<String> list = RegexUtil.getSplittedTokens(sentence, PHRASE_BOUNDARY_REGEX);
        List<String> out = new ArrayList<>();
        for (String phrase : list) {
            out.add(phrase.replaceAll("[\\(|\\)|,|;|\\{|\\}|\\[|\\]]", ""));
        }
        return out;
    }

    public static void main(String[] args) {
        String text = "গুলশান হামলায় ডা. চীন ও বাংলাদেশ ভালো; প্রতিবেশী (ভালো বন্ধু), এবং{ভালো} অংশীদার।";
        System.out.println(text + "\n\n");
        System.out.println(getPhraseTokenListFromSentence(text));
    }
}
