package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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

        //Sort Films By Index of Search String
        Arrays.sort(films, new StringIndexComparator(searchData));

        //Return
        return films;

    }


    //Film Object Comparator
    private static class StringIndexComparator implements Comparator<Film>{

        //Constants
        String data;

        //Constructor
        public StringIndexComparator(String data){
            this.data = data;
        }

        //Main Comparator
        @Override
        public int compare(Film f1, Film f2) {

            int f1Value = f1.getTitle().indexOf(data);
            int f2Value = f2.getTitle().indexOf(data);

            if(f1Value < f2Value) {
                return -1; //First Film comes First
            }else if (f1Value > f2Value) {
                return 1; //Second Film comes First
            }else {
                return 0; //Doesn't matter
            }

        }

    }

}
