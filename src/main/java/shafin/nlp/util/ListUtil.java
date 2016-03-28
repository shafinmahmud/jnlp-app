package shafin.nlp.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ListUtil {

	public static List<String> pickNRandom(List<String> lst, int n) {
		List<String> copy = new LinkedList<String>(lst);
		Collections.shuffle(copy);
		return copy.subList(0, n);
	}
}
