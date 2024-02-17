package net.tnn1nja.movieNight.utils.logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LoggerUtils  {

    public static void setup(Logger log, Level inputLevel, String path){
        //Base Settings
        log.setUseParentHandlers(false);
        log.setLevel(inputLevel);

        //Console Formatting
        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(inputLevel);
        ch.setFormatter(new ConsoleFormatter());
        log.addHandler(ch);

        //File Formatting
        try {
            FileHandler fh = new FileHandler(path + "/logfile.txt");
            fh.setLevel(inputLevel);
            fh.setFormatter(new FileFormatter());
            log.addHandler(fh);
        }catch(IOException e){
            e.printStackTrace();
        }
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
