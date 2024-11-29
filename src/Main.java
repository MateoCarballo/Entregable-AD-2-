import Connections.MySQL_Connection;
import Connections.PostgreSQL_Connection;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        App app = new App(new Modelo(MySQL_Connection.getMySQLConnection(), PostgreSQL_Connection.getPostgreSQLConnection()));

        try {
            app.iniciarApp();
        } catch (IOException e) {
            System.out.println("Error del tipo IOExcepcion");
            e.printStackTrace();
        }finally{
            try {
                app.getM().modeloConnectionMySQL().close();
                app.getM().modeloConnectionPostgre().close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar alguna de las conexiones");
                e.printStackTrace();
            }
        }
    }
}