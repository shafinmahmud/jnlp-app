package shafin.nlp.tokenizer;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import shafin.nlp.util.RegexUtil;

public class SentenceSpliter {

	public static String[] UNICODE_SPACE_CHARACTERS = { "\u0020", "\u00A0", "\u180E", "\u1680", "\u2000", "\u2001",
			"\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2007", "\u2008", "\u2009", "\u200A", "\u200B",
			"\u202F", "\u205F", "\u3000", "\uFEFF" };

	public static final String ENG_SPLIT_REGEX = "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s";
	public static final String BAN_SPLIT_REGEX = "(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\?|৷|।|!)";

	public static String[] getSentenceTokenArrayBn(String text) {
		List<String> list = getSentenceTokenListBn(text);
		String[] array = {};
		return list.toArray(array);
	}

	public static LinkedList<String> getSentenceTokenListBn(String text) {
		text = replaceAll(UNICODE_SPACE_CHARACTERS, " ", text);
		text = StringUtils.normalizeSpace(text);
		LinkedList<String> list = RegexUtil.getSplittedTokens(text, BAN_SPLIT_REGEX);
		return list;
	}

	public static String replaceAll(String[] shitStrings, String replaceWith, String text) {
		StringBuffer sb = new StringBuffer(text);
		for (int i = 0; i < shitStrings.length; i++) {
			sb = new StringBuffer(sb.toString().replaceAll(shitStrings[i], replaceWith));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String text = "আইএস তাদের সদস্যদের ‘খিলাফতের সৈনিক’   বলে সম্বোধন করে!গুলশান হামলায় ডা. জড়িত ও পরে অভিযানে নিহত পাঁচ জঙ্গিকে তারা একই সম্বোধন করে এবং হামলার দায় স্বীকার করে। যদিও বাংলাদেশের আইনশৃঙ্খলা রক্ষাকারী বাহিনীর কর্মকর্তারা বলেছেন, গুলশান হামলায় আইএস নয়, নব্য জেএমবি জড়িত। তামিম চৌধুরী এই নব্য জেএমবির নেতা এবং ১ জুলাই গুলশানের হলি আর্টিজানে হামলার অন্যতম সমন্বয়ক ও পরিকল্পনাকারী। গত ২৭ আগস্ট নারায়ণগঞ্জে জঙ্গিবিরোধী পুলিশের এক অভিযানে তামিম ও তাঁর দুই সহযোগী নিহত হন।";
		for (String string : getSentenceTokenListBn(text))
			System.out.println(string);
	}
}
