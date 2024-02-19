package net.tnn1nja.movieNight.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static net.tnn1nja.movieNight.Main.log;

public class database {

    private static Connection connection;

    public static void setup(){
        String dbUrl = "jdbc:sqlite:movies.db";
        try {
            connection = DriverManager.getConnection(dbUrl);
            log.info("Database Connection Esablished");

            connection.close();
            log.info("Database Connection Closed");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
