package shafin.nlp.tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Deprecated
public class WordTokenizer {

	public static String[] UNICODE_SPACE_CHARACTERS = { "\u0020", "\u00A0", "\u180E", "\u1680", "\u2000",
			"\u2001", "\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2007", "\u2008", "\u2009",
			"\u200A", "\u200B", "\u202F", "\u205F", "\u3000", "\uFEFF" };

	public static String[] getTokenizedBnStringArray(String text) {
		ArrayList<String> tokensList = new ArrayList<>();

		text = replaceAll(UNICODE_SPACE_CHARACTERS, "*", text);		
		text = text.replaceAll("\\s+", " ");
		StringTokenizer tokenizerBn = new StringTokenizer(text, " \t\n\r\f,।:;?![](){*}'‘");
		while (tokenizerBn.hasMoreTokens()) {
			String token = tokenizerBn.nextToken().trim();

			if (token.length() > 1) {
				tokensList.add(token);
			}
		}

		String[] tokens = new String[tokensList.size()];
		tokensList.toArray(tokens);
		return tokens;
	}

	public static List<String> getTokenizedBnList(String text) {
		ArrayList<String> tokensList = new ArrayList<>();
		
		text = replaceAll(UNICODE_SPACE_CHARACTERS, " ", text);
		text = text.replaceAll("\\s+", " ");
		StringTokenizer tokenizerBn = new StringTokenizer(text, " \t\n\r\f,।:;?![](){}'‘");
		while (tokenizerBn.hasMoreTokens()) {
			String token = tokenizerBn.nextToken().trim();
			if (token.length() > 1) {
				tokensList.add(token);
			}
		}

		return tokensList;
	}

	public static String[] getTokenizedEnStringArray(String text) {
		ArrayList<String> tokensList = new ArrayList<>();

		StringTokenizer tokenizerEn = new StringTokenizer(text, " \t\n\r\f,.:;?![](){}'");
		while (tokenizerEn.hasMoreTokens()) {
			tokensList.add(tokenizerEn.nextToken());
		}

		String[] tokens = new String[tokensList.size()];
		tokensList.toArray(tokens);
		return tokens;
	}

	public static List<String> getTokenizedEnList(String text) {
		ArrayList<String> tokensList = new ArrayList<>();

		StringTokenizer tokenizerEn = new StringTokenizer(text, " \t\n\r\f,.:;?![](){}'");
		while (tokenizerEn.hasMoreTokens()) {
			tokensList.add(tokenizerEn.nextToken());
		}

		return tokensList;
	}

	public static String replaceAll(String[] shitStrings, String replaceWith, String text){
		for (int i = 0; i < shitStrings.length; i++) {
			text = text.replaceAll(shitStrings[i], replaceWith);
		}
		return text;
	}
	
	public static void main(String[] args) {
		String sentence = "দুধ (Milk)  স্তন্যপায়ী প্রাণীদের প্রসবত্তোর নবজাতকের প্রাথমিক খাদ্য হিসেবে মায়ের স্তনগ্রন্থি "
				+ "থেকে নিঃসৃত দ্রব্য। শিশুর বৃদ্ধি ও স্বাস্থ্যের জন্য দুধ অপরিহার্য। দুধ পুষ্টিকর খাদ্যসমূহের অন্যতম। পুষ্টিগুণের বিবেচনায় এটি আদর্শ খাদ্য।"
				+ "প্রোটিন, কার্বোহাইড্রেট, স্নেহপদার্থ, ভিটামিন, অজৈব লবণ (ক্যালসিয়াম, ফসফরাস ও ক্লোরিন) ও"
				+ "পানি খাদ্যের সকল উপাদানই দুধে বিদ্যমান। দুধের গড় উপাদানে আছে ৮৭.৬% পানি, ৩.৭% চর্বি, ৩.২% প্রোটিন, "
				+ "৪.২% ল্যাকটোজ ও ০.৭২% খনিজ, বাকি সামান্য ভিটামিন। একশ মিলিলিটার দুধ থেকে ৬৫.৩ ক্যালরি শক্তি পাওয়া যায়। সমস্ত গ্রামের লোকদের বিভিন্ন দলে বিভক্ত করা হয়। "
				+ "দলের সদস্যরা সমান বা কাছাকাছি বয়সের হয়ে থাকে। প্রত্যেক দলের নিজস্ব ভূমিকা রয়েছে। চাষ, শিকার, উৎসব, ভোজ, বাড়ি নির্মাণ, "
				+ "আত্মরক্ষা প্রভৃতি কাজ এক একটি দলের ওপর ন্যস্ত থাকে।[শাহীদা আখতার]";
		
		String[] tokens = getTokenizedBnStringArray(sentence);
		for (int i = 0; i < tokens.length; i++) {
			System.out.println(tokens[i]);
		}
	}
}
