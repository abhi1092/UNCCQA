package core.main;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.*;
import edu.stanford.nlp.trees.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import java.lang.Math;
import java.util.List;
import java.util.Vector;

import abner.Tagger;
public class test {

    public static void main(String args[])throws IOException {

        String sent2 = "The C-terminus of the ataxin-3 protein";

        System.out.println(getNP.getTheNP(sent2));

    }







}
