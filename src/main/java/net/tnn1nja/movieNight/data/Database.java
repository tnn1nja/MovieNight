package net.tnn1nja.movieNight.data;

import java.sql.*;

import static net.tnn1nja.movieNight.Main.log;

public class Database {

    //Variables and Constants
    private static final String dbUrl = "jdbc:sqlite:movies.db";
    private Connection conn;


    //Initial Database Connection.
    public void connect(){
        try {
            conn = DriverManager.getConnection(dbUrl);
            log.info("Database Connection Established");
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


    //Run SQL Command
    public void run(String prompt){
        try {
            Statement s = conn.createStatement();
            s.execute(prompt + ";");
            log.fine("SQL Command Successfully Issued");

        }catch(SQLException e){
            log.severe("SQL Command Failed - SQLException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Get Results for SQL Query;
    public ResultSet query(String prompt){
        try {
            Statement s = conn.createStatement();
            log.fine("SQL Query Successfully Issued");
            return s.executeQuery(prompt + ";");

        } catch (SQLException e) {
            log.severe("SQL Query Failed - SQLException: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    //Generate Tables
    public void initialise(){
        //Set Constants
        run("PRAGMA foreign_keys=ON");
        //Films Table
        run("CREATE TABLE IF NOT EXISTS Films (" +
                "FilmID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title VARCHAR(128)," +
                "Synopsis TEXT," +
                "Year INTEGER DEFAULT -1," +
                "Cover VARCHAR(256)," +
                "Rating INTEGER DEFAULT -1," +
                "Genre VARCHAR(64)," +
                "TmdbID INTEGER DEFAULT -1"+
                ")");
        //UserData Table
        run("CREATE TABLE IF NOT EXISTS UserData (" +
                "UserDataID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER DEFAULT -1," +
                "Saved BOOLEAN," +
                "Liked BOOLEAN," +
                "Seen BOOLEAN," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID)" +
                ")");
        //Providers Table
        run("CREATE TABLE IF NOT EXISTS Providers (" +
                "ProviderID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(64)," +
                "URL VARCHAR(256)," +
                "Logo VARCHAR (256)" +
                ")");
        //People Table
        run("CREATE TABLE IF NOT EXISTS People (" +
                "PersonID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(128)" +
                ")");
        //ProvidersLink Table
        run("CREATE TABLE IF NOT EXISTS ProvidersLink (" +
                "ProviderLinkID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER DEFAULT -1," +
                "ProviderID INTEGER DEFAULT -1," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID)," +
                "FOREIGN KEY (ProviderID) REFERENCES Providers(ProviderID)" +
                ")");
        //PRFLink Table
        run("CREATE TABLE IF NOT EXISTS PRFLink (" +
                "PRFLinkID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER DEFAULT -1," +
                "PersonID INTEGER DEFAULT -1," +
                "Role INTEGER DEFAULT -1," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID)," +
                "FOREIGN KEY (PersonID) REFERENCES People(PersonID)" +
                ")");

        log.info("Database Tables Successfully Initialised");
    }
}
