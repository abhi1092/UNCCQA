package core.main;

import gov.nih.nlm.nls.metamap.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MetamapMapper {
    @SuppressWarnings("unchecked")
    public static ArrayList<Map> getEntities(String inputString)throws Exception{
        MetaMapApi api = new MetaMapApiImpl();
        api.setOptions("-y");

        List<Result> resultList = api.processCitationsFromString( inputString );
        Result result = resultList.get(0);
        ArrayList<Map> entitiesList = new ArrayList<Map>();

        // Get list of utterances
        for (Utterance utterance: result.getUtteranceList()) {

            for (PCM pcm : utterance.getPCMList()) {

                // Only select the entites for which mappings where found
                if(pcm.getMappingList().size() > 0){
                    for (Mapping map : pcm.getMappingList()) {
                        for (Ev mapEv : map.getEvList()) {
                            Map entity = new HashMap();
                            entity.put("Concept Name",mapEv.getConceptName());
                            entity.put("Preferred Name",mapEv.getPreferredName());
                            entity.put("Semantic Types",mapEv.getSemanticTypes());
                            entity.put("Matched Words",mapEv.getMatchedWords());
                            entity.put("Phrase",pcm.getPhrase().getPhraseText());
                            entitiesList.add(entity);

                        }
                    }
                }

            }
        }
        api.disconnect();
        return entitiesList;


    }
}
