package core.main;

import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.ArrayList;

public class sentenceSimilarity {

    public static double overlapSimilarity(String sentence1, String sentence2){
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String sentence1Tokens[] = tokenizer.tokenize(sentence1);
        String sentence2Tokens[] = tokenizer.tokenize(sentence2);
        int intersection = 0;
        int union;
        for(int i = 0; i < sentence1Tokens.length; i ++){

            for(int j = 0; j < sentence2Tokens.length; j++){

                if( sentence1Tokens[i].equalsIgnoreCase( sentence2Tokens[j] ) ){
                    intersection += 1;
                }
            }
        }

        if( sentence1Tokens.length > sentence2Tokens.length ){
            union = sentence2Tokens.length;
        }
        else {
            union = sentence1Tokens.length;
        }
        return (double) intersection/union;
    }

    public static float setOverlapSimilarity(ArrayList<String> set1,ArrayList<String> set2 ){
        int intersection = 0, union = 0;

        for( int i = 0; i < set1.size(); i++){
            for(int j = 0; j < set2.size(); j++){
                if( set1.get(i).equalsIgnoreCase( set2.get(j) ) ){
                    intersection += 1;
                }
            }
        }
        if(set1.size() > set2.size())
            union = set2.size();
        else
            union = set1.size();
        return (float)intersection;

    }
}
