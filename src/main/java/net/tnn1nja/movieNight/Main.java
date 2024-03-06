package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.data.APIs;
import net.tnn1nja.movieNight.utils.logger.LoggerUtils;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.tnn1nja.movieNight.data.Database;

public class Main {

    //Constants
    public static String mainPath = "D:/Coding/WBS/MovieNight/data"; //RELATIVE PATH LATER

    //Modules/Singletons
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

        //APIs
        api.populateDatabase();

        //Program End
        onClose();

    }

    //On Program End
    public static void onClose(){
        db.close();
        System.exit(0);
    }

}