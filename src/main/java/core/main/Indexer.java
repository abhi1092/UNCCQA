package core.main;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

public class Indexer {

    private IndexWriter writer;

    public Indexer(String indexDirectoryPath)throws IOException{
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDirectoryPath));
        Analyzer analyser = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyser);
        writer = new IndexWriter(indexDirectory, config);

    }

    public void close() throws IOException{
        writer.close();
    }

    private Document getDocument(Map doc)throws FileNotFoundException, IOException{
        Document document = new Document();
        //index file contents
        Field articleTitle = new Field( "Article Title", (String) doc.get("Article title"), TextField.TYPE_STORED );
        if( doc.get("Article Abstract") != null ){
            Field articleAbstract = new Field("Article Abstract", (String) doc.get("Article Abstract"), TextField.TYPE_STORED);
            document.add(articleAbstract);
        }
        Field articleID = new Field("Article ID", (String) doc.get("Article ID"), TextField.TYPE_STORED);
//         Add the field to document
        document.add(articleTitle);
        document.add(articleID);
        return document;
    }

    private void indexFile(Map doc)throws IOException{
        Document document = getDocument(doc);
        writer.addDocument(document);
    }

    public int createIndex(ArrayList<Map> data)throws IOException{
        // Get the Maps from Map list and index each map as document
        int i = 0;
        for( Map doc : data){
            //System.out.println("Indexing file number " + (i + 1));
            indexFile(doc);
            i += 1;
        }
        return writer.numDocs();
    }
}
