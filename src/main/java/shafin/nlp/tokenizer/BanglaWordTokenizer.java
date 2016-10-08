package shafin.nlp.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/*
 * Author: Shafin Mahmud
 * Email : shafin.mahmnud@gmail.com
 * 
 */
public class BanglaWordTokenizer extends Tokenizer {

	/*
	 * Some times UTF-8 encoded Text contains spaces that are not in general we
	 * use. They are some special kind. Below is the list of all UNICODE space
	 * charactes that we need to remove from the text for usual text processing.
	 */
	private static String[] UNICODE_SPACE_CHARACTERS = { "\u0020", "\u00A0", "\u180E", "\u1680", "\u2000", "\u2001",
			"\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2007", "\u2008", "\u2009", "\u200A", "\u200B",
			"\u202F", "\u205F", "\u3000", "\uFEFF" };

	/*
	 * This is the Regex that is used for select the SPLIT_TOKEN (tokens for
	 * those occurence we want to break the text into pieces
	 */
	private static final String SPLIT_REGEX = "([\\s\\t\\n\\r\\f,;\\/\\?\\!\\[\\]\\(\\)\\{\\}।—৷\\+]+"
												+ "|:[\\s]+|:[\\s]*-[\\s]*" + "|[\\.]{2,})";

	/*
	 * Lucene uses attributes to store information about a single token. For
	 * this tokenizer, the only attribute that we are going to use is the
	 * CharTermAttribute, which can store the text for the token that is
	 * generated. Other types of attributes exist (see interfaces and classes
	 * derived from org.apache.lucene.util.Attribute); we can use some of these
	 * other attributes when we build our custom token filter. It is important
	 * that you register attributes, whatever their type, using the
	 * addAttribute() function.
	 */
	protected CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);

	/*
	 * This object stores the string that we are turning into tokens. We will
	 * process its content as we call the incrementToken() function.
	 */
	protected String stringToTokenize;

	/*
	 * This stores the current position in this.stringToTokenize. We will
	 * increment its value as we call the incrementToken() function.
	 */
	protected int position = 0;

	/*
	 * This is the constructor for our custom tokenizer class. It takes all
	 * information from a java.io.Reader object and stores it in a StringBuffer.
	 * We are expecting very large blocks of text and want to saving chunks from
	 * the reader whenever incrementToken() is called. This function throws a
	 * RuntimeException when an IOException is raised - you can choose how you
	 * want to deal with the IOException, but for our purposes, we do not need
	 * to try to recover from it.
	 */
	public BanglaWordTokenizer(Reader reader) {
		super();
		int numChars;
		char[] buffer = new char[1024];
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ((numChars = reader.read(buffer, 0, buffer.length)) != -1) {
				stringBuilder.append(buffer, 0, numChars);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.stringToTokenize = removeUnicodeSpaceChars(stringBuilder);
	}

	/*
	 * In preprocessing we removes the UNICODE_SPACE_CHARACRTES from Text
	 */
	private String removeUnicodeSpaceChars(StringBuilder sb) {
		for (int i = 0; i < UNICODE_SPACE_CHARACTERS.length; i++) {
			sb = new StringBuilder(sb.toString().replaceAll(UNICODE_SPACE_CHARACTERS[i], " "));
		}
		return sb.toString();
	}

	/*
	 * This is the important function to override from the Tokenizer class. At
	 * each call, it should set the value of this.charTermAttribute to the text
	 * of the next token. It returns true if a new token is generated and false
	 * if there are no more tokens remaining.
	 */
	@Override
	public boolean incrementToken() throws IOException {
		// Clear anything that is already saved in this.charTermAttribute
		this.charTermAttribute.setEmpty();

		// Get the position of the next SPLIT_TOKEN
		String nextSplitingToken = getNextSplitingToken(this.stringToTokenize.substring(this.position));

		int nextIndex = -1;

		if (nextSplitingToken != null) {
			nextIndex = this.stringToTokenize.indexOf(nextSplitingToken, this.position);
		}

		// Execute this block if a SPLIT_TOKEN was found. Save the word token
		// and the position to start at when incrementToken() is next
		// called.
		if (nextIndex != -1) {
			String nextToken = this.stringToTokenize.substring(this.position, nextIndex);
			this.charTermAttribute.append(nextToken);
			this.position = nextIndex + nextSplitingToken.length();
			return true;
		}

		// Execute this block if no more SPLIT_TOKEN are found, but there is
		// still some text remaining in the string. For example, this saves
		// “আইরিশ" in "বায়োমেট্রিকস ফিচার, যেমন: আঙুলের ছাপ, হাতের ছাপ,আইরিশ".
		else if (this.position < this.stringToTokenize.length()) {
			String nextToken = this.stringToTokenize.substring(this.position);
			this.charTermAttribute.append(nextToken);
			this.position = this.stringToTokenize.length();
			return true;
		}

		// Execute this block if no more tokens exist in the string.
		else {
			return false;
		}
	}

	/* This uses the SPLIT_REGEX to find the Next Spliting Token */
	public String getNextSplitingToken(String input) {
		Pattern pattern = Pattern.compile(SPLIT_REGEX);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(0);
		} else {
			return null;
		}
	}

	/*
	 * Reset the stored position for this object when reset() is called.
	 */
	@Override
	public void reset() throws IOException {
		super.reset();
		this.position = 0;
	}

}
