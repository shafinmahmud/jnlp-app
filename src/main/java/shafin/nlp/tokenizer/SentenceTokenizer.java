package shafin.nlp.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SentenceTokenizer {

	public static String[] UNICODE_SPACE_CHARACTERS = { "\u0020", "\u00A0", "\u180E", "\u1680", "\u2000", "\u2001",
			"\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2007", "\u2008", "\u2009", "\u200A", "\u200B",
			"\u202F", "\u205F", "\u3000", "\uFEFF" };

	public static String[] getSentenceTokenArrayBn(String text) {
		text = replaceAll(UNICODE_SPACE_CHARACTERS, " ", text);
		text = text.replaceAll("\\s+", " ");

		String pattern = "(?<!\\w\\।\\w।)(?<![A-Z][a-z]\\।)(?<=\\।|\\?)\\s";
		String[] sentences = text.split(pattern);
		return sentences;
	}

	public static List<String> getSentenceTokenListBn(String text) {
		text = replaceAll(UNICODE_SPACE_CHARACTERS, " ", text);
		text = text.replaceAll("\\s+", " ");
	
		/* https://regex101.com/r/nG1gU7/142 for the explanation of the pattern  */	
		String pattern = "(?<!\\w\\।\\w।)(?<![A-Z][a-z]\\।)(?<=।|\\?|।”|।’|\\?”|\\?’|।“|।‘|\\?“|\\?‘)\\s";
		String[] sentences = text.split(pattern);		
		return new ArrayList<String>(Arrays.asList(sentences));
	}

	public static String replaceAll(String[] shitStrings, String replaceWith, String text) {
		for (int i = 0; i < shitStrings.length; i++) {
			text = text.replaceAll(shitStrings[i], replaceWith);
		}
		return text;
	}

	public static void main(String[] args) {
		String text = " “আমি ইচ্ছে করলেই সব পারি না, কিছু রাজনৈতিক কারণও রয়েছে। নসিমন, করিমন, ইজিবাইক ছাড়াও স্থানীয় জনপ্রতিনিধিরাই বাধা হয়ে দাঁড়ায়।"
				+ "তারা প্রশাসনকে করতে দেয় না। এগুলোর সঙ্গে অনেকের স্বার্থ জড়িত।” মন্ত্রীর ‘কষ্ট’, তবে হতাশ নন যোগাযোগ খাতের অনেক উন্নয়ন করা হলেও তা নিয়ে প্রচার কম বলে অনুযোগ প্রকাশ করেন ওবায়দুল কাদের?"
				+ " “আসলে মাঝে মাঝে কিছু বিষয় খুব কষ্ট দেয়।  খারাপটাই বলে মানুষ, ভালোটা বলতে চায় না। আরিচায় যে এত বড় কাজ করলাম, এটা কেউ বলতে চায় না।"
				+ "ফ্লাইওভার হওয়ার কারণে আড়াই ঘণ্টার রাস্তা আড়াই মিনিটে যাওয়া যায়?” রাজধানীর সড়কের জন্য নাগরিকরা যোগাযোগমন্ত্রীকে দায়ী করলেও এক্ষেত্রে নিজের দায় নেই বলে দাবি করেন যোগাযোগমন্ত্রী।"
				+ "তিনি বলেন, দুই-তিনটা ফ্লাইওভার ছাড়া এ সিটির রাস্তা তার অধীনে নয়।";
		for (String string : getSentenceTokenListBn(text))
			System.out.println(string);
	}
}
