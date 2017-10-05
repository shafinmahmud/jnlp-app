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
	
	public static boolean removeStringItem(String item, List<String> list){
		for ( int i = 0;  i < list.size(); i++){
            String tempItem = list.get(i);
            if(tempItem.equals(item)){
                list.remove(i);
            }
        }
		return true;
	}
	
	public static boolean removeAllStringItem(List<String> itemList, List<String> list){
		for ( int i = 0;  i < itemList.size(); i++){
            String tempItem = itemList.get(i);
            
            for(int j = 0; i < list.size(); i++){
            	if(tempItem.equals(list.get(j))){
            		list.remove(j);
            	}
            }
        }
		return true;
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
