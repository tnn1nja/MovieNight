package net.tnn1nja.movieNight.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static net.tnn1nja.movieNight.Main.log;
import static net.tnn1nja.movieNight.Main.mainPath;

public class UserConfig {

    //Constants
    private final Properties config = new Properties();
    private final String filePath = mainPath + "\\config.properties";


    //Testing Method
    public void test(){
        config.setProperty("One", "1");
        config.setProperty("Two", "2");
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
