package shafin.nlp.test;

/**
 *
 * @author FBI
 */
public class sortClass implements Comparable<sortClass> {

    public String key;
    public int value;

    public sortClass() {
    }

    public sortClass(String key, int value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(sortClass o) {
        return o.value - this.value;
    }

}
