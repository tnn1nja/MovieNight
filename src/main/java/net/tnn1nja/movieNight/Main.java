package net.tnn1nja.movieNight;

import net.tnn1nja.movieNight.data.APIs;
import net.tnn1nja.movieNight.data.UserConfig;
import net.tnn1nja.movieNight.data.objects.Film;
import net.tnn1nja.movieNight.logic.Filter;
import net.tnn1nja.movieNight.logic.PanelData;
import net.tnn1nja.movieNight.logic.Search;
import net.tnn1nja.movieNight.utils.logger.LoggerUtils;

import java.util.Arrays;
import java.util.logging.Logger;
import net.tnn1nja.movieNight.data.Database;

public class Main {

    //Constants
    public static String mainPath = "D:/Coding/WBS/MovieNight/data"; //RELATIVE PATH LATER

    //Singletons
    public static Logger log = Logger.getLogger("mnLogger");
    public static Database db = new Database();
    public static APIs api = new APIs();
    public static UserConfig config = new UserConfig();

    //On Program Start
    public static void onStart(){

        //Config Setup
        config.createIfMissing();
        config.load();

        //Logger Setup
        LoggerUtils.setup(log, config.getLogLevel(), mainPath);
        log.info("Logger Started.");
        log.warning("Any Invalid User Preferences Have Been Defaulted.");

        //Database Setup
        db.connect();
        db.initialise();

        //Testing
        PanelData pd = PanelData.getUnseenData();
        Film[] films = pd.getFilms();
        log.info("Title: " + pd.getTitle());
        for(int i=0;i<7;i++){
            Film f = films[i];
            log.info("Seen: " + f.getSeen() + ", Rating: " +
                    f.getRating() + ", Providers: " + Arrays.toString(f.getProviders()));
        }

        //Program End
        onClose();

    }

    //On Program End
    public static void onClose(){
        db.close();
        config.save();
        System.exit(0);
    }

}