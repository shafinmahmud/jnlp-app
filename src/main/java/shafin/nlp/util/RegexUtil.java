package shafin.nlp.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Version 1.1
 */
public class RegexUtil {

	public static String getFirstMatch(String input, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(0);
		} else {
			return "";
		}
	}

	public static Set<String> findUniqueMatchedStrings(String input, String regex) {
		Set<String> allMatches = new HashSet<>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			allMatches.add(matcher.group());
		}
		return allMatches;
	}

	public static boolean containsPattern(String input, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		String string = "ARD has been implementing dhaka Comprehensive in Dhanmondi Eye Care Services through well established four Eye Hospitals located in Sylhet district,";
		Set<String> matches = findUniqueMatchedStrings(string, "(?i)(dha[\\w]*)");
		System.out.println(matches);
	}
}
