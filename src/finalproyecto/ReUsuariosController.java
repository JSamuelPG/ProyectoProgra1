/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package finalproyecto;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author patza
 */
public class ReUsuariosController implements Initializable {

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtNOMBRE;
    @FXML
    private TextField txtDIRECCION;
    @FXML
    private TextField txtTELEFONO;
    @FXML
    private TextField txtCARNE;
    @FXML
    private TextField txtCONTRA;
    @FXML
    private Button btnREGISTRAR;
    @FXML
    private Button btnREGRESAR;

    ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    Connection conn;
    /**
     * Initializes the controller class.
     */
    
      public void ingresarUsuarios() {
        PreparedStatement st = null;

        try {
            // Create a new Usuario object
            Usuario usu = new Usuario(
                Integer.parseInt(txtID.getText()),
                txtNOMBRE.getText(),
                txtDIRECCION.getText(),
                Integer.parseInt(txtTELEFONO.getText()),
                txtCARNE.getText(),
                txtCONTRA.getText()
            );

            listaUsuarios.add(usu);

            conn = conectarBD.getConnection();

            String sqlInsert = "INSERT INTO usuarios(id, nombre, direccion, telefono, carne, contrasenia) VALUES (?, ?, ?, ?, ?, ?)";
            st = conn.prepareStatement(sqlInsert);

            // Set the parameters
            st.setInt(1, usu.getIdusuario());
            st.setString(2, usu.getNombre());
            st.setString(3, usu.getDireccion());
            st.setInt(4, usu.getTelefono());
            st.setString(5, usu.getCarne());
            st.setString(6, usu.getContra());

            // Execute the statement
            st.executeUpdate();
            limpiar();
            verAlerta(AlertType.INFORMATION, "Registro Exitoso", "El usuario ha sido registrado");
        } catch (SQLException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "Error en el Registro", "Error en el registro: " + ex.getMessage());
        } finally {
            // Close the PreparedStatement
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
                    verAlerta(AlertType.ERROR, "Error", "El usuario ya existe: " + ex.getMessage());
                }
            }
        }
    }

    private void verAlerta(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void limpiar(){
                txtID.clear();
                txtNOMBRE.clear();
                txtDIRECCION.clear();
                txtTELEFONO.clear();
                txtCARNE.clear();
                txtCONTRA.clear();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickREGISTRAR(ActionEvent event) {
        ingresarUsuarios();
    }

    @FXML
    private void clickREGRESAR(ActionEvent event) {
        try {
            // Usar ruta relativa al directorio de recursos del proyecto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/finalproyecto/sesion.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena con el contenido cargado
            Scene scene = new Scene(root);

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnREGISTRAR.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Registrar Usuarios");
            stage.show();

        } catch (IOException e) {
            // Manejar errores
            e.printStackTrace();
        }
    }
    
}
