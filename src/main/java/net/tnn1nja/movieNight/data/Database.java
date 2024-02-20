package net.tnn1nja.movieNight.data;

import java.sql.*;

import static net.tnn1nja.movieNight.Main.log;

public class Database {

    //Variables and Constants
    private final String dbUrl = "jdbc:sqlite:movies.db";
    private Connection conn;


    //Initial database connection.
    public void connect(){
        try {
            conn = DriverManager.getConnection(dbUrl);
            log.info("Database Connection Established");
        }catch(SQLException e){
            log.severe("Failed to Establish Database Connection");
            e.printStackTrace();
        }
    }

    //Close Database Connection
    public void close(){
        try {
            conn.close();
            log.info("Database Connection Closed");
        }catch(SQLException e){
            log.severe("Failed to Close Database Connection");
            e.printStackTrace();
        }
    }


    //Run SQL Command
    public void run(String prompt){
        try {
            Statement s = conn.createStatement();
            s.execute(prompt + ";");

        }catch(SQLException e){
            log.severe("SQL Command Failed - SQLException");
            e.printStackTrace();
        }
    }

    //Get Results for SQL Query;
    public ResultSet query(String prompt){
        try {
            Statement s = conn.createStatement();
            return s.executeQuery(prompt + ";");

        } catch (SQLException e) {
            log.severe("SQL Query Failed - SQLException");
            e.printStackTrace();
            return null;
        }
    }


    //Generate Tables
    public void initialise(){
        run("PRAGMA foreign_keys=ON");
        run("CREATE TABLE IF NOT EXISTS Films (" +
                "FilmID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Title VARCHAR(128)," +
                "Synopsis TEXT," +
                "Year INTEGER," +
                "Cover VARCHAR(256)," +
                "Rating INTEGER," +
                "Genre INTEGER" +
                ")");
        run("CREATE TABLE IF NOT EXISTS UserData (" +
                "UserDataID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER," +
                "Saved BOOLEAN," +
                "Liked BOOLEAN," +
                "Seen BOOLEAN," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID)" +
                ")");
        run("CREATE TABLE IF NOT EXISTS Providers (" +
                "ProviderID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(64)," +
                "URL VARCHAR(256)," +
                "Logo VARCHAR (256)" +
                ")");
        run("CREATE TABLE IF NOT EXISTS People (" +
                "PersonID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name VARCHAR(128)" +
                ")");
        run("CREATE TABLE IF NOT EXISTS ProvidersLink (" +
                "ProviderLinkID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER," +
                "ProviderID INTEGER," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID)," +
                "FOREIGN KEY (ProviderID) REFERENCES Providers(ProviderID)" +
                ")");
        run("CREATE TABLE IF NOT EXISTS PRFLink (" +
                "PRFLinkID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FilmID INTEGER," +
                "PersonID INTEGER," +
                "Role INTEGER," +
                "FOREIGN KEY (FilmID) REFERENCES Films(FilmID)," +
                "FOREIGN KEY (PersonID) REFERENCES People(PersonID)" +
                ")");
        log.info("Database Tables Successfully Initialised");
    }
}
