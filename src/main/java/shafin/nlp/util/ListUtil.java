package shafin.nlp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ListUtil {

	public static List<String> pickNRandom(List<String> lst, int n) {
		List<String> copy = new LinkedList<String>(lst);
		Collections.shuffle(copy);
		return copy.subList(0, n);
	}
	
	public static void main(String[] args) {
        List<String> myList = new ArrayList<String>();
        myList.add("one");
        myList.add("two");
        myList.add("three");
        myList.add("four");
        myList.add("five");

        printList(myList);
    }

    private static void printList(List<String> myList) {
        for (String string : myList) {
            System.out.println(string);
        }
    }
}
