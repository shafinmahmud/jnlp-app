package shafin.nlp.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/*
 * Version 1.1
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
		String string = "বেশিরভাগ মুসলিম অধ্যুষিত দেশেই যৌনশিক্ষাকে ‘ট্যাবু' হিসেবে দেখা হয়৷ যেমন ধরুন মিশর৷ কিন্তু মিশরেরই এক ডাক্তার সেই ট্যাবু,হিসেবে হিসেবে হিসেবে যৌনশিক্ষা নিয়ে রক্ষণশীলতা দূর করতে চান৷ আর এর জন্য ইউটিউবকে বেছে নিয়েছেন তিনি৷ মিশরীয় সমাজ অনেকটা বাংলাদেশ বা পাকিস্তানের মতোই রক্ষণশীল৷ সে কারণেই হয়ত যৌনশিক্ষার গুরুত্বও এই দেশগুলোতে অনেক বেশি৷ ডা. আলিয়া গাদের কথায়, ‘‘ইন্টারনেট মিশরে খুব জনপ্রিয়৷ তবে অভিভাবকদের মধ্যে ইন্টারনেট নিয়ে, বিশেষ করে শিশু বা কিশোর-কিশোরীদের হাতে ইন্টারনেটকে সহজলভ্য করে তোলায় ভয় আছে৷ অথচ এ মাধ্যমটির সঠিক ব্যবহার কিন্তু উপকারে আসতে পারে৷'' ডা. গাদ নিজেই এগিয়ে এসেছেন৷ ইউটিউবে নিজের একটি চ্যানেল তৈরি করে সেখানে যৌনতা, বয়ঃসন্ধি, স্বাস্থ্য বিষয়ক নানা প্রশ্নের খোলামেলা উত্তর দিচ্ছেন তিনি৷ যৌনশিক্ষা নিয়ে আপনার কোনো কিছু জানার থাকলে ভিডিওটি দেখুন এবং সরাসরি প্রশ্ন করুন ডা. আলিয়া গাদকে৷ বন্ধু, আমাদের দেশেও কি এমন একটা অভিনব উদ্যোগের কথা আপনি কল্পনা করতে পারেন? লিখুন নীচের ঘরে৷ ডিজি/এসিবি";
		System.out.println(getSplittedTokens(string, "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\?|৷|।)\\s"));
		System.out.println(StringUtils.countMatches(string, "হিসেবে"));
	}
}
