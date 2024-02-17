package net.tnn1nja.movieNight.utils.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileFormatter extends Formatter {

    //Main Inherited Formatter Method
    public String format(LogRecord record) {

        //Create and Format Time and Level Strings
        String time = LoggerUtils.formatDate(record.getMillis());
        String recordLevel = LoggerUtils.formatLevel(record.getLevel());
        String tab = LoggerUtils.tabDistance(recordLevel);

        //Return Final Concatenated String
        return "[" + time + "] " + "[" + recordLevel + "]" + tab + record.getMessage() + "\n";

    }
}
