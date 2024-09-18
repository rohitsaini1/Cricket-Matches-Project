package org.cricket.cricketAssignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.List;

public class CricketMatches {
    
    public static void main(String[] args) throws IOException {
        String apiUrl = "https://api.cuvora.com/car/partner/cricket-data";
        String apiKey = "test-creds@2320";
        
        
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("apiKey", apiKey)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            
            
            ObjectMapper mapper = new ObjectMapper();
            CricketData cricketData = mapper.readValue(response.body().string(), CricketData.class);
            
            
            String highestScoringTeam = "";
            int highestScore = 0;
            int count300PlusMatches = 0;

            List<Match> matches = cricketData.getData(); 
            for (Match match : matches) {
                if (match.getMs().equals("result")) {  
                    
                    int t1Score = parseScore(match.getT1s());
                    int t2Score = parseScore(match.getT2s());

                    if (t1Score > highestScore) {
                        highestScore = t1Score;
                        highestScoringTeam = match.getT1();
                    }
                    if (t2Score > highestScore) {
                        highestScore = t2Score;
                        highestScoringTeam = match.getT2();
                    }

                    if (t1Score + t2Score > 300) {
                        count300PlusMatches++;
                    }
                }
            }

            System.out.println("Highest Score: " + highestScore + " and Team Name is: " + highestScoringTeam);
            System.out.println("Number Of Matches with total 300 Plus Score: " + count300PlusMatches);
        }
    }

    private static int parseScore(String score) {
        if (score == null || score.isEmpty()) {
            return 0;
        }
        String[] parts = score.split("/");
        return Integer.parseInt(parts[0]);
    }
}
