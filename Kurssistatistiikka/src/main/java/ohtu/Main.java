package ohtu;

import com.google.gson.Gson;
import java.io.IOException;
import org.apache.http.client.fluent.Request;

public class Main {

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

        Course course = mapper.fromJson(info, Course.class);
        
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
    }
}