package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.utils.LogHandler;

import java.util.logging.Logger;

public class Main {

    Logger log = Logger.getLogger("mainLogger");

    public void onStart(){

        LogHandler.setup(log);

        log.info("Info Log!");
        log.warning("Warning Log!");
        log.severe("Severe Log!");
        log.fine("Debug Log!");

        System.out.println("Hello World");
    }

}