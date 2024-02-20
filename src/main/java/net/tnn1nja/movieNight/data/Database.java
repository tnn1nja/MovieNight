package net.tnn1nja.movieNight.data;

import java.sql.*;

import static net.tnn1nja.movieNight.Main.log;

public class Database {

    private final String dbUrl = "jdbc:sqlite:movies.db";
    private Connection conn;


    public void connect(){
        try {
            conn = DriverManager.getConnection(dbUrl);
            log.info("Database Connection Established");
        }catch(SQLException e){
            log.severe("Failed to Establish Database Connection");
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            conn.close();
            log.info("Database Connection Closed");
        }catch(SQLException e){
            log.severe("Failed to Close Database Connection");
            e.printStackTrace();
        }
    }

    public void run(String prompt){
        try {
            Statement s = conn.createStatement();
            s.execute(prompt + ";");

        }catch(SQLException e){
            log.severe("SQL Command Failed - SQLException");
            e.printStackTrace();
        }
    }

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
}
