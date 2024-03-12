package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Film;

import java.util.ArrayList;

import static net.tnn1nja.movieNight.Main.log;

public class Filter {

    //Limit By Year
    public static Film[] stripBelowYear(Film[] Films, int Year) {
        //Add Films With Year More than Inputted
        ArrayList<Film> output = new ArrayList<Film>();
        for (Film f: Films){
            if(f.getYear()>Year){
                output.add(f);
            }
        }
        //Return New Array
        log.info("Stripped Films From Before " + Year);
        return output.toArray(new Film[0]);
    }

    //Limit by Rating
    public static Film[] stripBelowRating(Film[] Films, int Rating) {
        //Add Films With Rating More Than Inputted
        ArrayList<Film> output = new ArrayList<Film>();
        for (Film f: Films){
            if(f.getRating()>Rating){
                output.add(f);
            }
        }
        //Return New Array
        log.info("Stripped Films Rating Rating Below " + Rating);
        return output.toArray(new Film[0]);
    }

    //Remove Genre
    public static Film[] stripGenre(Film[] Films, int Genre) {
        //Add Films Without Inputted Genre
        ArrayList<Film> output = new ArrayList<Film>();
        for(Film f: Films){
            if(!(intArrContains(f.getGenres(),Genre))){
                output.add(f);
            }
        }
        //Return New Array
        log.info("Stripped Films Containing Genre " + Genre);
        return output.toArray(new Film[0]);
    }

    //Remove Previously Seen
    public static Film[] stripSeen(Film[] Films) {
        //Add Films Without Inputted Genre
        ArrayList<Film> output = new ArrayList<Film>();
        for(Film f: Films){
            if(!(f.getSeen())){
                output.add(f);
            }
        }
        //Return New Array
        log.info("Removed Seen Films");
        return output.toArray(new Film[0]);
    }

    //Limit Providers
    public static Film[] limitProviders(Film[] Films, int[] Providers) {
        ArrayList<Film> output = new ArrayList<Film>();

        for(Film f: Films){
            //Variables
            int[] filmProviders = f.getProviders();
            //Check if Film Contains One of the Providers
            boolean contains = false;
            for(int provider: Providers){
                if ((intArrContains(filmProviders, provider))) {
                    contains = true;
                    break;
                }
            }
            //If so, Add it
            if(contains){
                output.add(f);
            }
        }

        //Return Final Array
        log.info("Removed Deselected Providers.");
        return output.toArray(new Film[0]);
    }


    //Int Array Contains
    private static boolean intArrContains(int[] array, int value){
        //Check Each Item for Value, if found flag
        for(int i: array){
            if(i==value){
                return true;
            }
        }
        //Nothing Found
        return false;
    }

}

