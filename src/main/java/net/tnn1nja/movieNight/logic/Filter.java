package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Film;

import java.util.ArrayList;

public class Filter {

    //Limit By Year
    public static Film[] stripBelowYear(Film[] Films, int Year) {
        ArrayList<Film> output = new ArrayList<Film>();
        for (Film f: Films){
            if(f.getYear()>Year){
                output.add(f);
            }
        }
        return output.toArray(new Film[0]);
    }

    //Limit by Rating
    public static Film[] stripBelowRating(Film[] Films, int Rating) {
        ArrayList<Film> output = new ArrayList<Film>();
        for (Film f: Films){
            if(f.getRating()>Rating){
                output.add(f);
            }
        }
        return output.toArray(new Film[0]);
    }

    //Remove Genre
    public static Film[] stripGenre(Film[] Films, int Genre) {
        ArrayList<Film> output = new ArrayList<Film>();
        for(Film f: Films){
            if(!(intArrContains(f.getGenres(),Genre))){
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


    //Int Array Contains
    private static boolean intArrContains(int[] array, int value){
        for(int i: array){
            if(i==value){
                return true;
            }
        }
        return false;
    }

}

