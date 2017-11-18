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
        
        System.out.println("opiskelijanumero: " + studentNr);
        System.out.println("\nOliot:");
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