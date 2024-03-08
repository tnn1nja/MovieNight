package net.tnn1nja.movieNight.data.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import static net.tnn1nja.movieNight.Main.db;
import static net.tnn1nja.movieNight.Main.log;

public class Film {

    //Static Constants
    private static final String baseURL = "\\media\\film";

    //Attributes
    public int ID;
    public int YEAR;
    public int RATING;
    public int[] GENRES;
    public String TITLE;
    public String SYNOPSIS;
    public String TMDBID;

    public String DIRECTOR;
    public String[] CAST;

    public Boolean SAVED;
    public Boolean SEEN;
    public Boolean LIKED;

    public int[] PROVIDERS;


    //Constructor
    public Film(int Id, String Title, String Synopsis, int Year, int Rating, int[] Genres,
                String TmdbId, String Director, String[] Cast, int[] Providers){

        //Assign Values
        ID = Id;
        TITLE = Title;
        SYNOPSIS = Synopsis;
        YEAR = Year;
        RATING = Rating;
        GENRES = Genres;
        TMDBID = TmdbId;
        DIRECTOR = Director;
        CAST = Cast;
        PROVIDERS = Providers;

        //Load UserData
        //loadUserData();

    }


    //Get Cover URL
    public String getRelativeCoverPath(){
        return baseURL + "\\" + TMDBID;
    }


    //Load UserData
    public void loadUserData(){
        //Run Query
        ResultSet rs = db.query("SELECT * FROM UserData WHERE FilmID = " + ID);

        //Record Found
        try {
            rs.next();
            SAVED = rs.getBoolean("Saved");
            LIKED = rs.getBoolean("Liked");
            SEEN = rs.getBoolean("Seen");
            log.info("Loaded UserData for '" + TITLE + "'");

        //No Record Found
        }catch(SQLException e){
            log.warning("Failed to Load UserData for '" + TITLE + "', Assigning Defaults...");
            SAVED = false;
            LIKED = false;
            SEEN = false;
        }
    }

    //Save UserData
    public void saveUserData(){
        //If Values aren't all Default
        if ((SAVED || LIKED || SEEN)) {
            //Save The Data
            db.run("INSERT OR REPLACE INTO UserData(FilmID,Saved,Liked,Seen) " +
                    "VALUES(" + ID + "," + SAVED + "," + LIKED + "," + SEEN + ")");
            log.info("Saved UserData for '" + TITLE + "'");

        //Otherwise Skip Saving
        }else{
            log.info("Not Saving Userdata for '" + TITLE + "', All Values are Default");
        }
    }

}
