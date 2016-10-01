package shafin.nlp.tokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;


public class NGramTokenizer extends Analyzer {

	private int minWords = 2;
	private int maxWords = 2;

	public NGramTokenizer(int minWords, int maxWords) {
		this.minWords = minWords;
		this.maxWords = maxWords;
	}

	/*
	 * fieldName - the name of the fields content passed to the
	 * Analyzer.TokenStreamComponents sink as a reader. e.g title, author,
	 * article
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {

		StandardTokenizer tokenizer = new StandardTokenizer();
		ShingleFilter sf = new ShingleFilter(tokenizer, minWords, maxWords);

		sf.setOutputUnigrams(true);// makes it false to no one word phrases out in the output.
		sf.setOutputUnigramsIfNoShingles(true);// if not enough for minimum, show anyway.

		return new TokenStreamComponents(tokenizer, sf);
	}

	
	public List<String> getNGramTokens(String text) throws IOException {
		
		List<String> nGramTokens = new ArrayList<>();
		
		TokenStream tokenStream = tokenStream("content", text);
		//OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			//int startOffset = offsetAttribute.startOffset();
			//int endOffset = offsetAttribute.endOffset();
			String term = charTermAttribute.toString();

			nGramTokens.add(term);
		}
		return nGramTokens;
	}

	public static void main(String[] args) throws IOException {
		String text = "তিতাস গ্যাসের ব্যবস্থাপনা পরিচালক (এমডি) নওশাদ ইসলামকে ওই পদ থেকে অব্যাহতি দেওয়া হয়েছে। আজ রোববার তাঁকে অব্যাহতি দেওয়া হয়। ";
		NGramTokenizer analyzer = new NGramTokenizer(2, 3);
		
		List<String> tokens = analyzer.getNGramTokens(text);
		for(String token: tokens){
			System.out.println(token);
		
		}
		
		analyzer.close();
	}

}
