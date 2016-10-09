package shafin.nlp.util;

public class StringTool {

	/*
	 * Some times UTF-8 encoded Text contains spaces that are not in general we
	 * use. They are some special kind. Below is the list of all UNICODE space
	 * charactes that we need to remove from the text for usual text processing.
	 */
	private static String[] UNICODE_SPACE_CHARACTERS = { "\u0020", "\u00A0", "\u180E", "\u1680", "\u2000", "\u2001",
			"\u2002", "\u2003", "\u2004", "\u2005", "\u2006", "\u2007", "\u2008", "\u2009", "\u200A", "\u200B",
			"\u202F", "\u205F", "\u3000", "\uFEFF" };

	/*
	 * In preprocessing we removes the UNICODE_SPACE_CHARACRTES from Text
	 */
	public static String removeUnicodeSpaceChars(StringBuilder sb) {
		for (int i = 0; i < UNICODE_SPACE_CHARACTERS.length; i++) {
			sb = new StringBuilder(sb.toString().replaceAll(UNICODE_SPACE_CHARACTERS[i], " "));
		}
		return sb.toString();
	}

}
