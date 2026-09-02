package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton pattern: only one connection instance is ever created.
 * Update DB_URL / DB_USER / DB_PASS to match your local MySQL setup.
 */
public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "yourpassword"; // change this

    private static Connection connection;

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }
}
