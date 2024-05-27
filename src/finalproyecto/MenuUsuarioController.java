/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package finalproyecto;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author patza
 */

public class MenuUsuarioController implements Initializable {

    @FXML
    private Button btnBUSCAR;
    @FXML
    private Button btnADQUIRIR;
    @FXML
    private TableView<?> tbLIBROS;
    @FXML
    private TableView<?> tbADQUIRIDOS;
    @FXML
    private Button btnDEVOLVER;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }    
     public void verAlerta(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void realizarPrestamo(int idUsuario, int idLibro, Date fechaPrestamo, Date fechaDevolucionPrevista) {
        
    Connection conn = null;
    PreparedStatement checkLibroStmt = null;
    PreparedStatement checkUsuarioStmt = null;
    PreparedStatement insertPrestamoStmt = null;
    PreparedStatement updateLibroStmt = null;
    ResultSet rsLibro = null;
    ResultSet rsUsuario = null;

    try {
        // Obtener la conexión
        conn = conectarBD.getConnection();
        conn.setAutoCommit(false); // Iniciar transacción

        // Verificar disponibilidad del libro
        String checkLibroSql = "SELECT cantidad FROM libros WHERE idlibro = ?";
        checkLibroStmt = conn.prepareStatement(checkLibroSql);
        checkLibroStmt.setInt(1, idLibro);
        rsLibro = checkLibroStmt.executeQuery();

        if (rsLibro.next()) {
            int cantidadDisponible = rsLibro.getInt("cantidad");
            if (cantidadDisponible <= 0) {
                throw new SQLException("El libro no está disponible");
            }
        } else {
            throw new SQLException("Libro no encontrado");
        }

        // Verificar préstamos pendientes del usuario
        String checkUsuarioSql = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND estado != 'Devuelto'";
        checkUsuarioStmt = conn.prepareStatement(checkUsuarioSql);
        checkUsuarioStmt.setInt(1, idUsuario);
        rsUsuario = checkUsuarioStmt.executeQuery();

        if (rsUsuario.next()) {
            int prestamosPendientes = rsUsuario.getInt(1);
            if (prestamosPendientes > 0) {
                throw new SQLException("El usuario tiene préstamos pendientes");
            }
        } else {
            throw new SQLException("Usuario no encontrado");
        }

        // Registrar el préstamo
        String insertPrestamoSql = "INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, fecha_devolucion_prevista, estado) VALUES (?, ?, ?, ?, 'Prestado')";
        insertPrestamoStmt = conn.prepareStatement(insertPrestamoSql);
        insertPrestamoStmt.setInt(1, idUsuario);
        insertPrestamoStmt.setInt(2, idLibro);
        insertPrestamoStmt.setDate(3, new java.sql.Date(fechaPrestamo.getTime()));
        insertPrestamoStmt.setDate(4, new java.sql.Date(fechaDevolucionPrevista.getTime()));
        insertPrestamoStmt.executeUpdate();

        // Actualizar la cantidad del libro
        String updateLibroSql = "UPDATE libros SET cantidad = cantidad - 1 WHERE idlibro = ?";
        updateLibroStmt = conn.prepareStatement(updateLibroSql);
        updateLibroStmt.setInt(1, idLibro);
        updateLibroStmt.executeUpdate();

        // Confirmar transacción
        conn.commit();

        // Mostrar mensaje de éxito
        verAlerta(Alert.AlertType.INFORMATION, "Préstamo Realizado", "El préstamo ha sido realizado con éxito");

    } catch (SQLException ex) {
        try {
            if (conn != null) {
                conn.rollback(); // Revertir transacción en caso de error
            }
        } catch (SQLException rollbackEx) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, rollbackEx);
        }
        Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
        verAlerta(Alert.AlertType.ERROR, "Error en el Préstamo", "Error al realizar el préstamo: " + ex.getMessage());
    } finally {
        try {
            if (rsLibro != null) rsLibro.close();
            if (rsUsuario != null) rsUsuario.close();
            if (checkLibroStmt != null) checkLibroStmt.close();
            if (checkUsuarioStmt != null) checkUsuarioStmt.close();
            if (insertPrestamoStmt != null) insertPrestamoStmt.close();
            if (updateLibroStmt != null) updateLibroStmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    
    @FXML
    private void clickBUSCAR(ActionEvent event) {
    }

    @FXML
    private void clickADQUIRIR(ActionEvent event) {
        
    }

    @FXML
    private void clickDEVOLVER(ActionEvent event) {
    }
    
}
