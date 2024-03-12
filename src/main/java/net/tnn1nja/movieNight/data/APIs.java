package net.tnn1nja.movieNight.data;

import net.tnn1nja.movieNight.data.objects.Provider;
import org.json.JSONArray;
import org.json.JSONException;
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


    //Check If Database is Empty
    public boolean ifDatabaseEmpty(){
        try {
            ResultSet rs = db.query("SELECT COUNT(*) FROM Films");
            if (rs.getInt(1) == 0) {
                return true;
            }
        }catch(SQLException e){
            log.severe("Failed to read Database.");
            e.printStackTrace();
        }
        return false;
    }

    //Populate the Database
    public void populateDatabase(){
        //Populate the Providers Table.
        populateProviders();

        //Iterate through every provider
        for(String provider: Provider.getApiTags()){

            //Clean ProvidersLink
            db.run("DELETE FROM ProvidersLink WHERE ProviderID=" +
                    "(SELECT ProviderID FROM Providers WHERE ApiTag='" + provider + "')");
            log.info("Provider Data For '"+ provider + "' Deleted.");

            //Page 1 Logic + Find Number of Pages
            int numPages = populatePage(provider, 1);

            //Page 1-n Logic
            for(int i=2; i<numPages+1; i++){
                populatePage(provider, i);
            }

        }

        //Logging
        log.info("Database Successfully Populated.");
    }


    //Parse a Page from API to Database
    private int populatePage(String provider,  int i){
        //Concatenate API Prompt
        String prompt = "&services=" + provider + "&page=" + i;
        log.finest("Api Prompt: " + prompt);
        String jsonString = call(prompt);

        //Try to Parse the Data
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            saveData(extractData(jsonObject), provider); //Save Data Works with Empty List
            int numPages = jsonObject.getInt("total_pages");
            log.info("Completed '" + provider + "' Page " + i + "/" + numPages + " call");

            //Output the Parsed JSON Object
            return numPages;

        //Log if it Fails
        }catch(Exception e){
            if(jsonString.length() < 100) { //Only Show Useful API Responses.
                log.severe("API Parser Failed: " + jsonString);
            }else{
                log.severe("API Parser Failed.");
            }
            e.printStackTrace();
        }

        //Catch All Return
        return -1;
    }

    //Make API Call (prompt = "&service=netflix&page=1")
    private String call(String prompt){
        //Create Object to Hold Reponse
        HttpResponse<String> response = null;
        try {
            //Build API Call
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(address + "?country=gb&type=movie&output_language=en&language=en" +
                            "&order_by=imdb_rating&desc=true" + prompt))
                    .header("X-RapidAPI-Key", APIKey)
                    .header("X-RapidAPI-Host", "streaming-availability.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            //Send API Call
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        //Log Failure
        }catch(InterruptedException | IOException e){
            log.severe("API Request Failed: " + e.getMessage());
            e.printStackTrace();
        }

        //Return API Response
        return response.body();
    }

    //Extract Data From JSONObject
    private ArrayList<JSONFilm> extractData(JSONObject jsonObject){
        ArrayList<JSONFilm> output = new ArrayList<JSONFilm>();

        //For Each Film in the Input
        JSONArray results = jsonObject.getJSONArray("results");
        for (Object item: results){
            //Try to Extract its Data
            try {
                //Simple Extraction
                JSONFilm jf = new JSONFilm();
                JSONObject jo = (JSONObject) item;
                jf.TITLE = jo.getString("title").replace("'", "''");
                jf.SYNOPSIS = jo.getString("overview").replace("'", "''");
                jf.YEAR = jo.getInt("year");
                jf.RATING = jo.getInt("imdbRating");
                jf.TMDBID = jo.getString("tmdbID").replace("'", "''");
                jf.DIRECTOR = jo.getJSONArray("significants").getString(0).replace("'", "''");
                jf.COVERHTTP = jo.getJSONObject("posterURLs").getString("185");

                //Create Genres String
                StringBuilder sb = new StringBuilder();
                for (Object genre : jo.getJSONArray("genres")) {
                    int i = (int) genre;
                    sb.append(i).append(",");
                }
                if(sb.length() == 0){
                    throw new NullPointerException();
                }
                jf.GENRES = sb.toString();

                //Create Cast Array
                jf.CAST = new ArrayList<String>();
                for (Object castMember : jo.getJSONArray("cast")) {
                    jf.CAST.add(((String) castMember).replace("'", "''"));
                }

                //Add To Output Array
                output.add(jf);

            //If Data is Missing (Cant be retrieved, returns null)
            }catch (JSONException | NullPointerException e){
                log.warning("Film Data Incomplete, Skipping...");
            }
        }

        //Return Parsed Data
        return output;
    }

    //Save JSONFilm to Database
    private void saveData(ArrayList<JSONFilm> films, String provider){

        //Contain Within One Transaction (for efficiency)
        db.run("begin");

        //For Every Film Inputted...
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
            log.info("'" + film.TITLE + "' Updated/Added to the Database");
        }

        //Send Transaction
        db.run("commit");

    }

    //Download Image From HTTP Address
    private void downloadImage(String http, String filename){
        //Stream Data from HTTP Address to File
        try {
            URL url = new URL(http);
            InputStream in = url.openStream();
            Path destination = Path.of(".\\media\\film\\" + filename + ".jpg");
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);

        //Log Failure
        } catch (IOException e) {
            log.severe("Failed to Download Image " + e.getMessage());
            e.printStackTrace();
        }
    }


    //Populate the Providers Table (Hardcoded)
    private void populateProviders(){
        try {

            //Run SQL Commands
            db.runUnhandled(getProviderSQL(Provider.NETFLIX));
            db.runUnhandled(getProviderSQL(Provider.DISNEY));
            db.runUnhandled(getProviderSQL(Provider.IPLAYER));
            db.runUnhandled(getProviderSQL(Provider.All4));
            db.runUnhandled(getProviderSQL(Provider.HOME));

            //Logging
            log.info("Providers Table Populated.");

        //In Case of Failure
        }catch (SQLException e){

            //Primary Key Constraint Violation Means The Algorithm has Already Run
            String violation = "[SQLITE_CONSTRAINT_PRIMARYKEY] A PRIMARY KEY constraint failed " +
                    "(UNIQUE constraint failed: Providers.ProviderID)";
            if (e.getMessage().equalsIgnoreCase(violation)) {
                log.info("Providers Table Already Populated... Skipping");

            //Log Failure
            }else{
                log.severe("Failed to Populate Providers Table - SQLException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //Return SQL Insert Command
    private String getProviderSQL(Provider p){
        return "INSERT OR REPLACE INTO Providers(ProviderID, Name, URL, ApiTag) VALUES('" +
                p.getID() + "','" + p.getName() + "','" + p.getURL() + "','" + p.getApiTag() + "')";
    }


    //Film Data Class (Non-Type Specific Array)
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