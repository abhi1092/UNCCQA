package core.main;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;


public class Searcher {

    IndexSearcher indexSearcher;
    QueryParser queryParser;
    Query query;

    public Searcher(String indexDirectoryPath)throws IOException{
        Directory indexDirectory  = FSDirectory.open(Paths.get(indexDirectoryPath));
        DirectoryReader directoryReader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher( directoryReader );
        Analyzer analyzer = new StandardAnalyzer();
        queryParser = new QueryParser("Article Title",analyzer);

    }


    public TopDocs search(String searchQuery)throws ParseException, IOException{
        query = queryParser.parse(searchQuery);
        return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
    }

    public Document getDocument(ScoreDoc scoreDoc)throws IOException{
        return indexSearcher.doc(scoreDoc.doc);
    }

}
