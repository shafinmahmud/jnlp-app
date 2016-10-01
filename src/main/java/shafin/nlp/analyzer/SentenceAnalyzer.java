package shafin.nlp.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import shafin.nlp.corpus.model.Document;
import shafin.nlp.tokenizer.SentenceTokenizer;
import shafin.nlp.util.FileHandler;
import shafin.nlp.util.JsonProcessor;

public class SentenceAnalyzer {

	private String CORPUS_LOC = "D:/home/corpus/test/sample/";

	public SentenceAnalyzer(String corpusPath) {
		this.CORPUS_LOC = corpusPath;
	}

	public double getSentenceFrequencyPerDocument() throws IOException {

		int numberOfDocument = 0;
		int numberOfSenctence = 0;

		Iterator<String> fileIterator = FileHandler.getRecursiveFileList(CORPUS_LOC).iterator();

		while (fileIterator.hasNext()) {
			File jsonFile = new File(fileIterator.next());

			if (jsonFile.getName().endsWith(".json")) {

				JsonProcessor processor = new JsonProcessor(jsonFile);
				Document doc = (Document) processor.convertToModel(Document.class);

				String article = doc.getArticle();
				List<String> sentences = SentenceTokenizer.getSentenceTokenListBn(article);

				numberOfDocument++;
				numberOfSenctence += sentences.size();
				System.out.println(sentences.size() + " " + jsonFile.getName());
			}

		}

		return numberOfSenctence / numberOfDocument;
	}

	public static void main(String[] args) throws IOException {

		SentenceAnalyzer analyzer = new SentenceAnalyzer("D:/home/corpus/test/sample/");
		System.out.println(analyzer.getSentenceFrequencyPerDocument());
	}
}
