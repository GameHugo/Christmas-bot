package nl.gamehugo.christmas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;

    public void connect(String ip, int port, String database, String user, String password) {
        // Check if the connection is already open
        if (isConnected()) {
            throw new IllegalStateException("Cannot connect to database because it is already connected.");
        }
        // Replace the placeholders with your actual database credentials
        String url = "jdbc:mysql://"+ip+":" + port + "/" + database;

        try {
            // Establish a connection to the MySQL server
            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Database connection established.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to connect to the database.", e);
        }
    }

    public void disconnect() {
        // Check if the connection is already closed (or was never opened)
        if (!isConnected()) {
            throw new IllegalStateException("Cannot close database because connection is not open.");
        }
        try {
            // Close the connection to the MySQL server
            connection.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to close database connection.", e);
        }
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        if (!isConnected()) throw new IllegalStateException("Could not get database connection because it is not open.");
        return connection;
    }
}
