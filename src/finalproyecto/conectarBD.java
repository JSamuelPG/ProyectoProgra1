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
     private String url = "jdbc:postgresql://monorail.proxy.rlwy.net:59878/railway";
    private Properties properties = new Properties();
    private static Connection conn = null;
    
    public conectarBD() {
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "KypePTpFZBvHeHdcVKcwUBapwVvuEqMg");
        
        try {
            conn = DriverManager.getConnection(url, properties);
        } catch (SQLException ex) {
            Logger.getLogger(conectarBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Connection getConnection() {
        if (conn == null) {
               conectarBD c = new conectarBD();
               return c.conn;
        }else {
            return conn ;
        }
    }
}
