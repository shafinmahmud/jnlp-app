package shafin.nlp.tokenizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import shafin.nlp.util.FileHandler;


public class DocumentParser {

	private Set<String> MALICIOUS_TOKEN;
	
	private Set<String> stopWords;
	private List<String> tokens;

	private List<String> tokensExcludingStopwords;

	public DocumentParser(String text) {
		
		this.tokens = Tokenizer.getTokenizedBnList(text);
		this.stopWords = new HashSet<>(FileHandler.readFile("D:\\DOCUMENT\\PROJECTS\\SPRING\\ml-tfidf\\stopwords.txt"));
		this.MALICIOUS_TOKEN = new HashSet<>(FileHandler.readFile("D:\\DOCUMENT\\PROJECTS\\SPRING\\ml-tfidf\\maltoken.txt"));
	}

	public List<String> getTokensExcludingStopwordsList() {
		this.tokensExcludingStopwords = tokens;
		this.tokensExcludingStopwords.removeAll(stopWords);
		this.tokensExcludingStopwords.removeAll(MALICIOUS_TOKEN);
		return tokensExcludingStopwords;
	}

	public String[] getTokensExcludingStopwordsArray() {
		this.tokensExcludingStopwords = tokens;
		this.tokensExcludingStopwords.removeAll(stopWords);
		this.tokensExcludingStopwords.removeAll(MALICIOUS_TOKEN);
		return tokensExcludingStopwords.toArray(new String[tokensExcludingStopwords.size()]);
	}

	public static void main(String[] args) {
		String text = "৪ ফেব্রুয়ারি, |  + দু’জন  বৃহস্পতিবার,‘হামদর্দ  দিবাগত রাত প্রায় সাড়ে ১২টায় বাংলাদেশ ব্যাংকের সুইফটের বার্তা বা সংকেত ব্যবহার করে "
				+ "৩৫টি অর্থ স্থানান্তরের পরামর্শ বা অ্যাডভাইস পাঠানো হয়েছিল যুক্তরাষ্ট্রের ফেডারেল রিজার্ভ ব্যাংক অব নিউইয়র্কে।";
		System.out.println(text);
		DocumentParser parser = new DocumentParser(text);
		for (String string : parser.getTokensExcludingStopwordsList()) {
			System.out.println(string);
		}
	}
}
