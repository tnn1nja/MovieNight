package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import static net.tnn1nja.movieNight.Main.db;
import static net.tnn1nja.movieNight.Main.log;

public class Spotlight {

    public static Film getSuggestion(){

        //Create a List of Genres
        ArrayList<Integer> Genres = new ArrayList<Integer>();

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

        log.info("Genres: " + Genres);

        return null;

    }

}
