package net.tnn1nja.movieNight.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;

import static net.tnn1nja.movieNight.Main.log;
import static net.tnn1nja.movieNight.Main.mainPath;

public class UserConfig {

    //Constants
    private final Properties config = new Properties();
    private final String filePath = mainPath + "\\config.properties";

    //Stored Variables
    public ArrayList<Integer> ownedProviders = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5));
    public String email = "Unknown";
    public String logLevel = "info";


    //Create Config File
    public void createIfMissing(){
        if(!(new File(filePath).exists())) {
            log.info("Config File Missing, Creating...");

            config.setProperty("ownedProviders", ownedProviders.toString());
            config.setProperty("email", email);
            config.setProperty("logLevel", logLevel);

            save();
        }else {
            log.fine("Config File Found, Skipping...");
        }
    }

    //Save Config File
    public void save(){
        try{
            FileOutputStream fos = new FileOutputStream(filePath);
            config.save(fos, "Movie Night - User Preferences.");
            log.info("Successfully Saved Config File");
        }catch (IOException e){
            log.severe("Failed to Save Config File: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Load Config File
    public void load(){
        try{
            FileInputStream fis = new FileInputStream(filePath);
            config.load(fis);
            log.info("Successfully Loaded Config File");
        }catch (IOException e){
            log.severe("Failed to Load Config File: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
