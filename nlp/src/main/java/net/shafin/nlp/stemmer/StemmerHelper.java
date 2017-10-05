package net.shafin.nlp.stemmer;

import java.util.ArrayList;
import java.util.List;

import net.shafin.common.util.FileHandler;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class StemmerHelper {

    public static void main(String[] args) {
        //BnStemmerLight stemmerLight = new BnStemmerLight();
//        BengaliStemmer bs = new BengaliStemmer("resources");
//        List<String> words = FileHandler
//                .readFile("D:\\DOCUMENT\\GoogleDrive\\NLP\\KEYWORD\\PROJECT_WORK\\nlp\\words.txt");
//        List<String> stopWords = FileHandler
//                .readFile("D:\\DOCUMENT\\GoogleDrive\\NLP\\KEYWORD\\PROJECT_WORK\\nlp\\stopword.txt");
//
//        for (String s : words) {
//            String given = s.split("\\s")[0];
//            if (!stopWords.contains(given)) {
//                //String root = stemmerLight.stem(given);
//                String root = bs.findRoot(given);
//                //FileHandler.appendFile("D:\\DOCUMENT\\GoogleDrive\\NLP\\KEYWORD\\PROJECT_WORK\\nlp\\root_output.txt",
//                //given + "  :  " + root + "\n");
//                System.out.println(given + " : " + root);
//            } else {
//                System.out.println("stopword: " + given);
//            }
//
//        }
    }

    public static List<String> getStemmedPhraseList(List<String> phraseList) {
        List<String> stemmedList = new ArrayList<>();
        //BnStemmerLight stemmerLight = new BnStemmerLight();
//        BengaliStemmer bs = new BengaliStemmer("resources");
//        for (String phrase : phraseList) {
//            String root = bs.findRoot(phrase);
//            stemmedList.add(root);
//        }
        return stemmedList;
    }
}
