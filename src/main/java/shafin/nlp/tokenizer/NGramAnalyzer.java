package shafin.nlp.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/*
 * Author : Shafin Mahmud
 * Email  : shafin.mahmud@gmail.com
 * Date	  : 02-10-2016 SUN
 */
public class NGramAnalyzer extends Analyzer {

	private final Reader reader;

	private final int minWords;
	private final int maxWords;

	public NGramAnalyzer(final Reader r, int minWords, int maxWords) {
		if (minWords > maxWords)
			throw new IllegalArgumentException("MaxWords cant be Smaller That MinWords!");

		this.reader = r;
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
		Tokenizer tokenizer = new BanglaWordTokenizer(reader);

		/*
		 * A ShingleFilter constructs shingles (token n-grams) from a token
		 * stream. In other words, it creates combinations of tokens as a single
		 * token. For example, the sentence
		 * "please divide this sentence into shingles" might be tokenized into
		 * shingles "please divide", "divide this", "this sentence",
		 * "sentence into", and "into shingles".
		 */
		ShingleFilter sf = new ShingleFilter(tokenizer, minWords, maxWords);

		// makes it false to no one word phrases outin the output.
		sf.setOutputUnigrams(true);
		// if not enough for minimum, show anyway.
		sf.setOutputUnigramsIfNoShingles(true);

		return new TokenStreamComponents(tokenizer, sf);
	}

	public List<String> getNGramTokens(String text) throws IOException {

		List<String> nGramTokens = new ArrayList<>();

		TokenStream tokenStream = tokenStream("content", reader);
		// OffsetAttribute offsetAttribute =
		// tokenStream.addAttribute(OffsetAttribute.class);
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream.reset();
		while (tokenStream.incrementToken()) {
			// int startOffset = offsetAttribute.startOffset();
			// int endOffset = offsetAttribute.endOffset();
			String term = charTermAttribute.toString();

			nGramTokens.add(term);
		}
		return nGramTokens;
	}

	public static void main(String[] args) throws IOException {
		String text = "তিতাস গ্যাসের ব্যবস্থাপনা পরিচালক (এমডি) নওশাদ ইসলামকে ওই পদ থেকে অব্যাহতি দেওয়া হয়েছে। আজ রোববার তাঁকে অব্যাহতি দেওয়া হয়। ";
		NGramAnalyzer analyzer = new NGramAnalyzer(new StringReader(text), 2, 3);

		System.out.println(text);
		List<String> tokens = analyzer.getNGramTokens(text);
		for (String token : tokens) {
			System.out.println(token);

		}

		analyzer.close();
	}

}
