package shafin.nlp.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import shafin.nlp.tokenizer.BanglaWordTokenizer;
import shafin.nlp.tokenizer.NoneWordTokenFilter;

/*
 * Author : Shafin Mahmud
 * Email  : shafin.mahmud@gmail.com
 * Date	  : 02-10-2016 SUN
 */
public class BanglaWordAnalyzer extends Analyzer {

	private final Reader reader;

	public BanglaWordAnalyzer(Reader r) {
		this.reader = r;
	}

	/*
	 * This is the only function that we need to override for our analyzer. It
	 * takes in a java.io.Reader object and saves the tokenizer and list of
	 * token filters that operate on it.
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer tokenizer = new BanglaWordTokenizer(reader);
		TokenStream filter = new NoneWordTokenFilter(tokenizer);

		TokenStream noneAlphabetFilter = new NoneWordTokenFilter(filter);
		return new TokenStreamComponents(tokenizer, noneAlphabetFilter);
	}

	public List<String> getTokenList() {
		List<String> result = new ArrayList<>();
		try {
			TokenStream stream = tokenStream(null, reader);
			stream.reset();
			while (stream.incrementToken()) {
				result.add(stream.getAttribute(CharTermAttribute.class).toString());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static void main(String[] args) {
		String text = "অবশ্য জাতীয় পরিচয় নিবন্ধন আইন-২০১০-এ বলা আছে, "
				+ "যেমন: আঙুলের ছাপ,স্বাক্ষর এবং কণ্ঠস্বর সংগ্রহ ও সংরক্ষণ করতে হবে। ";
		Reader reader = new StringReader(text);

		BanglaWordAnalyzer analyzer = new BanglaWordAnalyzer(reader);
		List<String> ss = analyzer.getTokenList();
		System.out.println(text);
		System.out.print(ss + " \n");
		analyzer.close();
	}
}
