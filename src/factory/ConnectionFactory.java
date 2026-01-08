package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:sqlite:database/Libreria.db";

    private ConnectionFactory() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
