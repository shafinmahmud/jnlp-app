package net.shafin.nlp.stemmer.bengali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Someone Oblivious
 * @since 10/10/2017
 */
public class BengaliStemmer {

    private Map wordRoot = new HashMap();
    private SecondaryStemmer stemmer;

    public BengaliStemmer(String banglaStemmerDir) {
        this.stemmer = new SecondaryStemmer(banglaStemmerDir);

        try {
            BufferedReader ex = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(
                                    new File(banglaStemmerDir + "/StemDictionary.txt")), "UTF8"));

            String temp;
            while ((temp = ex.readLine()) != null) {
                String word = temp.substring(0, temp.indexOf(" ")).trim();
                String root = temp.substring(temp.indexOf(" ") + 1, temp.length()).trim();
                if (!word.equals(root)) {
                    this.wordRoot.put(word, root);
                }
            }

            ex.close();
        } catch (IOException var6) {
            Logger.getLogger(BengaliStemmer.class.getName()).log(Level.SEVERE, (String) null, var6);
        }

    }

    public void setDocument(String string) {
        HashSet docToks = new HashSet();
        StringTokenizer token = new StringTokenizer(
                string, " `’‘1234567890১২৩৪৫৬৭৮৯০।-=~!@#$%^&*()_+[]\\{}|;\':\",./<>?।”“— \n\t৷");

        while (token.hasMoreTokens()) {
            docToks.add(token.nextToken());
        }

        this.stemmer.setDictionary(docToks);
    }

    public String findRoot(String string) {
        return this.wordRoot.containsKey(string)
                ? (String) this.wordRoot.get(string) : this.stemmer.findRootString(string);
    }
}
