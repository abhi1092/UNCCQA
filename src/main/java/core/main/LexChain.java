package core.main;

import java.util.ArrayList;

public class LexChain {
    private String semanticType;
    private ArrayList<String> chain;
    private float score;

    public void setSemanticType(String semanticType) {
        this.semanticType = semanticType;
    }

    public String getSemanticType() {
        return semanticType;
    }

    public ArrayList<String> getChain() {
        return chain;
    }

    public void setChain(ArrayList<String> chain) {
        this.chain = chain;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public float getScore() {
        return score;
    }
}
