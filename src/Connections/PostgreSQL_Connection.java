package Connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL_Connection {
    private final String URL = "jdbc:postgresql://localhost:5432/almacenes";
    private final String USER = "postgres";
    private final String PASSWORD = "abc123.";
    private static Connection connection;

    private PostgreSQL_Connection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error en la conexion con la base datos postgre");
            e.printStackTrace();
        }
    }

    public static Connection getPostgreSQLConnection (){
        if (connection == null) {
            new PostgreSQL_Connection();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexión a PostgreSQL cerrada con éxito.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión con PostgreSQL:");
                e.printStackTrace();
            }
        }
    }
}
