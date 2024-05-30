/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author patza
 */
public class conectarBD {
     private static final String URL = "jdbc:postgresql://monorail.proxy.rlwy.net:59878/railway";
    private static final String USER = "postgres";
    private static final String PASSWORD = "KypePTpFZBvHeHdcVKcwUBapwVvuEqMg";

    /**
     * Obtiene una nueva conexión a la base de datos.
     * @return una nueva conexión a la base de datos.
     * @throws SQLException si ocurre un error al conectar.
     */
    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", USER);
        properties.setProperty("password", PASSWORD);
        return DriverManager.getConnection(URL, properties);
    }
}
