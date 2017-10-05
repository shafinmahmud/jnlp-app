package net.shafin.nlp.stemmer.bengali;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Someone Oblivious
 * @since 10/10/2017
 */
public class SecondaryStemmer {

    private Trie trie = new Trie();
    private Node rootSuffix = new Node();
    private Set dictionary = new HashSet();
    private int i = 0;


    SecondaryStemmer(String banglaStemmerDir) {
        this.rootSuffix.setCharacter(Character.valueOf('~'));
        this.ReadFiles(banglaStemmerDir + "/suff.txt");
    }

    private void ReadFiles(String suffixFile) {
        try {
            BufferedReader ex;
            String temp;
            Node temporary;
            for (ex = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(suffixFile)), "UTF8"));
                 (temp = ex.readLine()) != null; temporary.setFinalState(true)) {

                temp = (new StringBuffer(temp.trim())).reverse().toString();
                temporary = this.trie.insert(this.rootSuffix, temp);
            }

            ex.close();
            this.rootSuffix.setFinalState(false);

        } catch (Exception var5) {
            System.out.println("Exception While reading necessary files: " + var5);
        }

    }

    String findRootString(String string) {
        String root = this.findSuffixString(string);
        if (!this.dictionary.contains(root)) {
            root = this.findSuffixString(root);
            if (!this.dictionary.contains(root)) {
                return string;
            }
        }

        return root;
    }

    private synchronized String findSuffixString(String string) {
        Node temporary = this.rootSuffix;
        boolean j = false;

        for (this.i = string.length() - 1;
             this.i > 1 && temporary.getChildren().containsKey(Character.valueOf(string.charAt(this.i))); --this.i) {

            temporary = (Node) temporary.getChildren().get(Character.valueOf(string.charAt(this.i)));
            if (temporary.isFinalState()) {
                String temp = string.substring(0, this.i);
                if (this.dictionary.contains(temp)) {
                    return temp;
                }
            }
        }

        return string;
    }

    private void findSuffix(String string, ArrayList stemmed) {
        Node temporary = this.rootSuffix;
        boolean j = false;

        for (this.i = string.length() - 1;
             this.i > 1 && temporary.getChildren().containsKey(Character.valueOf(string.charAt(this.i))); --this.i) {
            temporary = (Node) temporary.getChildren().get(Character.valueOf(string.charAt(this.i)));
            if (temporary.isFinalState()) {
                stemmed.add(string.substring(0, this.i));
            }
        }
    }

    void setDictionary(Set dictionary) {
        this.dictionary.clear();
        this.dictionary = dictionary;
    }
}
