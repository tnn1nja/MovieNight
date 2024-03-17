package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.data.objects.Film;
import net.tnn1nja.movieNight.data.objects.Genre;
import net.tnn1nja.movieNight.data.objects.Provider;
import net.tnn1nja.movieNight.logic.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static net.tnn1nja.movieNight.Main.*;

public class ConsoleDisplay {

    //Colors
    private static final String GREEN  = "\u001B[32m";
    private static final String WHITE  = "\u001B[97m";
    private static final String RED    = "\u001B[31m";

    //Program Demo Method
    public void demo(){

        //POPULATE
        if(ARG.equalsIgnoreCase("populate")) {
            api.populateDatabase();

        //SEARCH
        }else if(ARG.equalsIgnoreCase("search")) {
            String prompt = getInput("Enter Search Data: ");
            beautyFilmOut(Search.byString(prompt));
            getInput("Enter to Exit.");

        //SPOTLIGHT
        }else if(ARG.equalsIgnoreCase("spotlight")){
            beautyFilmOut(new Film[]{Spotlight.getSuggestion()});

        //FILTER
        }else if(ARG.equalsIgnoreCase("filter")){
            //Cast Owned Providers
            Integer[] prov = config.getOwnedProviders().toArray(new Integer[0]);
            int[] castProv = new int[prov.length];
            for(int i = 0; i<prov.length;i++){
                castProv[i] = prov[i];
            }

            String prompt = getInput("Enter Search Data: ");
            Film[] films  = Search.byString(prompt);
            beautyFilmOut(films);
            beautyFilmOut(
                    //Filter.stripBelowYear(films, 2022)
                    //Filter.stripBelowRating(films, 60)
                    //Filter.stripGenre(films,35)
                    //Filter.stripSeen(films)
                    Filter.limitProviders(films, castProv)
            );

        //BROWSE
        } else if(ARG.equalsIgnoreCase("browse")){
            //Variable
            PanelData p;

            //Genre Panel
            p = PanelData.getGenreData();
            System.out.println(RED + "Panel Title: " + WHITE + p.getTitle());
            beautyFilmOut(limitArr(p.getFilms()));
            //Provider Panel
            p = PanelData.getProviderData();
            System.out.println(RED + "Panel Title: " + WHITE + p.getTitle());
            beautyFilmOut(limitArr(p.getFilms()));
            //Rating Panel
            p = PanelData.getBestRatedData();
            System.out.println(RED + "Panel Title: " + WHITE + p.getTitle());
            beautyFilmOut(limitArr(p.getFilms()));
            //Unseen Panel
            p = PanelData.getUnseenData();
            System.out.println(RED + "Panel Title: " + WHITE + p.getTitle());
            beautyFilmOut(limitArr(p.getFilms()));

        //INPUT
        }else if(ARG.equalsIgnoreCase("input")){
            boolean b = ReturnDataHandler.addFilm(getInput("Enter a Title: "),
                    getInput("Enter Synopsis: "), getInput("Enter Year: "),
                    getInput("Enter Rating (/100): "), Integer.parseInt(getInput("Enter a Genre: ")),
                            getInput("Enter the Director: "), getInput("Enter the Cast: "),
                            getInput("Enter the Cover Files Name: "));

            if(b){
                log.info("Film Added to the Database.");
            }else{
                log.warning("Film Data Invalid, Rejected.");
            }
        }
    }

    //Take User Console Input
    public String getInput(String prompt){
        Scanner s = new Scanner(System.in);
        System.out.print(prompt);
        return s.nextLine();
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

    //Film Limiter
    public Film[] limitArr(Film[] films){
        if(films.length<7){
            return films;
        }else{
            Film[] limited = new Film[6];
            for(int i = 0; i<6; i++){
                limited[i] = films[i];
            }
            return limited;
        }
    }
}
