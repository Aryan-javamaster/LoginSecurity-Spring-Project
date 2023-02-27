package com.login.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class database {
    private static Connection connection = null;
    private static boolean connect = false;
    private static Statement stmt = null;

    private static boolean connect() {

        boolean temp = false;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://10.0.0.11:5432/LoginServer", "root", "root");
            connection.setAutoCommit(false);
            if (connection != null) {
                temp = true;

            } else {
                temp = false;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        connect = true;
        return temp;
    }

    private static void disconnect() throws SQLException {
        connection.close();
        connect = false;
    }

    // one time use method
   /*
    public static boolean createTable() throws SQLException {
        database.connect();
        if (connect == true) {
            stmt = connection.createStatement();
            String sql = " CREATE TABLE UserData " +
                    "NAME VARCHAR(255) NOT NULL," + "PASSWORD VARCHAR(255) NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
            database.disconnect();
            return true;
        }
        return false;
    }
 */
    public static boolean commit(String email, String password) throws SQLException {

        boolean done = false;
        database.connect();
        if (connect == true) {
            stmt = connection.createStatement();
            String sql = "INSERT INTO userdata (EMAIL,PASSWORD)" + "VALUES('"+ email
                    + "','" + password + "');";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            database.disconnect();
            done = true;
            
        } else {
            done = false;
        }
        return done;
    }

    public static String get(String email) throws SQLException {
        String result = "";
        database.connect();
        stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT password FROM UserData WHERE email='"+ email +"'");
        while (rs.next()) {
            String password = rs.getString("password");
            result = password;
                    
        }
        disconnect();
        return result;
    }

    public static boolean delete() throws SQLException {
        connect();
        if (connect == true) {
            stmt = connection.createStatement();
            String sql = "DELETE FROM userdata";
            stmt.executeUpdate(sql);
            stmt.close();
            connection.commit();
            database.disconnect();
            return true;
        } else {
            return false;
        }
    }
}
