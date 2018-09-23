package core.main;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import org.apache.lucene.queryparser.classic.ParseException;

public class LuceneTester {

    String indexDirectory = "D:\\Thesis\\IR system\\Search_engine\\indexDirectory";
    String dataDir = "D:\\Thesis\\IR system\\Search_engine\\Data";
    Indexer indexer;
    Searcher searcher;
//    public static void main(String args[]){
//        LuceneTester luceneTester;
//        try{
//            luceneTester = new LuceneTester();
//            luceneTester.createIndex();
//            System.out.println("File Indexing done...");
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//    }

//    private void createIndex()throws IOException{
//        indexer = new Indexer(indexDirectory);
//        int numIndexed;
//        long startTime = System.currentTimeMillis();
//        numIndexed = indexer.createIndex(dataDir);
//        long endTime = System.currentTimeMillis();
//        indexer.close();
//        System.out.println(numIndexed+" File indexed, time taken: "
//                +(endTime-startTime)+" ms");
//    }

}
