package net.tnn1nja.movieNight.logic;

import net.tnn1nja.movieNight.data.objects.Provider;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static net.tnn1nja.movieNight.Main.*;

public class ReturnDataHandler {

    //Add Custom Film to the Database
    public static boolean addFilm(String Title, String Synopsis, String Year, String Rating, int Genre,
                              String Director, String Cast, String CoverPath){
        //Parse Cast
        String[] parsedCast = Cast.split(",");

        //Parse Integers
        int parsedYear = -1;
        int parsedRating = -1;
        try{
            parsedYear = Integer.parseInt(Year);
            parsedRating = Integer.parseInt(Rating);
            log.fine("Year and Rating Successfully parsed");
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
        log.finer("Return Values Within Range.");

        //Validate Cover
        if(!(new File(mainPath + "\\media\\film\\" + CoverPath  + ".jpg").exists())){
            log.info("File Doesn't Exist.");
            return false;
        }
        log.finer("Return File Found.");


        //Film Insert Command
        db.run("INSERT INTO Films(Title, Synopsis, Year, Rating, Genres, TmdbID) VALUES(" +
                "'" + Title + "'," +
                "'" + Synopsis + "'," +
                parsedYear + "," +
                parsedRating + "," +
                "'" + Genre + "'," +
                "'" + CoverPath + "')");

        //Get FilmID
        String FilmID = null;
        ResultSet rs = db.query("SELECT FilmID From Films WHERE TmdbID ='" + CoverPath + "'");
        try {
            FilmID = rs.getString("FilmID");
        } catch (SQLException e) {
            log.severe("Failed to Insert Film: " + e.getMessage());
            e.printStackTrace();
        }

        //ProviderLink Insert Command
        db.run("INSERT INTO ProvidersLink(FilmID, ProviderID) VALUES(" + FilmID + ", " +
                Provider.HOME.getID() + ")");

        //People Insert Command
        for (String castMember : parsedCast) {
            db.run("INSERT INTO People(Name) VALUES('" + castMember + "')");
            db.run("INSERT INTO PRFLink(FilmID, PersonID, Role) VALUES(" + FilmID + "," +
                    "(SELECT PersonID FROM People WHERE Name = '" + castMember + "'), 0)");
        }
        db.run("INSERT INTO PEOPLE(Name) VALUES('" + Director + "')");
        db.run("INSERT INTO PRFLink(FilmID, PersonID, Role) VALUES(" + FilmID + "," +
                "(SELECT PersonID FROM People WHERE Name = '" + Director + "'), 1)");


        //Success
        log.finer("Custom Film Successfully Inputted");
        return true;

    }

    //Remove Film From the Database
    public static boolean removeFilm(int id){
        try {
            db.runUnhandled("DELETE FROM Films WHERE FilmID=" + id);
            return true;
        }catch(SQLException e){
            log.warning("Failed to delete Film: " + e.getMessage());
            return false;
        }
    }

}
