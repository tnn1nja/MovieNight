package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.utils.logger.LoggerUtils;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.tnn1nja.movieNight.data.Database;

public class Main {

    //Constants
    public static String mainPath = "D:/Coding/WBS/MovieNight/data";
    public static Logger log = Logger.getLogger("mainLogger");

    //Singletons
    public static Database db;

    //On Program Start
    public void onStart(){

        //Setup
        LoggerUtils.setup(log, Level.FINEST, mainPath);
        log.info("Logger Started.");


        db.run("CREATE TABLE Test ( TestID INTEGER PRIMARY KEY AUTOINCREMENT");

    }

}