package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Film;
import net.tnn1nja.movieNight.data.objects.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static net.tnn1nja.movieNight.Main.*;

public class Spotlight {

    public static Film getSuggestion(){

        //Create a List of Genres
        ArrayList<Integer> Genres = new ArrayList<Integer>();
        Genres.add(Genre.getRandom());

        //Extract Genres
        ResultSet rs = db.query("SELECT Genres FROM Films,UserData " +
                "WHERE Films.FilmID = UserData.FilmID");
        try{
            while(rs.next()) {
                String stringGenres = rs.getString("Genres");
                int[] genresArray = Film.formatGenres(stringGenres);
                for(int i: genresArray){
                    Genres.add(i);
                }
            }
        }catch(SQLException e){
            log.severe("Failed to extract Genres in Spotlight: " + e.getMessage());
            e.printStackTrace();
        }

        //Select Random Genre
        int chosenGenre = Genres.get(new Random().nextInt(Genres.size()));
        log.fine("Spotlight Chosen Genre: " + chosenGenre);

        //Extract Random Film ID
        String providerLimiter = config.getOwnedProviders()
                .toString().replace("[", "(").replace("]", ")");
        rs = db.query("" +
                "SELECT DISTINCT Films.FilmID FROM Films,ProvidersLink" +
                " WHERE Genres LIKE '%" + chosenGenre + "%'" +
                " AND Films.FilmID = ProvidersLink.FilmID" +
                " AND ProviderID in " + providerLimiter +
                " ORDER BY RANDOM() " +
                "LIMIT 1");

        try{
            return Film.getFilm(rs.getInt("FilmID"));
        }catch (SQLException e) {
            log.severe("Failed to Extract Film in Spotlight: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

}
