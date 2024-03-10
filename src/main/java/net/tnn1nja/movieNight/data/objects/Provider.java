package net.tnn1nja.movieNight.data.objects;

import java.util.ArrayList;

import static net.tnn1nja.movieNight.Main.log;

public class Provider {

    //Constants
    private static final ArrayList<String> ApiTags = new ArrayList<String>();
    public static final Provider NETFLIX = new Provider(1, "Netflix",
            "https://www.netflix.co.uk/", "netflix");
    public static final Provider DISNEY = new Provider (2, "Disney Plus",
            "https://www.disneyplus.com/", "disney");
    public static final Provider IPLAYER = new Provider (3, "BBC iPlayer",
            "https://bbc.co.uk/iplayer/", "iplayer");
    public static final Provider All4 = new Provider(4, "All 4",
            "https://www.channel4.com/","all4");
    public static final Provider HOME = new Provider(5, "Home",
            "Not Applicable", "home");

    //Attributes
    private final int ID;
    private final String NAME;
    private final String URL;
    private final String APITAG;

    //Constructor
    private Provider(int Id, String Name, String Url, String ApiTag){

        //Assign Values
        ID = Id;
        NAME = Name;
        URL = Url;
        APITAG = ApiTag;

        //Add Values to ApiTag Array
        if(!(ApiTag.equalsIgnoreCase("home"))) {
            ApiTags.add(ApiTag);
        }

        //Logging
        log.info("'" + NAME + "' Successfully Instantiated");

    }

    //Getters
    public int getID(){return ID;}
    public String getName(){return NAME;}
    public String getURL(){return URL;}
    public String getApiTag(){return APITAG;}

    public static String[] getApiTags(){return ApiTags.toArray(new String[0]);}

}
