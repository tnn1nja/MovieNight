package net.tnn1nja.movieNight.logic;

import static net.tnn1nja.movieNight.Main.log;

public class ReturnDataHandler {

    public static boolean addFilm(String Title, String Synopsis, String Year, String Rating, int Genre,
                              String Director, String Cast){
        //Parse Cast
        String[] parsedCast = Cast.split(",");

        //Parse Integers
        int parsedYear = -1;
        int parsedRating = -1;
        try{
            parsedYear = Integer.parseInt(Year);
            parsedRating = Integer.parseInt(Rating);
        }catch(NumberFormatException e){
            log.warning("Film Year or Rating cannot be parsed.");
            return false;
        }

        //Validate Integers
        if(parsedRating<0 || parsedRating >100 ||
                parsedYear < 1900 || parsedYear > 2050){
            log.warning("Inputted Values Out of Range.");
            return false;
        }

        return true;
    }

}
