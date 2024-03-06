package net.tnn1nja.movieNight.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static net.tnn1nja.movieNight.Main.*;

public class APIs {

    //Constants
    private static final String address = "https://streaming-availability.p.rapidapi.com/search/ultra";
    private static final String APIKey = "55a3606031mshcf4633bebae51abp130e52jsnc5476f35f166";

    static final String[] providers = {"netflix", "disney", "prime.subscription", "iplayer", "all4"};
    public boolean PREMIUM = false;


    //Populate the Database
    public void populate(){
        //Populate the Providers Table.
        populateProviders();

        //Iterate through every provider
        log.info("Populating Database with PREMIUM=" + PREMIUM);
        for(String provider: providers){

            //Page 1 Logic
            JSONObject jo = populatePage(provider, 1);

            //Find and Cap Number of Pages
            int numPages = jo.getInt("total_pages");
            if((numPages>3)&&!PREMIUM){
                numPages = 3;
            }

            //Page 1-n Logic
            for(int i=2; i<numPages+1; i++){
                populatePage(provider, i);
            }
        }

        log.info("Database Successfully Populated.");
    }

    //Parse a Page from API to Database
    private JSONObject populatePage(String provider,  int i){
        String prompt = "&services=" + provider + "&page=" + i;
        log.finest("Api Prompt: " + prompt);
        String jsonString = call(prompt);
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            saveData(extractData(jsonObject), provider);
            log.info("Completed '" + provider + "' Page " + i + " call");

            return jsonObject;
        }catch(Exception e){
            if(jsonString.length() < 100) {
                log.severe("API Parser Failed: " + jsonString);
            }else{
                log.severe("API Parser Failed.");
            }
            e.printStackTrace();
        }

        return null;
    }


    //Make API Call (prompt = "&service=netflix&page=1")
    private String call(String prompt){
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(address + "?country=gb&type=movie&output_language=en&language=en" +
                            "&order_by=imdb_rating&desc=true" + prompt))
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

    //Extract Data From JSONObject
    private ArrayList<JSONFilm> extractData(JSONObject jsonObject){
        ArrayList<JSONFilm> output = new ArrayList<JSONFilm>();

        JSONArray results = jsonObject.getJSONArray("results");
        for (Object item: results){
            JSONFilm jf = new JSONFilm();
            JSONObject jo = (JSONObject) item;
            jf.TITLE = jo.getString("title").replace("'", "''");
            jf.SYNOPSIS = jo.getString("overview").replace("'", "''");
            jf.YEAR = jo.getInt("year");
            jf.RATING = jo.getInt("imdbRating");
            jf.TMDBID = jo.getString("tmdbID").replace("'", "''");
            JSONArray significants = jo.getJSONArray("significants");
            jf.DIRECTOR = significants.getString(0).replace("'", "''");

            StringBuilder sb = new StringBuilder();
            for(Object genre: jo.getJSONArray("genres")){
                int i = (int) genre;
                sb.append(i).append(",");
            }
            jf.GENRES = sb.toString();

            jf.CAST = new ArrayList<String>();
            for(Object castMember: jo.getJSONArray("cast")){
                jf.CAST.add(((String) castMember).replace("'", "''"));
            }

            jf.COVERHTTP = jo.getJSONObject("posterURLs").getString("185");
            output.add(jf);
        }

        return output;
    }

    //Save JSONFilm to Database
    private void saveData(ArrayList<JSONFilm> films, String provider){

        db.run("begin");
        for(JSONFilm film: films) {
            //Film Insert Command
            db.run("INSERT INTO Films(Title, Synopsis, Year, Rating, Genres, TmdbID) VALUES(" +
                    "'" + film.TITLE + "'," +
                    "'" + film.SYNOPSIS + "'," +
                    film.YEAR + "," +
                    film.RATING + "," +
                    "'" + film.GENRES + "'," +
                    "'" + film.TMDBID + "')");

            //Get FilmID
            String FilmID = null;
            ResultSet rs = db.query("SELECT FilmID From Films WHERE TmdbID ='" + film.TMDBID + "'");
            try {
                FilmID = rs.getString("FilmID");
            } catch (SQLException e) {
                log.severe("Failed to Insert Film: " + e.getMessage());
                e.printStackTrace();
            }

            //ProviderLink Insert Command
            db.run("INSERT INTO ProvidersLink(FilmID, ProviderID) VALUES(" + FilmID + ", " +
                    "(SELECT ProviderID FROM Providers WHERE ApiTAG = '" + provider + "'))");

            //People Insert Command
            for (String castMember : film.CAST) {
                db.run("INSERT INTO People(Name) VALUES('" + castMember + "')");
                db.run("INSERT INTO PRFLink(FilmID, PersonID, Role) VALUES(" + FilmID + "," +
                        "(SELECT PersonID FROM People WHERE Name = '" + castMember + "'), 0)");
            }
            db.run("INSERT INTO PEOPLE(Name) VALUES('" + film.DIRECTOR + "')");
            db.run("INSERT INTO PRFLink(FilmID, PersonID, Role) VALUES(" + FilmID + "," +
                    "(SELECT PersonID FROM People WHERE Name = '" + film.DIRECTOR + "'), 1)");

            //Download Cover
            downloadImage(film.COVERHTTP, film.TMDBID);

            //Logging
            log.info("'" + film.TITLE + "' Added to the Database");
        }
        db.run("commit");

    }

    //Download Image From HTTP Address
    private void downloadImage(String http, String filename){
        try {
            URL url = new URL(http);
            InputStream in = url.openStream();
            Path destination = Path.of(".\\media\\film\\" + filename + ".jpg");
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.warning("Failed to Download Image " + e.getMessage());
            e.printStackTrace();
        }
    }


    //Populate the Providers Table (Hardcoded)
    private void populateProviders(){
        try {
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(1, 'Netflix', " +
                    "'https://www.netflix.co.uk/', 'netflix.jpg', 'netflix')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(2, 'Disney Plus', " +
                    "'https://www.disneyplus.com/', 'disney.jpg', 'disney')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(3, 'Amazon Prime Video', " +
                    "'https://www.amazon.co.uk/', 'prime.jpg', 'prime.subscription')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(4, 'BBC iPlayer', " +
                    "'https://bbc.co.uk/iplayer/', 'iplayer.jpg', 'iplayer')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(5, 'All 4', " +
                    "'https://www.channel4.com/', 'all4.jpg', 'all4')");
            db.runUnhandled("INSERT INTO Providers(ProviderID, Name, URL, Logo, ApiTag) VALUES(6, 'Custom'," +
                    " null, 'custom.jpg', 'custom')");
            log.info("Providers Table Populated.");

        }catch (SQLException e){
            String violation = "[SQLITE_CONSTRAINT_PRIMARYKEY] A PRIMARY KEY constraint failed " +
                    "(UNIQUE constraint failed: Providers.ProviderID)";
            if (e.getMessage().equalsIgnoreCase(violation)) {
                log.info("Providers Table Already Populated... Skipping");
            }else{
                log.severe("Failed to Populate Providers Table - SQLException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //Film Data Class
    private static class JSONFilm{
        String TITLE;
        String SYNOPSIS;
        int YEAR;
        int RATING;
        String TMDBID;
        String DIRECTOR;
        String GENRES;
        ArrayList<String> CAST;
        String COVERHTTP;
    }

}