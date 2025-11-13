// Class purpose: Provides a single method to get a JDBC Connection to the DB.
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // TODO: Change credentials to your real Postgres settings when ready.
    private static final String URL = "jdbc:postgresql://localhost:5432/ems_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
