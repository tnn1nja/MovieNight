package net.tnn1nja.movieNight.utils;

import java.util.logging.Logger;

public class LogHandler {
    static Logger log = Logger.getLogger("mainLogger");

    public static void setup(){
        log.info("Hello World!");
    }
}
