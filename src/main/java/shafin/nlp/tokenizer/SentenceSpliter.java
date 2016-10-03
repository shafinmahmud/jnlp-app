package shafin.nlp.tokenizer;

import java.util.List;

import shafin.nlp.util.RegexUtil;

public class SentenceSpliter {

	public static String[] UNICODE_SPACE_CHARACTERS = { "\u0020", "\u00A0", "\u180E", "\u1680", "\u2000", "\u2001",
			"\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2007", "\u2008", "\u2009", "\u200A", "\u200B",
			"\u202F", "\u205F", "\u3000", "\uFEFF" };

	public static final String ENG_SPLIT_REGEX = "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s";
	public static final String BAN_SPLIT_REGEX = "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\?|৷|।)\\s";

	public static String[] getSentenceTokenArrayBn(String text) {
		List<String> list = getSentenceTokenListBn(text);
		String[] array = {};
		return list.toArray(array);
	}

	public static List<String> getSentenceTokenListBn(String text) {
		text = replaceAll(UNICODE_SPACE_CHARACTERS, " ", text);
		text = text.replaceAll("\\s+", " ");
		List<String> list = RegexUtil.getSplittedTokens(text, BAN_SPLIT_REGEX);
		return list;
	}

	public static String replaceAll(String[] shitStrings, String replaceWith, String text) {
		for (int i = 0; i < shitStrings.length; i++) {
			text = text.replaceAll(shitStrings[i], replaceWith);
		}
		return text;
	}

	public static void main(String[] args) {
		String text = "বেশিরভাগ মুসলিম অধ্যুষিত দেশেই যৌনশিক্ষাকে ‘ট্যাবু' হিসেবে দেখা হয়৷ যেমন ধরুন মিশর৷ কিন্তু মিশরেরই এক ডাক্তার সেই ট্যাবু, যৌনশিক্ষা নিয়ে রক্ষণশীলতা দূর করতে চান৷ আর এর জন্য ইউটিউবকে বেছে নিয়েছেন তিনি৷ মিশরীয় সমাজ অনেকটা বাংলাদেশ বা পাকিস্তানের মতোই রক্ষণশীল৷ সে কারণেই হয়ত যৌনশিক্ষার গুরুত্বও এই দেশগুলোতে অনেক বেশি৷ ডা. আলিয়া গাদের কথায়, ‘‘ইন্টারনেট মিশরে খুব জনপ্রিয়৷ তবে অভিভাবকদের মধ্যে ইন্টারনেট নিয়ে, বিশেষ করে শিশু বা কিশোর-কিশোরীদের হাতে ইন্টারনেটকে সহজলভ্য করে তোলায় ভয় আছে৷ অথচ এ মাধ্যমটির সঠিক ব্যবহার কিন্তু উপকারে আসতে পারে৷'' ডা. গাদ নিজেই এগিয়ে এসেছেন৷ ইউটিউবে নিজের একটি চ্যানেল তৈরি করে সেখানে যৌনতা, বয়ঃসন্ধি, স্বাস্থ্য বিষয়ক নানা প্রশ্নের খোলামেলা উত্তর দিচ্ছেন তিনি৷ যৌনশিক্ষা নিয়ে আপনার কোনো কিছু জানার থাকলে ভিডিওটি দেখুন এবং সরাসরি প্রশ্ন করুন ডা. আলিয়া গাদকে৷ বন্ধু, আমাদের দেশেও কি এমন একটা অভিনব উদ্যোগের কথা আপনি কল্পনা করতে পারেন? লিখুন নীচের ঘরে৷ ডিজি/এসিবি";
		for (String string : getSentenceTokenListBn(text))
			System.out.println(string);
	}
}
