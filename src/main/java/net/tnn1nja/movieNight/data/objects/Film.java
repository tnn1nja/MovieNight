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
    private int ID;
    private int YEAR;
    private int RATING;
    private int[] GENRES;
    private String TITLE;
    private String SYNOPSIS;
    private String TMDBID;

    private String DIRECTOR;
    private String[] CAST;

    private boolean SAVED = false;
    private boolean SEEN = false;
    private boolean LIKED = false;

    private int[] PROVIDERS;


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
    public int[] getProviders(){return PROVIDERS;}

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
            }

        //Failed to Parse ResultSet
        }catch(SQLException e){
            log.severe("Failed Retrieve Values UserData ResultSet: " + e.getMessage());
            e.printStackTrace();
        }
    }

}