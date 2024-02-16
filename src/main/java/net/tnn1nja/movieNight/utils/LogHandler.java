package net.tnn1nja.movieNight.utils;

import java.util.logging.*;

public class LogHandler extends Formatter {

    public static void setup(Logger log){
        //Base Settings
        log.setUseParentHandlers(false);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new LogHandler());
        log.addHandler(ch);
    }

    @Override
    public String format(LogRecord record) {
        //Create and Format Strings
        String recordLevel = formatLevel(record.getLevel());

        //Return Final Concatenated String
        return "[" + recordLevel + "] " + record.getMessage() + "\n";
    }

    //Remaps Fine, Finer and Finest Levels to "Debug"
    public static String formatLevel(Level recordLevel){
        if(recordLevel.intValue() < Level.INFO.intValue()){
            return "DEBUG";
        }else{
            return recordLevel.getName();
        }
    }
}
