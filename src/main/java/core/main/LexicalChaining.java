package core.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("unchecked")
public class LexicalChaining {

    public static Map getLexChains(String input)throws Exception{
        // Load Stop word list
        ArrayList<String> arr = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\Thesis\\IR system\\Search_engine\\Data\\stopwordList.txt")))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                arr.add(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Map> entities= MetamapMapper.getEntities(input);
        ArrayList<String> semanticSet = new ArrayList<String>();
        HashMap<String, ArrayList<String> > lexChains = new HashMap();
        ArrayList<LexChain> lexChain = new ArrayList<LexChain>();
        Map output = new HashMap();
        for(Map ent : entities){
            ArrayList<String> semanticType = (ArrayList<String>) ent.get("Semantic Types");
            for(String sem : semanticType)
            {
                // Using Preffered name
                // Check for stopwords in entity names
                String prefferedName = (String)ent.get("Preferred Name");
                Boolean stat = true;
                int idx = 0;
                for(String word: prefferedName.split(" ")){
                    if(idx == 0){
                        stat = arr.contains(word.toLowerCase());
                    }else {
                        stat = stat & arr.contains(word.toLowerCase());
                    }
                    idx += 1;
                }
                if(!stat){
                    if( !lexChains.containsKey( sem ) ){
                        lexChains.put(sem, new ArrayList<String>());
                        semanticSet.add(sem);
                    }
                    StringBuilder st = new StringBuilder();
                    for(String s:(ArrayList<String>) ent.get("Matched Words")){
                        st.append(s);
                        st.append(" ");
                    }
                    lexChains.get(sem).add( (String)ent.get("Preferred Name"));
                }


//                // Using Matched words
//                // Check for stopwords in entity names
//                Boolean stat = true;
//                int idx = 0;
//                for(String word: (ArrayList<String>) ent.get("Matched Words")){
//                    if(idx == 0){
//                        stat = arr.contains(word.toLowerCase());
//                    }else {
//                        stat = stat & arr.contains(word.toLowerCase());
//                    }
//                    idx += 1;
//                }
//                if(!stat){
//                    if( !lexChains.containsKey( sem ) ){
//                        lexChains.put(sem, new ArrayList<String>());
//                        semanticSet.add(sem);
//                    }
//                    StringBuilder st = new StringBuilder();
//                    for(String s:(ArrayList<String>) ent.get("Matched Words")){
//                        st.append(s);
//                        st.append(" ");
//                    }
//                    lexChains.get(sem).add( st.toString());
//                }

//                // Using Phrase text name
//                // Check for stopwords in entity names
//                String prefferedName = (String)ent.get("Phrase");
//                Boolean stat = true;
//                int idx = 0;
//                for(String word: prefferedName.split(" ")){
//                    if(idx == 0){
//                        stat = arr.contains(word.toLowerCase());
//                    }else {
//                        stat = stat & arr.contains(word.toLowerCase());
//                    }
//                    idx += 1;
//                }
//                if(!stat){
//                    if( !lexChains.containsKey( sem ) ){
//                        lexChains.put(sem, new ArrayList<String>());
//                        semanticSet.add(sem);
//                    }
//                    lexChains.get(sem).add( (String)ent.get("Phrase"));
//                }


            }
        }

        Iterator it = lexChains.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            LexChain element = new LexChain();
            element.setSemanticType((String)pair.getKey());
            element.setChain( (ArrayList<String>) pair.getValue());
            element.setScore( element.getChain().size() );
            lexChain.add(element);
            it.remove(); // avoids a ConcurrentModificationException
        }
        output.put("chain",lexChain);
        output.put("set", semanticSet);
        return output;
    }

    public static ArrayList<String> getSemanticSet(String input)throws Exception{
        ArrayList<Map> semanticsList= MetamapMapper.getEntities(input);
        ArrayList<String> semantics = new ArrayList<String>();
        for(Map semantic: semanticsList){
            ArrayList<String> sem = (ArrayList<String>) semantic.get("Semantic Types");
            semantics.addAll(sem);
        }

        return semantics;
    }
}
