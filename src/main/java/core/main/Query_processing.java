package core.main;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class Query_processing {
    Query_processing(){}

    public static String processQuestion(String sent2)
    {
        String result = null;
        int idx = 0;
        String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
        //String sent2 = "Are there any desmins present in plants?";
        TokenizerFactory<CoreLabel> tokenizerFactory =
                PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tok =
                tokenizerFactory.getTokenizer(new StringReader(sent2));
        List<CoreLabel> rawWords2 = tok.tokenize();
        Tree parse = lp.apply(rawWords2);
        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

        List<Tree> qu = new ArrayList<Tree>();

        for(int i=0;i<parse.getChildrenAsList().size();i++)
        {
            qu.add(parse.getChild(i));
        }
        while(!qu.isEmpty())
        {
            //Pop the Child
            Tree child = qu.get(0);
            //System.out.println(child.value());
            qu.remove(0);
            //System.out.println(child.value());
            if(child.value().equals("NP"))
            {
                String s = processNP(child);
                if(idx==0&&s!=null)
                {
                    result = s;
                    idx++;
                }
                else
                {	if(s!=null)
                    result = result + s;
                }
            }
            else{

                if(child.value().equals("VP"))
                {
                    //System.out.println(processVP(child));
                }

            }
            for(int i=0;i<child.getChildrenAsList().size();i++)
            {
                qu.add(child.getChild(i));
            }
        }

	    /*
	    // You can also use a TreePrint object to print trees and dependencies
	    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
	    tp.printTree(parse);
		*/
        return result;
    }

    public static String processNP(Tree e)
    {
        String s = null;
        int j=0;
        for(int i=0;i<e.getChildrenAsList().size();i++)
        {
            Tree child = e.getChild(i);
            if((child.value().equals("NN")||child.value().equals("NNS")||child.value().equals("JJ")||child.value().equals("NNP"))&&child.getChild(0).isLeaf())
            {
                if(j==0)
                {
                    s = child.getChild(0).value() + " ";
                    j++;
                }
                else{
                    s = s + child.getChild(0).value() + " ";
                    j++;
                }

            }

        }

        return s;

    }
    public static String processVP(Tree e)
    {
        String s = null;
        int j=0;
        for(int i=0;i<e.getChildrenAsList().size();i++)
        {
            Tree child = e.getChild(i);
            if((child.value().equals("NN")||child.value().equals("NNS")||child.value().equals("JJ"))&&child.getChild(0).isLeaf())
            {
                if(j==0)
                {
                    s = child.getChild(0).value() + " ";
                    j++;
                }
                else{
                    s = s + child.getChild(0).value() + " ";
                    j++;
                }

            }

        }

        return s;
    }
}
