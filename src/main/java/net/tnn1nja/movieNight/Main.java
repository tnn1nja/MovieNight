package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.data.APIs;
import net.tnn1nja.movieNight.data.objects.Film;
import net.tnn1nja.movieNight.utils.logger.LoggerUtils;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tnn1nja.movieNight.data.Database;

public class Main {

    //Constants
    public static String mainPath = "D:/Coding/WBS/MovieNight/data"; //RELATIVE PATH LATER

    //Modules and Singletons
    public static Database db = new Database();
    public static APIs api = new APIs();
    public static Logger log = Logger.getLogger("mnLogger");

    //On Program Start
    public static void onStart(){

        //Logger Setup
        LoggerUtils.setup(log, Level.INFO, mainPath);
        log.info("Logger Started.");

        //Database Setup
        db.connect();
        db.initialise();

        //Testing
        Film[] films = Film.getFilms(new int[]{1,2,3});
        for(Film f: films) {
            log.info("ID: " + f.getID());
            log.info("Title: " + f.getTitle());
            log.info("Synopsis: " + f.getSynopsis());
            log.info("Genres: " + Arrays.toString(f.getGenres()));
            log.info("Year: " + f.getYear());
            log.info("Rating: " + f.getRating());
            log.info("TMDBID: " + f.getTMDBID());
            log.info("Director:  " + f.getDirector());
            log.info("Cast: " + Arrays.toString(f.getCast()));
            log.info("Saved: " + f.getSaved());
            log.info("Seen: " + f.getSeen());
            log.info("Liked: " + f.getLiked());
            log.info("Providers: " + Arrays.toString(f.getProviders()));
            log.info("------------");
        }

        //Program End
        onClose();

    }

    //On Program End
    public static void onClose(){
        db.close();
        System.exit(0);
    }

}