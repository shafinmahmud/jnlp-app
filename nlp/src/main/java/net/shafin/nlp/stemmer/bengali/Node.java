package net.shafin.nlp.stemmer.bengali;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Someone Oblivious
 * @since 10/10/2017
 */
public class Node {

    private boolean finalState;
    private boolean suffix;
    private boolean prefix;
    private Node parent;
    private Character character;
    private Map children;

    public Node() {
        this.children = new HashMap();
    }

    public boolean isFinalState() {
        return finalState;
    }

    public void setFinalState(boolean finalState) {
        this.finalState = finalState;
    }

    public boolean isSuffix() {
        return suffix;
    }

    public void setSuffix(boolean suffix) {
        this.suffix = suffix;
    }

    public boolean isPrefix() {
        return prefix;
    }

    public void setPrefix(boolean prefix) {
        this.prefix = prefix;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public Map getChildren() {
        return children;
    }

    public void setChildren(Map children) {
        this.children = children;
    }
}
