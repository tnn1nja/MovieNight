package net.tnn1nja.movieNight.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static net.tnn1nja.movieNight.Main.log;
import static net.tnn1nja.movieNight.Main.mainPath;

public class UserConfig {

    //Constants
    private final Properties config = new Properties();
    private final String filePath = mainPath + "\\config.properties";


    //Stored Variables
    private HashSet<Integer> ownedProviders = new HashSet<Integer>(Arrays.asList(1,2,3,4,5));
    private String email = "Unknown";
    private Level logLevel = Level.INFO;

    //Setters and Getters
    public void setEmail(String newEmail){email = newEmail;}
    public void addOwnedProvider(int i){ownedProviders.add(i);}
    public void removeOwnedProvider(int i){ownedProviders.remove(i);}
    public String getEmail(){return email;}
    public HashSet<Integer> getOwnedProviders(){return ownedProviders;}
    public Level getLogLevel(){return logLevel;}


    //Create Config File
    public void createIfMissing(){
        if(!(new File(filePath).exists())) {
            log.info("Config File Missing, Creating...");

            config.setProperty("ownedProviders", ownedProviders.toString());
            config.setProperty("email", email);
            config.setProperty("logLevel", logLevel.getName());

            save();
        }else {
            log.fine("Config File Found, Skipping...");
        }
    }

    //Save Config File
    public void save(){
        //Save to Object
        config.setProperty("email", email);
        config.setProperty("ownedProviders", ownedProviders.toString());
        config.setProperty("logLevel", logLevel.getName());

        //Save to File
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
        //Load from File
        try{
            FileInputStream fis = new FileInputStream(filePath);
            config.load(fis);
            log.info("Successfully Loaded Config File");
        }catch (IOException e){
            log.severe("Failed to Load Config File: " + e.getMessage());
            e.printStackTrace();
        }

        //Assign Email
        email = config.getProperty("email");

        //Assign OwnedProvider
        String[] pb = config.getProperty("ownedProviders").replace("[", "").
                replace("]","").split(",");
        try {
            HashSet<Integer> opb = new HashSet<Integer>();
            for (String s : pb) {
                opb.add(Integer.parseInt(s.replace(" ", "")));
            }
            ownedProviders = opb;
        }catch(IllegalArgumentException ignored){}

        //Assign logLevel
        try {
            logLevel = Level.parse(config.getProperty("logLevel"));
        }catch(IllegalArgumentException ignored){}

    }

}
