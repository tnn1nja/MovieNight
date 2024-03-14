package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.data.objects.Film;
import net.tnn1nja.movieNight.data.objects.Genre;
import net.tnn1nja.movieNight.data.objects.Provider;
import net.tnn1nja.movieNight.logic.Search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Display {

    //Colors
    private static final String GREEN  = "\u001B[32m";
    private static final String WHITE  = "\u001B[97m";

    //Take Input
    public String getInput(String prompt){
        Scanner s = new Scanner(System.in);
        System.out.print(prompt);
        return s.next();
    }

    //Program Demo Method (Changed Per Test)
    public void demo(){
        String prompt = getInput("Enter Search Data: ");
        beautyFilmOut(Search.byString(prompt));
    }

    //Film Array Console Output
    public void beautyFilmOut(Film[] films){

        //Line Break
        System.out.println("\n------------------\n");

        //For Each Film
        for(Film f: films){

            //Format Providers
            int[] providers = f.getProviders();
            ArrayList<String> strProv = new ArrayList<String>();
            for(int i: providers){
                strProv.add(Provider.getIndex().get(i));
            }

            //Format Genres
            int[] genres = f.getGenres();
            ArrayList<String> strGen = new ArrayList<String>();
            for(int i: genres){
                strGen.add(Genre.get(i));
            }

            //Output Data
            System.out.println(GREEN + "ID: " + WHITE + f.getID());
            System.out.println(GREEN + "Title: " + WHITE + f.getTitle());
            System.out.println(GREEN + "Synopsis: \n" + WHITE + f.getSynopsis());
            System.out.println(GREEN + "Year: " + WHITE + f.getYear());
            System.out.println(GREEN + "Rating:  " + WHITE + f.getRating() + "/100 ");
            System.out.println(GREEN + "Genres: " + WHITE + strGen);
            System.out.println(GREEN + "TMDBID: " + WHITE + f.getTMDBID());
            System.out.println(GREEN + "Director: " + WHITE + f.getDirector());
            System.out.println(GREEN + "Cast: " + WHITE + Arrays.toString(f.getCast()));
            System.out.println(GREEN + "Saved: " + WHITE + f.getSaved());
            System.out.println(GREEN + "Seen: " + WHITE + f.getSeen());
            System.out.println(GREEN + "Liked: " + WHITE + f.getLiked());
            System.out.println(GREEN + "Providers: " + WHITE + strProv);

            //Line Break
            System.out.println("------------------\n");

        }
    }
}
