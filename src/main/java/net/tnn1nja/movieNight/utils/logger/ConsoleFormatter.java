package net.tnn1nja.movieNight.utils.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {

    //Color Codes
    private static final String RESET  = "\u001B[0m";
    private static final String GRAY  = "\u001B[37m";
    private static final String WHITE  = "\u001B[97m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String GREEN  = "\u001B[32m";
    private static final String BLUE = "\u001B[34m";

    //Main Inherited Formatter Method
    public String format(LogRecord record) {

        // Set Level Color Depending on Level
        String levelColor = switch (record.getLevel().getName()) {
            case("INFO") -> GREEN;
            case("WARNING") -> YELLOW;
            case("SEVERE") -> RED;
            default -> BLUE;
        };

        //Create and Format Time and Level Strings
        String time = GRAY + LoggerUtils.formatDate(record.getMillis()) + WHITE;
        String recordLevel = levelColor + LoggerUtils.formatLevel(record.getLevel()) + WHITE;
        String tab = LoggerUtils.tabDistance(LoggerUtils.formatLevel(record.getLevel()));

        //Return Final Concatenated String
        return WHITE + "[" + time + "] [" + recordLevel +"]" + tab + record.getMessage() + "\n" + RESET;
    }
}