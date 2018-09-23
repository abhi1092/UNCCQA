package core.main;

import java.util.ArrayList;
import java.util.HashMap;

public class Sentence implements Comparable<Sentence>{
    private String text;
    private float score = 0;
    private ArrayList<LexChain> lexChain;
    private ArrayList<String> semanticSet;
    boolean done;
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int compareTo(Sentence o) {
        float score = o.getScore();
        return Float.compare(o.score,this.score);
    }

    public ArrayList<LexChain> getLexChain() {
        return lexChain;
    }

    public void setLexChain(ArrayList<LexChain> lexChain) {
        this.lexChain = lexChain;
    }

    public ArrayList<String> getSemanticSet() {
        return semanticSet;
    }

    public void setSemanticSet(ArrayList<String> semanticSet) {
        this.semanticSet = semanticSet;
    }
}
