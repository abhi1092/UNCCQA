package core.main;

import opennlp.tools.cmdline.SystemInputStreamFactory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;

import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@SuppressWarnings("unchecked")
public class QuestionAnswering {

    private ArrayList<Sentence> sentences;
    private String question;
    private StringBuilder summary;
    private StringBuilder summary2;
    private JSONArray factoids;

    // Declare the constructor

    public QuestionAnswering(ArrayList<String> snippetList, String question)throws Exception{
        sentences = new ArrayList<Sentence>();
        for(String snippet: snippetList){
            ArrayList<String> extractedSentence = sentenceSplitter.splitter(snippet);

            for(String sent : extractedSentence){
                Sentence sentence = new Sentence();
                // Some sentences have period at the end. Remove that.
                if( sent.charAt(sent.length() - 1) == '.')
                    sent = sent.substring(0, sent.length() - 1);
                sentence.setText(sent);
                Map output = LexicalChaining.getLexChains(sent);
                sentence.setLexChain( (ArrayList<LexChain>) output.get("chain") );
                sentence.setSemanticSet( (ArrayList<String>) output.get("set") );
                sentences.add(sentence);
            }
        }
        // Initialize private variables
        summary = new StringBuilder();
        summary2 = new StringBuilder();
        // Remove similar sentence due to overlaping snippets.
        removeSimilarSentences();
        // set the question
        this.question = question;

//        // Test
//        for(Sentence sent: sentences){
//            ArrayList<LexChain> l = sent.getLexChain();
//            for(LexChain lx: l){
//                System.out.println(lx.getSemanticType() + " " + lx.getChain());
//            }
//            System.out.println("--------------------");
//        }

    }


