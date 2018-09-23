package core.main;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;

public class searchMain {
    String indexDirectory = "D:\\Thesis\\IR system\\Search_engine\\indexDirectory";
    Searcher searcher;
    public static void main(String arg[])throws IOException, ParseException{
        searchMain searchMain = new searchMain();
        searchMain.search("Effect of neutral salts on the interaction of rat brain hexokinase with the outer mitochondrial membrane.");
    }

    private void search(String searchQuery)throws ParseException, IOException {
        searcher = new Searcher(indexDirectory);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();
        System.out.println(hits.totalHits +
                " documents found. Time :" + (endTime - startTime));

        for(ScoreDoc scoreDoc : hits.scoreDocs){
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: "
                    + doc.get("Article Title"));
        }
    }
}
