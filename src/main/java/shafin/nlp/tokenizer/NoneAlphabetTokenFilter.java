package shafin.nlp.tokenizer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/*
 * Author: Shafin Mahmud
 * Email : shafin.mahmnud@gmail.com
 * 
 */
public class NoneAlphabetTokenFilter extends TokenFilter {

	public static String[] UNICODE_SPACE_CHARACTERS = { "\u0020", "\u00A0", "\u180E", "\u1680", "\u2000", "\u2001",
			"\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2007", "\u2008", "\u2009", "\u200A", "\u200B",
			"\u202F", "\u205F", "\u3000", "\uFEFF" };

	public static final String ALPHABETIC_CHAR_CAPTURING_REGEX = "[^.।,`~!@#$%^&*()_\\-+=\\|{}\\[\\]'\";:\\/\\?<>‘’—\\s]+";

	/*
	 * The constructor for our custom token filter just calls the TokenFilter
	 * constructor; that constructor saves the token stream in a variable named
	 * this.input.
	 */
	public NoneAlphabetTokenFilter(TokenStream tokenStream) {
		super(tokenStream);
	}

	/*
	 * Like the BanglaWordTokenizer class, we are going to save the text of the
	 * current token in a CharTermAttribute object. In addition, we are going to
	 * use a PositionIncrementAttribute object to store the position increment
	 * of the token. Lucene uses this later attribute to determine the position
	 * of a token. Given a token stream with "This", "is", "",
	 * ”some", and "text", we are going to ensure that "This" is saved at
	 * position 1, "is" at position 2, "some" at position 3, and "text" at
	 * position 4. Note that we have completely ignored the empty string at what
	 * was position 3 in the original stream.
	 */
	protected CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
	protected PositionIncrementAttribute positionIncrementAttribute = addAttribute(PositionIncrementAttribute.class);

	/*
	 * Like we did in the BanglaWordTokenizer class, we need to override the
	 * incrementToken() function to save the attributes of the current token. We
	 * are going to pass over any tokens that are empty strings and save all
	 * others without modifying them. This function should return true if a new
	 * token was generated and false if the last token was passed.
	 */
	@Override
	public boolean incrementToken() throws IOException {

		// Loop over tokens in the token stream to find the next one
		// that is not empty
		String nextToken = null;
		while (nextToken == null) {

			// Reached the end of the token stream being processed
			if (!this.input.incrementToken()) {
				return false;
			}

			// Get text of the current token and remove any
			// leading/trailing whitespace.
			String token = replaceAllUnicodeSpace(this.input.getAttribute(CharTermAttribute.class).toString());
			String currentTokenInStream = token.trim();

			// Save the token if it is not an empty string
			if (currentTokenInStream.length() > 0 && doesContainAlphaNumeric(currentTokenInStream)){
					nextToken = currentTokenInStream;
			}
		}

		// Save the current token
		this.charTermAttribute.setEmpty().append(nextToken);
		this.positionIncrementAttribute.setPositionIncrement(1);
		return true;
	}

	public static boolean doesContainAlphaNumeric(String text) {
		Pattern pattern = Pattern.compile(ALPHABETIC_CHAR_CAPTURING_REGEX);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}

	public static String replaceAllUnicodeSpace(String text) {
		StringBuffer sb = new StringBuffer(text);
		for (int i = 0; i < UNICODE_SPACE_CHARACTERS.length; i++) {
			sb = new StringBuffer(sb.toString().replaceAll(UNICODE_SPACE_CHARACTERS[i], " "));
		}
		return sb.toString();
	}
}
