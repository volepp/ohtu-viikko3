package ohtu;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.fluent.Request;

public class Main {
	
	private static Course course;

    public static void main(String[] args) throws IOException {
        // vaihda oma opiskelijanumerosi seuraavaan, ÄLÄ kuitenkaan laita githubiin omaa opiskelijanumeroasi
        String studentNr = "";
        if ( args.length>0) {
            studentNr = args[0];
        }

        String url = "https://studies.cs.helsinki.fi/ohtustats/students/"+studentNr+"/submissions";

        String bodyText = Request.Get(url).execute().returnContent().asString();

        Gson mapper = new Gson();
        Submission[] subs = mapper.fromJson(bodyText, Submission[].class);
        
        String infoUrl = "https://studies.cs.helsinki.fi/ohtustats/courseinfo";

        String info = Request.Get(infoUrl).execute().returnContent().asString();

        course = mapper.fromJson(info, Course.class);
        
        String  statsUrl = "https://studies.cs.helsinki.fi/ohtustats/stats";
        String stats = Request.Get(statsUrl).execute().returnContent().asString();
        
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(stats).getAsJsonObject();

        ArrayList<WeekStats> weekStats = new ArrayList<WeekStats>();
        for(int i = 0; i < jsonObject.size(); i++) {
        	WeekStats w = mapper.fromJson(jsonObject.get(""+(i+1)), WeekStats.class);
        	weekStats.add(w);
        }
        
        for(int i = 0; i < subs.length; i++) {
        	subs[i].setMaxExerciseAmount(course.getExercises().get(i));
        }
        
        System.out.println(course + "\n");
        System.out.println("opiskelijanumero: " + studentNr + "\n");
        int exAmount = 0;
        int totalHours = 0;
        for (Submission submission : subs) {
            System.out.println(submission);
            exAmount += submission.getExercises().size();
            totalHours += submission.getHours();
        }
        System.out.println("\nyhteensä: " + exAmount + " tehtävää " + totalHours + " tuntia");
        
        int courseTotalSubmissions = 0;
        int courseTotalExercises = 0;
        for(WeekStats ws : weekStats) {
        	courseTotalSubmissions += ws.getStudents();
        	courseTotalExercises += ws.getExercise_total();
        }
        
        System.out.println("kurssilla yhteensä " + courseTotalSubmissions + " palautusta, palautettuja tehtäviä " + courseTotalExercises + " kpl");
    }
}