package net.tnn1nja.movieNight.data;

import java.sql.*;

import static net.tnn1nja.movieNight.Main.log;

public class Database {

    //Variables and Constants
    private static final String dbUrl = "jdbc:sqlite:movies.db";
    private Connection conn;


    //Initialise Database Connection.
    public void connect(){
        try {
            conn = DriverManager.getConnection(dbUrl);
            log.info("Database Connection Established");
            run("PRAGMA foreign_keys=ON");
        }catch(SQLException e){
            log.severe("Failed to Establish Database Connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Close Database Connection
    public void close(){
        try {
            conn.close();
            log.info("Database Connection Closed");
        }catch(SQLException e){
            log.severe("Failed to Close Database Connection: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //Run SQL Command With Unhandled SQLException
    public void runUnhandled(String prompt) throws SQLException{
        Statement s = conn.createStatement();
        log.finest("SQL Command Running: " + prompt);
        s.execute(prompt + ";");
        log.finer("SQL Command Successfully Issued");
    }

    //Run SQL Command
    public void run(String prompt){
        try {
            runUnhandled(prompt);
        }catch(SQLException e){
            //Catch Unique Constraint Violation From API Class
            if(e.getMessage().contains("CONSTRAINT_UNIQUE")){
                log.finer("SQL Command Ignored - Duplicate Record Not Added.");
            }else {
                log.warning("SQL Command Failed - SQLException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //Get Results for SQL Query;
    public ResultSet query(String prompt){
        try {
            Statement s = conn.createStatement();
            log.finest("SQL Query Running: " + prompt);
            ResultSet rs = s.executeQuery(prompt + ";");
            log.finer("SQL Query Successfully Issued");
            return rs;

        } catch (SQLException e) {
            log.warning("SQL Query Failed - SQLException: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    //Create Tables (Hardcoded)
    public void initialise(){
        //Films Table
        run("CREATE TABLE IF NOT EXISTS Films (" +
                "FilmID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title VARCHAR(128)," +
                "Synopsis TEXT," +
                "Year INTEGER DEFAULT -1," +
                "Rating INTEGER DEFAULT -1," +
                "Genres VARCHAR(64)," +
                "TmdbID VARCHAR(64) DEFAULT -1,"+
                "UNIQUE(Title,Synopsis,Year,Rating,Genres,TmdbID)" +
                ")");
        //UserData Table
        run("CREATE TABLE IF NOT EXISTS UserData (" +
                "UserDataID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER DEFAULT -1," +
                "Saved BOOLEAN," +
                "Liked BOOLEAN," +
                "Seen BOOLEAN," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID) ON DELETE CASCADE," +
                "UNIQUE(FilmID)" +
                ")");
        //Providers Table
        run("CREATE TABLE IF NOT EXISTS Providers (" +
                "ProviderID INTEGER PRIMARY KEY," +
                "Name VARCHAR(64)," +
                "URL VARCHAR(256)," +
                "ApiTag VARCHAR (32)," +
                "UNIQUE(Name,URL,ApiTag)" +
                ")");
        //People Table
        run("CREATE TABLE IF NOT EXISTS People (" +
                "PersonID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(128)," +
                "UNIQUE(Name)" +
                ")");
        //ProvidersLink Table
        run("CREATE TABLE IF NOT EXISTS ProvidersLink (" +
                "ProviderLinkID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER DEFAULT -1," +
                "ProviderID INTEGER DEFAULT -1," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID) ON DELETE CASCADE," +
                "FOREIGN KEY (ProviderID) REFERENCES Providers(ProviderID) ON DELETE CASCADE," +
                "UNIQUE(FilmID,ProviderID)" +
                ")");
        //PRFLink Table
        run("CREATE TABLE IF NOT EXISTS PRFLink (" +
                "PRFLinkID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER DEFAULT -1," +
                "PersonID INTEGER DEFAULT -1," +
                "Role INTEGER DEFAULT -1," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID) ON DELETE CASCADE," +
                "FOREIGN KEY (PersonID) REFERENCES People(PersonID) ON DELETE CASCADE," +
                "UNIQUE(FilmID,PersonID,Role)" +
                ")");

        //Logging
        log.info("Database Tables Successfully Initialised");
    }
}
