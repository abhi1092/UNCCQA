package core.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

@SuppressWarnings("unchecked")
public class QuestionAnsweringMain {

    public static void main(String arg[]) throws Exception{
        String input = "Coding sequence mutations in e.g. RET, GDNF, EDNRB, EDN3, and SOX10 lead to long-segment (L-HSCR) as well as syndromic HSCR but fail to explain the transmission of the much more common short-segment form (S-HSCR). Furthermore, mutations in the RET gene are responsible for approximately half of the familial and some sporadic cases, strongly suggesting, on the one hand, the importance of non-coding variations and, on the other hand, that additional genes involved in the development of the enteric nervous system still await their discovery";
        String fileName = "phaseB_1b_03.json";
        // Load the Training data
        JSONParser parser = new JSONParser();
//        JSONObject document = (JSONObject)parser.parse(new FileReader("D:\\Thesis\\UNCCQA\\Data\\InputTestData\\" + fileName + ".json"));
        JSONObject document = (JSONObject)parser.parse(new FileReader("D:\\Thesis\\UNCCQA\\Data\\InputTestData\\" + fileName));
        JSONArray questionList = (JSONArray) document.get("questions");


        // Test area
//        JSONObject question = (JSONObject)  questionList.get(0);
//        JSONArray snippetList = (JSONArray) question.get("snippets");
//        String questionString = (String) question.get("body");
//        ArrayList<String> snippetListNew  = new ArrayList<String>();
//        for(Object snip : snippetList){
//            JSONObject ob = (JSONObject) snip;
//            snippetListNew.add( (String) ob.get("text") );
//        }
//        System.out.println(questionString);
//        String type = (String) question.get("type");
//
//        QuestionAnswering summarizer = new QuestionAnswering(snippetListNew,questionString);
//        if(type.equalsIgnoreCase("factoid"))
//            System.out.println("exact_answer" + summarizer.factoidTypeQuestionAnswering());
//        summarizer.createSummary2();
//        System.out.println(summarizer.getSummary2());



        // Final area
        JSONArray AnswerList = new JSONArray();
        int idx = 0;
        for(Object question : questionList){

            idx += 1;
            JSONObject question1 = (JSONObject) question;
            JSONArray snippetList = (JSONArray) question1.get("snippets");
            String questionString = (String) question1.get("body");
            System.out.println("Answering question " + idx + " " + questionString);
            String id = (String) question1.get("id");
            String type = (String) question1.get("type");

            ArrayList<String> snippetListNew  = new ArrayList<String>();
            for(Object snip : snippetList){
                JSONObject ob = (JSONObject) snip;
                snippetListNew.add( (String) ob.get("text") );
            }
            QuestionAnswering questionAnswering = new QuestionAnswering(snippetListNew,questionString);
            questionAnswering.createSummary2();
            Map output = new HashMap();
            output.put("id",id);
            output.put("ideal_answer", questionAnswering.getSummary2());
            if(type.equalsIgnoreCase("factoid"))
                output.put("exact_answer",questionAnswering.factoidTypeQuestionAnswering());
            if(type.equalsIgnoreCase("yesno"))
                output.put("exact_answer","yes");
            if(type.equalsIgnoreCase("list"))
                output.put("exact_answer",questionAnswering.ListTypeQuestionAnswering());
            AnswerList.add(output);
        }
        Map finalOutput = new HashMap();
        finalOutput.put("questions",AnswerList);
        try (FileWriter file = new FileWriter("D:\\Thesis\\UNCCQA\\Data\\OutputTestData\\" + fileName + "_output_2.json")) {
            file.write(toPrettyFormat(finalOutput.toString()));
            System.out.println("Successfully Copied JSON Object to File...");
        }


    }
    public static String toPrettyFormat(String jsonString)
    {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);
        return prettyJson;
    }

    public static void promptEnterKey(){
        System.out.println("Press \"ENTER\" to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
