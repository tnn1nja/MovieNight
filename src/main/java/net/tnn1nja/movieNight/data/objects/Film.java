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
    public int ID;
    public int YEAR;
    public int RATING;
    public int[] GENRES;
    public String TITLE;
    public String SYNOPSIS;
    public String TMDBID;

    public String DIRECTOR;
    public String[] CAST;

    private Boolean SAVED;
    private Boolean SEEN;
    private Boolean LIKED;

    public int[] PROVIDERS;


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
        PROVIDERS = Providers;

        //Load UserData
        loadUserData();

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
        log.info(Arrays.toString(gSplit));
        int[] Genres = new int[gSplit.length];
        for(int i = 0; i<gSplit.length; i++){
            Genres[i] = Integer.parseInt(gSplit[i]);
        }
        log.info(Arrays.toString(Genres));


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

        log.info(Director);
        log.info(Arrays.toString(Cast));

        //Return Film
        return new Film(id, Title, Synopsis, Year, Rating, Genres, TmdbID, Director, Cast, null);

    }


    //Getters
    public String getRelativeCoverPath(){return baseURL + "\\" + TMDBID;}
    public Boolean getSaved(){return SAVED;}
    public Boolean getSeen(){return SEEN;}
    public Boolean getLiked(){return LIKED;}

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
                SAVED = false;
                LIKED = false;
                SEEN = false;
            }

        //Failed to Parse ResultSet
        }catch(SQLException e){
            log.severe("Failed Retrieve Values UserData ResultSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
