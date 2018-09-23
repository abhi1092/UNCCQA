package core.main;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class getNP {
    public static ArrayList<String> getTheNP(String sent2){
        ArrayList<String> result = new ArrayList<String>();
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

        for (int i = 0; i < parse.getChildrenAsList().size(); i++) {
            qu.add(parse.getChild(i));
        }
        while (!qu.isEmpty()) {
            //Pop the Child
            Tree child = qu.get(0);
            //System.out.println(child.value());
            qu.remove(0);
            //System.out.println(child.value());
            if (child.value().equals("NP")) {
                ArrayList<String> s = processNP(child);
                if (idx == 0 && s != null) {
                    result = s;
                    idx++;
                } else {
                    if (s != null)
                        result.addAll(s);
                }
            } else {

                if (child.value().equals("VP")) {
                    //System.out.println(processVP(child));
                }

            }
            for (int i = 0; i < child.getChildrenAsList().size(); i++) {
                qu.add(child.getChild(i));
            }

        }
        ArrayList<String> ab = new ArrayList<String>();
        for(int i = 0; i< result.size();i++){
            boolean stat = false;
            for(int j = 0;j< result.size();j++){
                if(i != j){
                    if(result.get(j).indexOf(result.get(i)) != -1){
                        stat = true;
                    }
                }
            }
            if(!stat)
                ab.add(result.get(i));
        }
        return ab;
    }

    public static ArrayList<String> processNP(Tree e)
    {
        String s = null;
        int j=0;
        ArrayList<String> output = new ArrayList<String>();
        for(int i=0;i<e.getChildrenAsList().size();i++)
        {
            Tree child = e.getChild(i);
            if((child.value().equals("NN")||child.value().equals("NNS")||child.value().equals("JJ")||child.value().equals("NNP"))&&child.getChild(0).isLeaf())
            {
                if(j==0)
                {
                    output.add(child.getChild(0).value());
                    j++;
                }
                else{
                    output.add(child.getChild(0).value());
                    j++;
                }

            }

        }

        return output;

    }
}
