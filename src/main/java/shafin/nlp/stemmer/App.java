package shafin.nlp.stemmer;

import java.util.List;

import shafin.nlp.util.FileHandler;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		BnStemmerLight stemmerLight = new BnStemmerLight();
		List<String> words = FileHandler
				.readFile("D:\\DOCUMENT\\GoogleDrive\\NLP\\KEYWORD\\PROJECT_WORK\\nlp\\words.txt");
		List<String> stopWords = FileHandler
				.readFile("D:\\DOCUMENT\\GoogleDrive\\NLP\\KEYWORD\\PROJECT_WORK\\nlp\\stopword.txt");

		for (String s : words) {
			String given = s.split("\\s")[0];
			if (!stopWords.contains(given)) {
				String root = stemmerLight.stem(given);
				FileHandler.appendFile("D:\\DOCUMENT\\GoogleDrive\\NLP\\KEYWORD\\PROJECT_WORK\\nlp\\root_output.txt",
						given + "  :  " + root + "\n");
				System.out.println(given + "... ");
			} else {
				System.out.println("stopword: " + given);
			}

		}
	}
}
