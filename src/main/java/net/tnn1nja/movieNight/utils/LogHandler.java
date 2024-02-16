package net.tnn1nja.movieNight.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        String time = formatDate(record.getMillis());

        //Return Final Concatenated String
        return "[" + time + "] [" + recordLevel + "] " + record.getMessage() + "\n";
    }

    //Remaps Fine, Finer and Finest Levels to "Debug"
    public static String formatLevel(Level recordLevel){
        if(recordLevel.intValue() < Level.INFO.intValue()){
            return "DEBUG";
        }else{
            return recordLevel.getName();
        }
    }

    //Converts Unix Time Stamp to Readable Date.
    public static String formatDate(long millis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
        Date resultdate = new Date(millis);
        return dateFormat.format(resultdate);
    }
}
