package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.utils.logger.LoggerUtils;

import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tnn1nja.movieNight.data.Database;

public class Main {

    //Constants
    public static String mainPath = "D:/Coding/WBS/MovieNight/data";
    public static Logger log = Logger.getLogger("mainLogger");

    //Singletons
    public static Database db = new Database();

    //On Program Start
    public void onStart(){

        //Setup
        LoggerUtils.setup(log, Level.FINEST, mainPath);
        log.info("Logger Started.");
        db.connect();

        ResultSet rs = db.query("SELECT * FROM Test");
        try {
            System.out.println(rs.getInt("TestID"));
        }catch(Exception e){
            e.printStackTrace();
        }

        db.close();

    }

}