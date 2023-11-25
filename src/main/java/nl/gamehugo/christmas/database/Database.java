package nl.gamehugo.christmas.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;
    private static Database instance;

    String ip;
    int port;
    String database;
    String user;
    String password;

    private final TreeDAO treeDAO;

    public Database(String ip, int port, String database, String user, String password) {
        instance = this;
        treeDAO = new TreeDAO();
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public void connect() {
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

        // Create the tables if they don't exist
        treeDAO.createTable();
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

    public static Database getInstance() {
        return instance;
    }

    public TreeDAO getTreeDAO() {
        return treeDAO;
    }
}
