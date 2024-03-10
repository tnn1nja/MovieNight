package net.tnn1nja.movieNight.data.objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static net.tnn1nja.movieNight.Main.db;
import static net.tnn1nja.movieNight.Main.log;

public class Film {

    //Static Constants
    private static final String baseURL = "\\media\\film";

    //Attributes
    private final int ID;
    private final int YEAR;
    private final int RATING;
    private final int[] GENRES;
    private final String TITLE;
    private final String SYNOPSIS;
    private final String TMDBID;

    private final String DIRECTOR;
    private final String[] CAST;

    private boolean SAVED = false;
    private boolean SEEN = false;
    private boolean LIKED = false;

    private final int[] PROVIDERS;
    private boolean HOME = false;


    //Constructor
    public Film(int Id, String Title, String Synopsis, int Year, int Rating, int[] Genres,
                String TmdbID, String Director, String[] Cast, int[] Providers){

        //Assign Values
        ID = Id;
        TITLE = Title;
        SYNOPSIS = Synopsis;
        YEAR = Year;
        RATING = Rating;
        GENRES = Genres;
        TMDBID = TmdbID;
        DIRECTOR = Director;
        CAST = Cast;


        //Check for Home Provider
        for(int i: Providers){
            if (i == Provider.HOME.getID()) {
                HOME = true;
                break;
            }
        }

        //If Found - Remove and Set Owned to true
        if(HOME){
            Arrays.sort(Providers);
            PROVIDERS = new int[Providers.length-1];
            System.arraycopy(Providers, 0, PROVIDERS, 0, PROVIDERS.length);
        }else{
            PROVIDERS = Providers;
        }

        log.fine("Film '" + TITLE + "' Initialised");

    }


