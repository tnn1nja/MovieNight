package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.utils.LogHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    //Log File Location
    static String mainPath = "C:/users/theno/desktop";

    Logger log = Logger.getLogger("mainLogger");

    public void onStart(){

        LogHandler.setup(log, Level.FINEST, mainPath);

        log.info("Info Log!");
        log.warning("Warning Log!");
        log.severe("Severe Log!");
        log.fine("Debug Log!");

        System.out.println("Hello World");
    }

}