    public void createSummary2()throws Exception{
        ArrayList<String> entitiesSet = LexicalChaining.getSemanticSet(this.question);
//        System.out.println(entitiesSet);
        ArrayList<Sentence> sentencesTemp = new ArrayList<Sentence>(sentences);
//        System.out.println(entitiesSet);
//        System.out.println("----------------");
//        for(Sentence s: sentencesTemp){
//            System.out.println(s.getSemanticSet() + ">>" + s.getText());
//        }
        while (true){
            if(sentencesTemp.size() == 0)
                break;
            // Set the default sentence as first sentence
            float max_score = sentenceSimilarity.setOverlapSimilarity(entitiesSet, sentencesTemp.get(0).getSemanticSet()), score = -1;
            String selectedString = sentencesTemp.get(0).getText();
            int index_ptr_for_sent = 0, index_to_delete = 0;
            ArrayList<String> selectedTextEntities = sentencesTemp.get(0).getSemanticSet();

            for(Sentence sent : sentencesTemp)
            {
                score = sentenceSimilarity.setOverlapSimilarity(entitiesSet, sent.getSemanticSet());

                if( score > max_score){
                    max_score = score;
                    selectedString = sent.getText();
                    selectedTextEntities = sent.getSemanticSet();
                    index_to_delete = index_ptr_for_sent;
                }
                index_ptr_for_sent += 1;
            }
            if( wordCount(summary2.toString()) + wordCount(selectedString) > 200)
                break;
            summary2.append(selectedString);
            summary2.append(". ");

//            System.out.println(selectedTextEntities + "--" + selectedString);
            sentencesTemp.remove(index_to_delete);
//            for(Sentence s: sentencesTemp){
//                System.out.println(s.getSemanticSet() + s.getText());
//            }
//            System.out.println("----------");

            // Do union between two sets
            for(String ent : selectedTextEntities){
                if( !entitiesSet.contains(ent)){
                    entitiesSet.add(ent);
                }
            }
        }
//        System.out.println(wordCount(summary2.toString()));

    }


//    public JSONArray factoidTypeQuestionAnswering()throws Exception{
//        HashMap<String, Double> globalChain = new HashMap();
//        ArrayList<String> entitiesSet = LexicalChaining.getSemanticSet(this.question);
//        double max = 0;
//        Sentence sent_max_score = null;
//        for(Sentence sent: sentences){
//            double score = sentenceSimilarity.setOverlapSimilarity(entitiesSet, sent.getSemanticSet());
//            if(score > max){
//                max = score;
//                sent_max_score = sent;
//            }
//        }
//
//        if(sent_max_score == null) {
//            sent_max_score = sentences.get(0);
//            System.out.println("Null");
//        }
//        // Get the entity as answer
//        ArrayList<LexChain> lexChain = sent_max_score.getLexChain();
//        Map<String, ArrayList<String>> entity_sem_set = new HashMap();
//        for(LexChain lexChain1: lexChain){
//            ArrayList<String> entities = lexChain1.getChain();
//            // For every entity in the lex chain check if it is in our entity list. If not then add it. If yes increase its frequency
//            for(String ent:entities){
//                if(!entity_sem_set.containsKey(ent)){
//                    entity_sem_set.put(ent,new ArrayList<String>());
//                }
//                if(!entity_sem_set.get(ent).contains(lexChain1.getSemanticType()))
//                    entity_sem_set.get(ent).add(lexChain1.getSemanticType());
//                if(globalChain.containsKey(ent)){
//                    double a = globalChain.get(ent).intValue();
//                    a = a + 1;
//                    globalChain.remove(ent);
//                    globalChain.put(ent,a);
//                }
//                else {
//                    globalChain.put(ent,1.0);
//                }
//            }
//        }
//
//        java.util.Iterator it1 = entity_sem_set.entrySet().iterator();
////        while(it1.hasNext()){
////            Map.Entry pair = (Map.Entry) it1.next();
////            System.out.println(pair.getKey() + " " + pair.getValue());
////        }
//
//        ArrayList<BiomedicalEntity> finalList = new ArrayList<BiomedicalEntity>();
//        Iterator it = globalChain.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            BiomedicalEntity element = new BiomedicalEntity();
//            element.entity = (String) pair.getKey();
//            element.score = (double) pair.getValue();
//            finalList.add(element);
//        }
//        for(BiomedicalEntity element : finalList){
//            element.score = element.score + sentenceSimilarity.setOverlapSimilarity(entitiesSet,entity_sem_set.get(element.entity));
////            System.out.println(element.entity + " " + sentenceSimilarity.setOverlapSimilarity(entitiesSet,entity_sem_set.get(element.entity)) + entitiesSet + entity_sem_set.get(element.entity));
//        }
//
//
//        // Add the idf weighing
//        for(BiomedicalEntity element : finalList){
////            System.out.println(element.entity + " " + element.score + "-----------");
//            StringTokenizer st = new StringTokenizer(element.entity);
//            double score = 0;
//            while (st.hasMoreTokens()) {
//                String term = st.nextToken();
//                score = Math.max(score,getTermIDF(term));
//            }
//            element.score = element.score * score;
////            System.out.println(element.entity + " " +  element.score + "-----------");
//        }
//        Collections.sort(finalList);
//        int count = 5;
//        JSONArray output = new JSONArray();
//        for(BiomedicalEntity e: finalList){
//
//            if(count == 0)
//                break;
//            JSONArray a = new JSONArray();
//            ArrayList<String> seperateNPs = getNP.getTheNP(e.entity);
//            if(seperateNPs.size() != 0){
//                for(String s: seperateNPs){
//                    a.add(s);
//                }
//                count--;
//                output.add(a);
//            }
//
//
//
//
//
//        }
//        return output;
//    }



    public JSONArray factoidTypeQuestionAnswering()throws Exception{
        HashMap<String, Double> globalChain = new HashMap();
        ArrayList<String> entitiesSet = LexicalChaining.getSemanticSet(this.question);
        for(Sentence sent: sentences){
            ArrayList<LexChain> lexChain = sent.getLexChain();
            double score = sentenceSimilarity.setOverlapSimilarity(entitiesSet, sent.getSemanticSet());
            for(LexChain lexChain1: lexChain){
                ArrayList<String> entities = lexChain1.getChain();
                // For every entity in the lex chain check if it is in our entity list. If not then add it. If yes increase its frequency
                for(String ent:entities){
                    if(globalChain.containsKey(ent)){
                        double a = globalChain.get(ent).intValue();
                        a = a + 1 + score;
                        globalChain.remove(ent);
                        globalChain.put(ent,a);
                    }
                    else {
                        globalChain.put(ent,1.0);
                    }
                }
            }
        }
        ArrayList<BiomedicalEntity> finalList = new ArrayList<BiomedicalEntity>();
        Iterator it = globalChain.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            BiomedicalEntity element = new BiomedicalEntity();
            element.entity = (String) pair.getKey();
            element.score = (double) pair.getValue();
            finalList.add(element);
        }