    //Get Film by ID
    public static Film getFilm(int id){

        //Film Variables
        int Year;
        int Rating;
        String inGenres;
        String Title;
        String Synopsis;
        String TmdbID;

        //Extract from Film Record
        ResultSet rs = db.query("SELECT * FROM Films WHERE FilmID = " + id);
        try {
            Title = rs.getString("Title");
            Synopsis = rs.getString("Synopsis");
            Year = rs.getInt("Year");
            Rating = rs.getInt("Rating");
            inGenres = rs.getString("Genres");
            TmdbID = rs.getString("TmdbID");
        }catch(SQLException e){
            log.severe("Failed to extract Film Data (ID=" + id + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        //Format Genres
        String[] gSplit = inGenres.split(",");
        int[] Genres = new int[gSplit.length];
        for(int i = 0; i<gSplit.length; i++){
            Genres[i] = Integer.parseInt(gSplit[i]);
        }


        //People Variables
        String Director = null;
        ArrayList<String> CastBuilder = new ArrayList<String>();

        //Extract People
        rs = db.query("SELECT * FROM PRFLink WHERE FilmID = " + id);
        try {
            //For every record
            while (rs.next()) {
                int personID = rs.getInt("PersonID");
                int role = rs.getInt("Role");
                if(role==1){ //Assign Name to Director
                    Director = db.query("SELECT Name FROM People WHERE PersonID="+personID).
                            getString("Name");
                }else{ //Append Name to Cast
                    CastBuilder.add(db.query("SELECT Name FROM PEOPLE WHERE PersonID=" + personID).
                            getString("Name"));
                }
            }
        }catch(SQLException e){
            log.severe("Failed to extract People Data (ID=" + id + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        //Build Cast Array
        String[] Cast = CastBuilder.toArray(new String[0]);

        //Extract Providers
        ArrayList<Integer> ProvidersBuilder = new ArrayList<Integer>();
        try{
            rs = db.query("SELECT * FROM ProvidersLink WHERE FilmID = " + id);
            while(rs.next()){ //For Every Record
                ProvidersBuilder.add(rs.getInt("ProviderID"));
            }
        }catch(SQLException e){
            log.severe("Failed to extract Provider Data (ID=" + id + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        //Format Providers
        int[] Providers = new int[ProvidersBuilder.size()];
        for(int i = 0; i<ProvidersBuilder.size(); i++){
            Providers[i] = ProvidersBuilder.get(i);
        }


        //Load User Data and Return Film
        Film f = new Film(id, Title, Synopsis, Year, Rating, Genres, TmdbID, Director, Cast, Providers);
        f.loadUserData();
        return f;

    }

    //Get Films by IDs
    public static Film[] getFilms(int[] ids){
        Film[] output = new Film[ids.length];

        //Run getFilm() for each input
        for(int i = 0;i<ids.length;i++){
            output[i] = getFilm(ids[i]);
        }

        return output;
    }


    //Getters
    public Boolean getSaved(){return SAVED;}
    public Boolean getSeen(){return SEEN;}
    public Boolean getLiked(){return LIKED;}
    public String getRelativeCoverPath(){return baseURL + "\\" + TMDBID;}
    public int getID(){return ID;}
    public int getYear(){return YEAR;}
    public int getRating(){return RATING;}
    public int[] getGenres(){return GENRES;}
    public String getTitle(){return TITLE;}
    public String getSynopsis(){return SYNOPSIS;}
    public String getTMDBID(){return TMDBID;}
    public String getDirector(){return DIRECTOR;}
    public String[] getCast(){return CAST;}
    public int[] getProviders() {
        if(HOME){
            //Add Home Provider and Return
            int[] Providers = new int[PROVIDERS.length+1];
            System.arraycopy(PROVIDERS, 0, Providers, 0, PROVIDERS.length);
            Providers[Providers.length-1] = Provider.HOME.getID();
            return Providers;
        }
        else {
            return PROVIDERS;
        }
    }

    //Setters
    public void setSaved(boolean value){
        SAVED = value;
        saveUserData();
    }

    public void setSeen(boolean value){
        SEEN = value;
        saveUserData();
    }

    public void setLiked(boolean value){
        LIKED = value;
        saveUserData();
    }

    public void setHomeProvider(boolean value){
        if(HOME!=value) {
            if (HOME) { //Add or Do Nothing
                db.run("INSERT OR IGNORE INTO ProvidersLink(FilmID,ProviderID) VALUES(" + ID + "," +
                        Provider.HOME + ")");
            } else {//Remove from Database
                db.run("DELETE FROM ProvidersLink WHERE FilmID = " + ID + " AND ProviderID = " + Provider.HOME);
            }
            //Logging
            log.info("Updated Home Provider for '" + TITLE + "'");
        }else {
            log.warning("Home value already " + HOME + ", ignored");
        }
        HOME = value;
    }


    //Save UserData
    private void saveUserData(){
        //If Values aren't all Default
        if ((SAVED || LIKED || SEEN)) {
            //Save The Data
            db.run("INSERT OR REPLACE INTO UserData(FilmID,Saved,Liked,Seen) " +
                    "VALUES(" + ID + "," + SAVED + "," + LIKED + "," + SEEN + ")");
            log.info("Saved UserData for '" + TITLE + "'");

            //Otherwise Skip Saving
        }else{
            log.warning("Not Saving Userdata for '" + TITLE + "', All Values are Default");
        }
    }

    //Load UserData
    private void loadUserData(){
        //Run Query
        ResultSet rs = db.query("SELECT * FROM UserData WHERE FilmID = " + ID);

        //Parse ResultSet
        try{
            //Record Found
            if(rs.next()) {
                SAVED = rs.getBoolean("Saved");
                LIKED = rs.getBoolean("Liked");
                SEEN = rs.getBoolean("Seen");
                log.info("Loaded UserData for '" + TITLE + "'");
            //No Record Found
            }else{
                log.warning("Failed to Load UserData for '" + TITLE + "', Assigning Defaults...");
            }

        //Failed to Parse ResultSet
        }catch(SQLException e){
            log.severe("Failed Retrieve Values UserData ResultSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
