package net.shafin.nlp.analyzer;

import net.shafin.nlp.tokenizer.BanglaWordTokenizer;
import net.shafin.nlp.tokenizer.NoneWordTokenFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
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
        ShingleFilter shingleFilter = new ShingleFilter(tokenizer, minWords, maxWords);
        // makes it false to no one word phrases outin the output.
        shingleFilter.setOutputUnigrams(true);
        // if not enough for minimum, show anyway.
        shingleFilter.setOutputUnigramsIfNoShingles(true);

        TokenStream noneAlphabetFilter = new NoneWordTokenFilter(shingleFilter);
        return new TokenStreamComponents(tokenizer, noneAlphabetFilter);
    }

    public List<String> getNGramTokens() throws IOException {
        List<String> nGramTokens = new ArrayList<>();

        TokenStream tokenStream = tokenStream("content", reader);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            String term = charTermAttribute.toString();
            nGramTokens.add(term.trim());
        }
        return nGramTokens;
    }

    public static void main(String[] args) throws IOException {
        String text = "আর ‘হাউপ্টশুলে'-র ছাত্র-ছাত্রীরা ন'বছর পরেই স্কুলের পাঠ শেষ করে ‘মিটলারে রাইফে'-র সার্টিফিকেট পকেটে নিয়ে পেশাগত প্রশিক্ষণের দিকে চলে যায়৷";
        NGramAnalyzer analyzer = new NGramAnalyzer(new StringReader(text), 2, 3);

        System.out.println(text);
        List<String> tokens = analyzer.getNGramTokens();
        for (String token : tokens) {
            System.out.println(token);

        }

        analyzer.close();
    }

}