        // Add the idf weighing
        for(BiomedicalEntity element : finalList){
//            System.out.println(element.entity + " " + element.score + "-----------");
            StringTokenizer st = new StringTokenizer(element.entity);
            double score = 0;
            while (st.hasMoreTokens()) {
                String term = st.nextToken();
                score = Math.max(score,getTermIDF(term));
            }
            element.score = element.score * score;
//            System.out.println(element.entity + " " +  element.score + "-----------");
    }
        Collections.sort(finalList);
        int count = 5;
        JSONArray output = new JSONArray();
        for(BiomedicalEntity e: finalList){

            if(count == 0)
                break;
            JSONArray a = new JSONArray();
            a.add(e.entity);
            output.add(a);
            count--;
        }
        return output;
    }

    public JSONArray ListTypeQuestionAnswering()throws Exception {
        HashMap<String, Integer> globalChain = new HashMap();
        ArrayList<String> entitiesSet = LexicalChaining.getSemanticSet(this.question);
        for (Sentence sent : sentences) {
            ArrayList<LexChain> lexChain = sent.getLexChain();
            int score = (int) sentenceSimilarity.setOverlapSimilarity(entitiesSet, sent.getSemanticSet());
            for (LexChain lexChain1 : lexChain) {
                ArrayList<String> entities = lexChain1.getChain();
                // For every entity in the lex chain check if it is in our entity list. If not then add it. If yes increase its frequency
                for (String ent : entities) {
                    if (globalChain.containsKey(ent)) {
                        int a = globalChain.get(ent).intValue();
                        a = a + 1 + score;
                        globalChain.remove(ent);
                        globalChain.put(ent, a);
                    } else {
                        globalChain.put(ent, 1);
                    }
                }
            }
        }
        ArrayList<BiomedicalEntity> finalList = new ArrayList<BiomedicalEntity>();
        Iterator it = globalChain.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            BiomedicalEntity element = new BiomedicalEntity();
            element.entity = (String) pair.getKey();
            element.score = (int) pair.getValue();
            finalList.add(element);
        }

        // Add the idf weighing
        for(BiomedicalEntity element : finalList){
//            System.out.println(element.entity + " " + element.score + "-----------");
            StringTokenizer st = new StringTokenizer(element.entity);
            double score = 0;
            while (st.hasMoreTokens()) {
                String term = st.nextToken();
                score = Math.max(score,getTermIDF(term));
            }
            element.score = element.score * score;
//            System.out.println(element.entity + " " +  element.score + "-----------");
        }

        Collections.sort(finalList);
        int count = 100;
        JSONArray output = new JSONArray();
        for (BiomedicalEntity e : finalList) {
            if(count == 0)
                break;
            JSONArray a = new JSONArray();
            a.add(e.entity);
            output.add(a);
            count--;
        }
        return output;
    }

    static double getTermIDF(String termText)throws IOException {
        String indexDirectory = "D:\\Thesis\\UNCCQA\\indexDirectory";
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDirectory)));
        Term termInstance = new Term("Article Abstract", termText.toLowerCase());
        long termFreq = reader.totalTermFreq(termInstance);
        long docCount = reader.docFreq(termInstance);
        // Number of documents.
        long N = 26759399;
        // Return IDF
        if(docCount != 0)
            return Math.log(N/docCount);
        else {
//            System.out.println(">> " + termText);
            //Return 1 for unknown terms.
            return 1;

        }
    }

    public String getSummary() {
        return summary.toString();
    }

    public static int wordCount(String sentence){
        int count = 0;
        count = sentence.split(" ").length;
        return count;
    }
    public double getScore(String sent){

        float questionMatchingWeight = 1;
        double score = questionMatchingWeight * sentenceSimilarity.overlapSimilarity( sent, question);
        return score;
    }

    private void removeSimilarSentences(){
        while(true){
            int pair = getSimilarPair();
            if(pair == -1)
                break;
            // Delete one of the sentence
            sentences.remove(pair);
        }
    }

    private int getSimilarPair(){
        int pair;
        pair = -1;
        for(int i = 0; i < sentences.size(); i++){
            for(int j = 0; j < sentences.size(); j++){
                if(i != j){
                    if(sentences.get(i).getText().equalsIgnoreCase(  sentences.get(j).getText() )){
                        pair = i;
                        break;
                    }
                }
            }
        }
        return pair;
    }

    public String getSummary2() {
        return summary2.toString();
    }

    public ArrayList<Sentence> getSentences() {
        return sentences;
    }
}
