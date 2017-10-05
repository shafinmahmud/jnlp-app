package net.shafin.nlp.junk;

import net.shafin.nlp.stemmer.bengali.BengaliStemmer;

import java.io.*;
import java.util.*;

/**
 * @author FBI
 */

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class BengaliKeywordExtraciton {

    /// addding word list for sorting 
    ArrayList<SortClass> wordList = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public class sorter implements Comparator<sorter> {

        int i, j;
        double val;

        public sorter() {
        }

        public sorter(int i, int j, double val) {
            this.i = i;
            this.j = j;
            this.val = val;
        }

        @Override
        public int compare(sorter o1, sorter o2) {
            return Double.compare(o1.val, o2.val);
        }

    }

    public BengaliKeywordExtraciton() {
    }

    public BengaliKeywordExtraciton(String readDirectory, String writeDerectory,
                                    BengaliStemmer bs, String catName) throws IOException {

        wordList.clear();
        // reading resource dirctory 
        File f = new File(readDirectory);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);

        /// creating bangla steamer 
        //BengaliStemmer bs = new BengaliStemmer("resources");
        /// reading whole files in a string total
        String total = "";
        while (br.ready()) {
            total = total + br.readLine();
            //System.out.println("total " + total);
        }

        /// reding file for banned word list
        HashMap<String, Boolean> abboy = new HashMap<>();
        f = new File("stopword.txt");
        fr = new FileReader(f);
        br = new BufferedReader(fr);

        while (br.ready()) {
            abboy.put(br.readLine(), Boolean.TRUE);
        }

        /// spliting the total in lines
        String[] lines = total.split("([।])+");

        /// map word and  count frequencey and create word list 
        Map<String, Integer> mapWordFreq = new HashMap<String, Integer>();
        ArrayList<ArrayList<String>> lineWordList = new ArrayList<>();   // Word List per Line
        ArrayList<Set<String>> lineWordSet = new ArrayList<>();

        @SuppressWarnings("unused")
        double totalWordsInDoc = 0f;
        for (String x : lines) {
            //System.out.println(" -> " + x);

            ArrayList<String> tmpwordList = new ArrayList<>();
            Set<String> tmpwordset = new HashSet<>();
            String[] words = x.split("([  .;(—:‘’,)-])+");
            for (String y : words) {
                String tmp = bs.findRoot(y);
                if (tmp.length() <= 1) {
                    continue;
                }
                if (tmp.trim().isEmpty()) {
                    continue;
                }
                if (mapWordFreq.containsKey(tmp)) {
                    mapWordFreq.put(tmp, mapWordFreq.get(tmp) + 1);
                } else {
                    mapWordFreq.put(tmp, 1);
                }
                tmpwordList.add(tmp);
                tmpwordset.add(tmp);
            }
            lineWordList.add(tmpwordList);
            lineWordSet.add(tmpwordset);
            totalWordsInDoc += tmpwordList.size();
        }
        f = new File(writeDerectory);
        if (!f.exists()) {
            //System.out.println("File Not Found");
            f.createNewFile();
        }

        FileWriter fw = new FileWriter(f, true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(catName + " ");
        for (String key : mapWordFreq.keySet()) {
            if (abboy.containsKey(key)) {
                continue;
            }

            for (int loop = 0; loop < mapWordFreq.get(key); loop++) {
                bw.write(key + " ");
            }
            //System.out.println(key + " " + mapWordFreq.get(key));
            wordList.add(new SortClass(key, mapWordFreq.get(key)));
        }
        bw.newLine();


        bw.close();
        br.close();
        fw.close();
        fr.close();
    }

    public static void main(String[] args) throws IOException {
        // BengaliStemmer bs = new BengaliStemmer("resources");
        // BengaliKeywordExtraciton bk = new BengaliKeywordExtraciton("D:/link4554.txt", "D:/link4554out.txt", bs, "");
    }
}
