package net.shafin.nlp.junk;

/**
 * @author Shafin Mahmud
 * @since 10/2/2016
 */
public class SortClass implements Comparable<SortClass> {

    public String key;
    public int value;

    public SortClass() {
    }

    public SortClass(String key, int value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(SortClass o) {
        return o.value - this.value;
    }
}
