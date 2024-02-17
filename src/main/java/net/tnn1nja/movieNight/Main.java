package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.utils.logger.LoggerUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    //Constants, Variables and Objects
    public static Logger log = Logger.getLogger("mainLogger");
    public static String mainPath = "C:/users/theno/desktop";

    //On Program Start
    public void onStart(){

        LoggerUtils.setup(log, Level.FINEST, mainPath);

        log.info("Info Log!");
        log.warning("Warning Log!");
        log.severe("Severe Log!");
        log.fine("Debug Log!");

        System.out.println("Hello World");
    }

}