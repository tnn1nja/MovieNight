package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static net.tnn1nja.movieNight.Main.db;
import static net.tnn1nja.movieNight.Main.log;

public class Search {

    //Get Films Containing Inputted String
    public Film[] byString(String searchData){

        //Retrieve Films Containing Inputted String
        ResultSet rs = db.query("SELECT FilmID FROM Films WHERE lower(Title) LIKE '%" +
                searchData.toLowerCase() + "%'");

        //Add All ID's to a List
        ArrayList<Integer> ids = new ArrayList<Integer>();
        try {
            while (rs.next()) {
                ids.add(rs.getInt("FilmID"));
            }
        }catch(SQLException e){
            log.severe("Failed to make search query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        //Construct Film for Each ID
        Film[] films = new Film[ids.size()];
        for(int i=0; i<films.length; i++){
            films[i] = Film.getFilm(ids.get(i));
        }

        //Return
        return films;

    }

}
