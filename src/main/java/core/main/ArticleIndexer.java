package core.main;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class ArticleIndexer {




    public static void main(String arg[])throws IOException{
        ArticleIndexer parsers;
        String indexDirectory = "D:\\Thesis\\IR system\\Search_engine\\indexDirectory";
        String dataDir = "F:\\pubmed\\New folder";
        Indexer indexer = new Indexer(indexDirectory);
        try{
            File Datadir = new File(dataDir);
            File[] files = Datadir.listFiles();
            for( File file : files){
                // Get the file
                InputStream xmlInput  =
                        new FileInputStream(file);
                // Parse the artilces from input file
                long startTime = System.currentTimeMillis();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                XMLParser handler = new XMLParser();
                saxParser.parse(xmlInput,handler);
                ArrayList<Map> parsedArticle = handler.getListOfExtractedArticles();
                long endTime = System.currentTimeMillis();
                // Index the output
                parsers = new ArticleIndexer();
                long startTime1 = System.currentTimeMillis();
                int numIndexed = indexer.createIndex(parsedArticle);
                long endTime1 = System.currentTimeMillis();
                System.out.println(file.getName()+" File indexed, time taken for parsing: "
                        +(endTime-startTime)+" ms and time taken for indexing: " + (endTime1-startTime1)+" ms");
            }
            indexer.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
