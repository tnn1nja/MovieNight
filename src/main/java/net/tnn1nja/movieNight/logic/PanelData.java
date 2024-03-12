package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Film;
import net.tnn1nja.movieNight.data.objects.Genre;
import net.tnn1nja.movieNight.data.objects.Provider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static net.tnn1nja.movieNight.Main.*;

public class PanelData{

    //Attributes
    private final Film[] FILMS;
    private final String TITLE;

    //Constructor
    private PanelData(Film[] Films, String Title){
        FILMS = Films;
        TITLE = Title;
        log.finer("PanelData Object Constructed.");
    }

    //Getters
    public Film[] getFilms(){ return FILMS;}
    public String getTitle(){ return TITLE;}


    //Create Panel by Random Genre
    public static PanelData getGenreData(){
        //Choose Random Genre and Title
        int chosenGenre = Genre.getRandom();
        String title = Genre.get(chosenGenre) + " Films.";

        //Select 6 Random Films of That Genre
        ResultSet rs = db.query("" +
                "SELECT DISTINCT FilmID " +
                "From Films " +
                "WHERE Genres " +
                "LIKE '%" + chosenGenre + "%' " +
                "ORDER BY RANDOM()");

        //Add them to an Array
        Film[] films = rsToArray(rs);

        //Return Title and Films
        log.info("Random Genre Panel Data Generated");
        return new PanelData(films, title);

    }

    //Create Panel by Random Provider
    public static PanelData getProviderData(){
        //Choose Random Provider and Title
        Object[] ownedProviders = config.getOwnedProviders().toArray();
        int chosenProvider = (int) ownedProviders[new Random().nextInt(ownedProviders.length-1)];

        String title = "Films from " + Provider.getIndex().get(chosenProvider) + ".";

        //Select 6 Random Films of That Genre
        ResultSet rs = db.query("" +
                "SELECT DISTINCT Films.FilmID " +
                "FROM Films,ProvidersLink " +
                "WHERE Films.FilmID = ProvidersLink.FilmID " +
                "AND ProvidersLink.ProviderID = " + chosenProvider +
                " ORDER BY RANDOM()");

        //Add them to an Array
        Film[] films = rsToArray(rs);

        //Return Title and Films
        log.info("Random Provider Panel Data Generated");
        return new PanelData(films, title);

    }

    //Create Panel by Best Rated
    public static PanelData getBestRatedData(){
        ResultSet rs = db.query("" +
                "SELECT FilmID " +
                "From Films " +
                "ORDER BY(Rating) " +
                "DESC " +
                "LIMIT 100");

        Film[] films = rsToArray(rs);

        log.info("Best Rated Panel Data Generated");
        return new PanelData(films, "Best Rated.");
    }

    //Create Panel by Not Seen
    public static PanelData getUnseenData(){
        ResultSet rs = db.query("" +
                "SELECT FilmID " +
                "FROM Films " +
                "WHERE FilmID NOT IN (SELECT FilmID FROM UserData) " +
                "ORDER BY RANDOM() " +
                "LIMIT 100");

        Film[] films = rsToArray(rs);

        log.info("Unseen Panel Data Generated");
        return new PanelData(films, "New to You.");
    }



    //ResultSet to Array
    private static Film[] rsToArray(ResultSet rs){
        ArrayList<Film> films = new ArrayList<Film>();
        //Add each Result to List
        try{
            while(rs.next()){
                films.add(Film.getFilm(rs.getInt("FilmID")));
            }

        //Log Failure
        }catch(SQLException e){
            log.severe("Failed to Create Browse Panel: " + e.getMessage());
            e.printStackTrace();
        }

        //Cast to Array and Return
        return films.toArray(new Film[0]);
    }
}
