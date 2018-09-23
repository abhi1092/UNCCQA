package core.main;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class XMLParser extends DefaultHandler {
    private boolean articleTitle;
    private boolean articleAbstract;
    private boolean articleID;
    private boolean DescriptorName;
    private ArrayList<String> DescriptorNameString = new ArrayList<String>();
    private String articleAbstractString;
    private String articletitleString;
    private ArrayList<Map> listOfExtractedArticles = new ArrayList<Map>();
    public void startElement(String uri, String localName,String qName,
                             Attributes attributes) throws SAXException {
        if(qName.equals("ArticleTitle")){
            articleTitle = true;
        }
        if(qName.equals("AbstractText"))
        {
            articleAbstract = true;
        }
        if(qName.equals("ArticleId") && attributes.getValue(0).equals("pubmed"))
        {
            articleID = true;
        }
        if(qName.equals("DescriptorName")){
            DescriptorName = true;
        }

    }

    public void endElement(String uri, String localName,
                           String qName) throws SAXException {



    }


    @SuppressWarnings("unchecked")
    public void characters(char ch[], int start, int length)throws SAXException{
        if(articleTitle){
            articletitleString = new String(ch, start, length);
            articleTitle = false;
        }
        if(articleID){
            // Article ID is the last element in each subtree. Encountering this means we have reached the end of subtree. Save everything and reset
            String articleIDString = new String(ch, start, length);
            Map outputDictionary = new HashMap();
            outputDictionary.put("Article title",articletitleString);
            outputDictionary.put("Article Abstract",articleAbstractString);
            outputDictionary.put("Article ID",articleIDString);
            outputDictionary.put("Mesh Headings",new ArrayList<String>(DescriptorNameString));
            // System.out.println(outputDictionary);
            listOfExtractedArticles.add(outputDictionary);
            DescriptorNameString.clear();
            articletitleString = "";
            articleAbstractString = "";
            articleID = false;
        }
        if(articleAbstract){
            articleAbstractString = new String(ch, start, length);

            articleAbstract = false;
        }
        if(DescriptorName){
            DescriptorNameString.add(new String(ch, start, length));
            DescriptorName = false;
        }
    }

    public ArrayList<Map> getListOfExtractedArticles() {

        return listOfExtractedArticles;
    }
}
