package net.tnn1nja.movieNight.data;

import java.sql.*;

import static net.tnn1nja.movieNight.Main.log;

public class Database {

    private final String dbUrl = "jdbc:sqlite:movies.db";


    private Connection getConnection() throws SQLException{
        Connection c = DriverManager.getConnection(dbUrl);
        log.info("Database Connection Established");
        return c;
    }

    private void closeConnection(Connection c) throws SQLException{
        c.close();
        log.info("Database Connection Closed");
    }


    public void run(String prompt){
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            s.execute(prompt + ";");
            closeConnection(c);

        }catch(SQLException e){
            log.severe("SQL Command Failed - SQLException");
            e.printStackTrace();
        }
    }

    public ResultSet query(String prompt){
        try {
            Connection c = getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(prompt + ";");
            closeConnection(c);
            return rs;

        }catch(SQLException e){
            log.severe("SQL Query Failed - SQLException");
            e.printStackTrace();
            return null;
        }
    }

}
