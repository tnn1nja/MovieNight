package net.tnn1nja.movieNight.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LogHandler extends Formatter {

    //Color Codes
    private static final String RESET  = "\u001B[0m";
    private static final String GRAY  = "\u001B[37m";
    private static final String WHITE  = "\u001B[97m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";

    public static void setup(Logger log){
        //Base Settings
        log.setUseParentHandlers(false);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new LogHandler());
        log.addHandler(ch);
    }

    //Main Inherited Formatter Method
    public String format(LogRecord record) {
        // Set Level Color Depending on Level
        String levelColor = switch (record.getLevel().getName()) {
            case("INFO") -> GREEN;
            case("WARNING") -> YELLOW;
            case("SEVERE") -> RED;
            default -> BLUE;
        };

        //Create and Format Strings
        String time = GRAY + formatDate(record.getMillis()) + WHITE;
        String recordLevel = formatLevel(record.getLevel()) + WHITE;
        String tab = tabDistance(formatLevel(record.getLevel()));


        //Return Final Concatenated String
        return WHITE + "[" + time + "] [" + recordLevel +"]" + tab + record.getMessage() + "\n" + RESET;
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

    //Get Tab Distance
    public static String tabDistance(String levelName){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 8- levelName.toCharArray().length; i++){
            builder.append(" ");
        }
        builder.append("- ");
        return builder.toString();
    }
}
