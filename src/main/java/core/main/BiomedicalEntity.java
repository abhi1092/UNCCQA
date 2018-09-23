package core.main;

public class BiomedicalEntity implements Comparable<BiomedicalEntity>{
    String entity;
    double score;
    public int compareTo(BiomedicalEntity o) {
        return Double.compare(o.score,this.score);
    }
}
