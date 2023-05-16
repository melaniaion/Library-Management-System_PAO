package databaseConfig;

import java.sql.*;

public class DatabaseConfig {
    private static final String username = "root";
    private static final String password = "";
    private static final String  url = "jdbc:mysql://localhost:3307/library_management_system";
    private static Connection databaseConnection = null;

    private DatabaseConfig() {
    }

    public static Connection getDatabaseConnection() {
        try {
            if (databaseConnection == null || databaseConnection.isClosed()) {
                databaseConnection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return databaseConnection;
    }

    public static void closeDatabaseConnection() {
        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
