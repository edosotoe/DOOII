package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "220687chris";

    private static DatabaseConnection instancia;
    private Connection conexion;

    private DatabaseConnection() throws SQLException {
        conexion = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return conexion;
    }
}