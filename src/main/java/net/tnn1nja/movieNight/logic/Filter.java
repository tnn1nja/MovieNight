package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Film;

import java.util.ArrayList;
import java.util.Arrays;

public class Filter {

    //Limit By Year
    public static Film[] stripBelowYear(Film[] Films, int Value) {
        ArrayList<Film> output = new ArrayList<Film>();
        for (Film f: Films){
            if(f.getYear()>Value){
                output.add(f);
            }
        }
        return output.toArray(new Film[0]);
    }

    //Limit by Rating
    public static Film[] stripBelowRating(Film[] Films, int Value) {
        ArrayList<Film> output = new ArrayList<Film>();
        for (Film f: Films){
            if(f.getRating()>Value){
                output.add(f);
            }
        }
        return output.toArray(new Film[0]);
    }

    //Remove Genre
    public static Film[] stripGenre(Film[] Films, int Genre) {
        ArrayList<Film> output = new ArrayList<Film>();
        for(Film f: Films){
            if(!(Arrays.asList(f.getGenres()).contains(Genre))){
                output.add(f);
            }
        }
        return output.toArray(new Film[0]);
    }

    //Remove Previously Seen
    public static Film[] stripSeen(Film[] Films) {
        ArrayList<Film> output = new ArrayList<Film>();
        for(Film f: Films){
            if(!(f.getSeen())){
                output.add(f);
            }
        }
        return output.toArray(new Film[0]);
    }

}

