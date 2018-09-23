package core.main;


import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class sentenceSplitter{

    public static ArrayList<String> splitter(String inputText)
    {
        SentenceDetector sentenceDetector = null;
        InputStream modelIn = null;

        try {
            modelIn = new FileInputStream("en-sent.bin");

            final SentenceModel sentenceModel = new SentenceModel(modelIn);
            modelIn.close();
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        }
        catch (final IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            if (modelIn != null) {
                try {
                    modelIn.close();
                } catch (final IOException e) {}
            }
        }
        return new ArrayList<String>(Arrays.asList(sentenceDetector.sentDetect(inputText)));
    }
}
