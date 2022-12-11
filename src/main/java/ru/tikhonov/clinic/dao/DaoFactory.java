package ru.tikhonov.clinic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {

    public static Connection getConnection() {
        String url = "jdbc:sqlserver://localhost;databaseName=Clinic";
        String username = "sa";
        String password = "sa";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,username, password);
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return connection;
    }
}
