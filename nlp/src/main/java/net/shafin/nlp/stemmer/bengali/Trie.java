package net.shafin.nlp.stemmer.bengali;

/**
 * @author Someone Oblivious
 * @since 10/10/2017
 */
public class Trie {

    Node temp;


    public Node insert(Node temp, String string) {
        this.temp = temp;
        int len = string.length();

        for (int i = 0; i < len; ++i) {
            Character c = Character.valueOf(string.charAt(i));
            if (temp.getChildren().containsKey(c)) {
                temp = (Node) temp.getChildren().get(c);
            } else {
                Node newNode = new Node();
                newNode.setCharacter(c);
                newNode.setParent(temp);
                temp.getChildren().put(c, newNode);
                temp = newNode;
            }
        }

        return temp;
    }
}
