package net.shafin.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class RegexUtil {

    public static String getFirstMatch(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return "";
        }
    }

    public static int countMatches(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    public static Set<String> findUniqueMatchedStrings(String input, String regex) {
        Set<String> allMatches = new HashSet<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            allMatches.add(matcher.group());
        }
        return allMatches;
    }

    public static boolean containsPattern(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    public static LinkedList<String> getSplittedTokens(String input, String splitRegex) {
        Pattern pattern = Pattern.compile(splitRegex);
        String[] tokens = pattern.split(input);

        LinkedList<String> tokenList = new LinkedList<>();
        for (String token : tokens) {
            tokenList.add(token);
        }
        return tokenList;
    }

    public static void main(String[] args) {
        String string = "বেশিরভাগ মুসলিম অধ্যুষিত দেশেই যৌনশিক্ষাকে ‘ট্যাবু' হিসেবে দেখা হয়৷ যেমন ধরুন মিশর৷ কিন্তু মিশরেরই এক ডাক্তার সেই ট্যাবু,";
        System.out.println(string);
        System.out.println(StringUtils.replacePattern(string, "\\p{InGeneral_Punctuation}", ""));

    }
}
