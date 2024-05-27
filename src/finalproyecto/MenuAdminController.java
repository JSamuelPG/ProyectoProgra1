/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package finalproyecto;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;

/**
 * FXML Controller class
 *
 * @author patza
 */
public class MenuAdminController implements Initializable {

    @FXML
    private TextField txtID;
    @FXML
    private TextField txtDIRECCION;
    @FXML
    private TextField txtTELEFONO;
    @FXML
    private TextField txtNOMBRE;
    @FXML
    private TextField txtCARNE;
    @FXML
    private Button btnGUARDAR;
    @FXML
    private Button btnELIMINAR;
    @FXML
    private Button bntMODIFICAR;
    @FXML
    private TextField txtIDLIB;
    @FXML
    private TextField txtISBN;
    @FXML
    private TextField txtTITULO;
    @FXML
    private TextField txtAUTOR;
    @FXML
    private TextField txtEDITORIAL;
    @FXML
    private TextField txtANIO;
    @FXML
    private TextField txtCANTIDAD;
    @FXML
    private Button btnMODIFICAR;
    @FXML
    private TableView<?> tbLIBROS;
    @FXML
    private TextField txtCONTRA;
    @FXML
    private TableView<?> tbUSUARIOS;
    @FXML
    private TableColumn<?, ?> colID;
    @FXML
    private TableColumn<?, ?> colNOMBRE;
    @FXML
    private TableColumn<?, ?> colDIRECCION;
    @FXML
    private TableColumn<?, ?> colTELEFONO;
    @FXML
    private TableColumn<?, ?> colCARNE;
    @FXML
    private TableColumn<?, ?> colCONTRA;

    
    private ObservableList<Usuario> verlista = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }  
    
      ArrayList<Usuario> listaUsuarios = new ArrayList<>();
      Connection conn;
    
   
    
    //METODOS CRUD PARA USUARIOS
    @FXML
    private void clickGUARDAR(ActionEvent event) {
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
            verAlerta(Alert.AlertType.INFORMATION, "Registro Exitoso", "El usuario ha sido registrado");
        } catch (SQLException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(Alert.AlertType.ERROR, "Error en el Registro", "Error en el registro: " + ex.getMessage());
        } finally {
            // Close the PreparedStatement
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
                    verAlerta(Alert.AlertType.ERROR, "Error", "El usuario ya existe: " + ex.getMessage());
                }
            }
        }
    }
    
    public void verAlerta(Alert.AlertType alertType, String title, String message) {
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
    
    @FXML
    private void clickELIMINAR(ActionEvent event) {
        PreparedStatement st = null;

        try {
            // Obtener el ID del usuario a eliminar
            int idUsuario = Integer.parseInt(txtID.getText());

            conn = conectarBD.getConnection();

            String sqleliminar = "DELETE FROM usuarios WHERE id = ?";
            st = conn.prepareStatement(sqleliminar);

            // Asignar el parámetro
            st.setInt(1, idUsuario);

            // Ejecutar la consulta
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                limpiar();
                verAlerta(AlertType.INFORMATION, "Eliminación Exitosa", "El usuario ha sido eliminado");
            } else {
                verAlerta(AlertType.WARNING, "Usuario No Encontrado", "No se encontró un usuario con el ID proporcionado");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "Error en la Eliminación", "Error al eliminar el usuario: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "ID Inválido", "El ID proporcionado no es un número válido: " + ex.getMessage());
        }
    }

    @FXML
    private void clickMODIFICAR(ActionEvent event) {
        PreparedStatement st = null;
        Connection conn = null;

        try {
            // Obtener los valores de los campos de texto
            int idUsuario = Integer.parseInt(txtID.getText());
            String nombre = txtNOMBRE.getText();
            String direccion = txtDIRECCION.getText();
            int telefono = Integer.parseInt(txtTELEFONO.getText());
            String carne = txtCARNE.getText();
            String contra = txtCONTRA.getText();

            conn = conectarBD.getConnection();

            String sqlUpdate = "UPDATE usuarios SET nombre = ?, direccion = ?, telefono = ?, carne = ?, contrasenia = ? WHERE id = ?";
            st = conn.prepareStatement(sqlUpdate);

            // Asignar los parámetros
            st.setString(1, nombre);
            st.setString(2, direccion);
            st.setInt(3, telefono);
            st.setString(4, carne);
            st.setString(5, contra);
            st.setInt(6, idUsuario);

            // Ejecutar la consulta
            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                limpiar();
                verAlerta(AlertType.INFORMATION, "Actualización Exitosa", "El usuario ha sido actualizado");
            } else {
                verAlerta(AlertType.WARNING, "Usuario No Encontrado", "No se encontró un usuario con el ID proporcionado");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "Error en la Actualización", "Error al actualizar el usuario: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "ID Inválido", "El ID o el teléfono proporcionado no es un número válido: " + ex.getMessage());
        }
    }

    //METODOS CRUD PARA LIBROS ----------------------------------------------------------------------------------------------------
    
    private ArrayList<Libros> listaLibros = new ArrayList<>();

    public void ingresarLibros() {
        PreparedStatement ps = null;

        try {
            Libros ingresa = new Libros(
                Integer.parseInt(txtIDLIB.getText()),
                txtISBN.getText(),
                txtTITULO.getText(),
                txtAUTOR.getText(),
                Integer.parseInt(txtANIO.getText()),
                txtEDITORIAL.getText(),
                Integer.parseInt(txtCANTIDAD.getText())
            );
            listaLibros.add(ingresa);

            conn = conectarBD.getConnection();

            String ingreSQL = "INSERT INTO rlibros(idlibro, isbn, titulo, autor, anio_publicacion, editorial, cantidad) VALUES (?, ?, ?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(ingreSQL);

            ps.setInt(1, ingresa.getId_libro());
            ps.setString(2, ingresa.getIsbn());
            ps.setString(3, ingresa.getTitulo());
            ps.setString(4, ingresa.getAutor());
            ps.setInt(5, ingresa.getAnio());
            ps.setString(6, ingresa.getEditorial());
            ps.setInt(7, ingresa.getCantidad());

            ps.executeUpdate();
            verAlerta(Alert.AlertType.INFORMATION, "Registro Exitoso", "El Libro ha sido registrado");
        } catch (SQLException ex) {
            Logger.getLogger(MenuAdminController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(Alert.AlertType.ERROR, "Error en el Registro", "Error en el registro: " + ex.getMessage());
        } finally {
            // Cerrar el PreparedStatement
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MenuAdminController.class.getName()).log(Level.SEVERE, null, ex);
                }}}}
    @FXML
    private void clickGUAR(ActionEvent event) {
        ingresarLibros();
    }

    @FXML
    private void clickELIM(ActionEvent event) {
        PreparedStatement ps = null;

        try {
            // Obtener el ID del usuario a eliminar
            int idLibro = Integer.parseInt(txtIDLIB.getText());

            conn = conectarBD.getConnection();

            String sqleliminar = "DELETE FROM rlibros WHERE idlibro = ?";
            ps = conn.prepareStatement(sqleliminar);

            // Asignar el parámetro
            ps.setInt(1, idLibro);

            // Ejecutar la consulta
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                limpiar();
                verAlerta(AlertType.INFORMATION, "Eliminación Exitosa", "El libro ha sido eliminado");
            } else {
                verAlerta(AlertType.WARNING, "Libro No Encontrado", "No se encontró un libro con el ID proporcionado");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "Error en la Eliminación", "Error al eliminar el libro: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "ID Inválido", "El ID proporcionado no es un número válido: " + ex.getMessage());
        }
    }

    @FXML
    private void clickMOD(ActionEvent event) {
        PreparedStatement prep = null;
        Connection conn = null;

        try {
            // Obtener los valores de los campos de texto
            int idlib = Integer.parseInt(txtIDLIB.getText());
            String isbn = txtISBN.getText();
            String titulo = txtTITULO.getText();
            String autor  = txtAUTOR.getText();
            int anio = Integer.parseInt(txtANIO.getText());
            String editorial = txtEDITORIAL.getText();
            int cantidad = Integer.parseInt(txtCANTIDAD .getText());

            conn = conectarBD.getConnection();

            String sqlUpdate = "UPDATE rlibros SET isbn = ?, titulo = ?, autor = ?, anio_publicacion = ?, editorial = ?, cantidad = ? WHERE idlibro = ?";
            prep = conn.prepareStatement(sqlUpdate);

            // Asignar los parámetros
            prep.setString(1, isbn);
            prep.setString(2, titulo);
            prep.setString(3, autor);
            prep.setInt(4, anio);
            prep.setString(5, editorial);
            prep.setInt(6, cantidad);
            prep.setInt(7, idlib);

            // Ejecutar la consulta
            int rowsAffected = prep.executeUpdate();

            if (rowsAffected > 0) {
                limpiar();
                verAlerta(AlertType.INFORMATION, "Actualización Exitosa", "El usuario ha sido actualizado");
            } else {
                verAlerta(AlertType.WARNING, "Usuario No Encontrado", "No se encontró un usuario con el ID proporcionado");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "Error en la Actualización", "Error al actualizar el usuario: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            Logger.getLogger(ReUsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(AlertType.ERROR, "ID Inválido", "El ID o el teléfono proporcionado no es un número válido: " + ex.getMessage());
        }
    }
    
}
