package net.tnn1nja.movieNight.data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static net.tnn1nja.movieNight.Main.log;

public class APIs {

    //Constants
    private final String address = "https://streaming-availability.p.rapidapi.com/search/basic?";
    private final String APIKey = "55a3606031mshcf4633bebae51abp130e52jsnc5476f35f166";

    //Out-facing Methods
    public void test(){
        System.out.println(
                call("country=gb" +
                        "&service=netflix" +
                        "&type=movie" +
                        "&page=1" +
                        "&output_language=en" +
                        "&language=en")
        );
    }


    //Make a Call to the API
    private String call(String prompt){
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(address + prompt))
                    .header("X-RapidAPI-Key", APIKey)
                    .header("X-RapidAPI-Host", "streaming-availability.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        }catch(InterruptedException | IOException e){
            log.severe("API Request Failed: " + e.getMessage());
            e.printStackTrace();
        }

        return response.body();
    }

}
