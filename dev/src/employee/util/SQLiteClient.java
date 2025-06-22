package employee.util;
import java.sql.*;

public class SQLiteClient implements DataBaseClient {
    private static SQLiteClient instance;
    private Connection connection;

    private SQLiteClient(String url) {
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Successfully connected to the database.");
        } catch (SQLException e) {
            throw new RuntimeException("Unable to connect to SQLite database", e);
        }
    }

    public static synchronized SQLiteClient getInstance(String url) {
        if (instance == null) {
            instance = new SQLiteClient(url);
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null) connection.close();
        } catch (Exception e) {
            System.out.println("cannot close connection to dataBase because: " + e.getMessage());
        }
    }
}