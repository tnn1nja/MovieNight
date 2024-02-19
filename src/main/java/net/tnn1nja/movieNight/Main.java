package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.utils.logger.LoggerUtils;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.tnn1nja.movieNight.data.database;

public class Main {

    //Constants, Variables and git Objects
    public static Logger log = Logger.getLogger("mainLogger");
    public static String mainPath = "D:/Coding/WBS/MovieNight/data";

    //On Program Start
    public void onStart(){

        //Setup Logger
        LoggerUtils.setup(log, Level.FINEST, mainPath);
        log.info("Logger Started.");
        database.setup();

    }

}