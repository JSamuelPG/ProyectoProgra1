/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package finalproyecto;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class SesionController implements Initializable {

    @FXML
    private TextField txtCARNE;
    @FXML
    private TextField txtCONTRA;
    @FXML
    private Button btnINICIAR;
    @FXML
    private Button btnREGISTRAR;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clickINICIAR(ActionEvent event) {
         administrador();
        try {
            String carne = txtCARNE.getText();
            String contra = txtCONTRA.getText();
            Connection conn = conectarBD.getConnection();

            String misql = "SELECT * FROM usuarios WHERE carne = ? AND contrasenia = ?";
            PreparedStatement ps = conn.prepareStatement(misql);
            ps.setString(1, carne);
            ps.setString(2, contra);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Guardar el ID del usuario en la clase SesionUsuario
                int idUsuario = rs.getInt("id_usuario");
                SesionUsuario.setIdUsuario(idUsuario);

                // Cargar el archivo FXML de la nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/finalproyecto/menuUsuario.fxml"));
                Parent root = loader.load();

                // Configurar la nueva escena y mostrarla en una nueva ventana
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Menú de Usuario");
                stage.show();

                // Cerrar la ventana actual
                Stage currentStage = (Stage) btnINICIAR.getScene().getWindow();
                currentStage.close();
            } else {
                // Mostrar un mensaje de error si el usuario no existe
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error de Autenticación");
                alert.setHeaderText("El Usuario no existe");
                alert.setContentText("Por favor, verifica tu carné y contraseña e intenta nuevamente.");
                alert.showAndWait();
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al buscar");
            alert.setContentText("Hubo un error al intentar buscar el usuario en la base de datos.");
            alert.showAndWait();
            Logger.getLogger(SesionController.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(SesionController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void clickREGISTRAR(ActionEvent event) {
        try {
            // Usar ruta relativa al directorio de recursos del proyecto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/finalproyecto/reUsuarios.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena con el contenido cargado
            Scene scene = new Scene(root);

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) btnREGISTRAR.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Registrar Usuarios");
            stage.show();

        } catch (IOException e) {
            // Manejar errores de IO
            e.printStackTrace();
        }
    }
    
     private void administrador() {
        // Obtener los valores ingresados
        String carne = txtCARNE.getText();
        String contra = txtCONTRA.getText();

        // Verificar si las credenciales son correctas
        if ("admin".equals(carne) && "1444".equals(contra)) {
            try {
                // Cargar el archivo FXML de la nueva ventana
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/finalproyecto/menuAdmin.fxml"));
                Parent root = loader.load();

                // Crear una nueva escena con el contenido cargado
                Scene scene = new Scene(root);

                // Obtener la ventana actual y cambiar la escena
                Stage stage = (Stage) btnINICIAR.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Admin Panel");
                stage.show();

            } catch (IOException e) {
                // Manejar errores de IO
                e.printStackTrace();
            }
        } else {
            // Simplemente imprimir en consola si las credenciales son incorrectas
            System.out.println("El usuario o la contraseña son incorrectos.");
        }
    }
    }