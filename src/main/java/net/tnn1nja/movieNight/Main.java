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
        //db.connect();
        //db.initialise();

        //Testing
        Film testFilm = new Film(1, "Epic Romance", "Some people fight then fall in love", 2017,
                13, new int[]{12, 14}, "#1", false, false, false, "Famous Man",
                new String[]{"Actor Johnson", "John Actorson"}, new int[]{1});

        log.info(testFilm.TITLE);
        log.info(testFilm.SYNOPSIS);
        log.info(String.valueOf(testFilm.YEAR));
        log.info(String.valueOf(testFilm.RATING));
        log.info(Arrays.toString(testFilm.GENRES));
        log.info(testFilm.TMDBID);
        log.info(String.valueOf(testFilm.SAVED));
        log.info(String.valueOf(testFilm.LIKED));
        log.info(String.valueOf(testFilm.SEEN));
        log.info(testFilm.DIRECTOR);
        log.info(Arrays.toString(testFilm.CAST));
        log.info(Arrays.toString(testFilm.PROVIDERS));
        log.info(testFilm.getRelativeCoverPath());


        //Program End
        onClose();

    }

    //On Program End
    public static void onClose(){
        //db.close();
        System.exit(0);
    }

}