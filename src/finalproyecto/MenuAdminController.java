/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package finalproyecto;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableView<Libros> tbLIBROS;
    @FXML
    private TextField txtCONTRA;
    @FXML
    private TableView<Usuario> tbUSUARIOS;
    
    private ObservableList<Usuario> verlista = FXCollections.observableArrayList();
    private ObservableList<Libros> listaLibros = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         configurarColumnasTabla();
         cargarLibrosDesdeBaseDeDatos();
         cargarUsuariosDesdeBaseDeDatos();
    }  
    
    private void configurarColumnasTabla() {
        TableColumn<Libros, Integer> colIdLibro = new TableColumn<>("ID");
        TableColumn<Libros, String> colIsbn = new TableColumn<>("ISBN");
        TableColumn<Libros, String> colTitulo = new TableColumn<>("Título");
        TableColumn<Libros, String> colAutor = new TableColumn<>("Autor");
        TableColumn<Libros, Integer> colAnio = new TableColumn<>("Año");
        TableColumn<Libros, String> colEditorial = new TableColumn<>("Editorial");
        TableColumn<Libros, Integer> colCantidad = new TableColumn<>("Cantidad Disponible");

        colIdLibro.setCellValueFactory(new PropertyValueFactory<>("id_libro"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        tbLIBROS.getColumns().addAll(colIdLibro, colIsbn, colTitulo, colAutor, colAnio, colEditorial, colCantidad);
        
        TableColumn<Usuario, Integer> colId = new TableColumn<>("ID");
        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Usuario, String> colDireccion = new TableColumn<>("Dirección");
        TableColumn<Usuario, Integer> colTelefono = new TableColumn<>("Teléfono");
        TableColumn<Usuario, String> colCarne = new TableColumn<>("Carné");
        TableColumn<Usuario, String> colContrasenia = new TableColumn<>("Contraseña");

        // Asignar las propiedades de cada columna
        colId.setCellValueFactory(new PropertyValueFactory<>("idusuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCarne.setCellValueFactory(new PropertyValueFactory<>("carne"));
        colContrasenia.setCellValueFactory(new PropertyValueFactory<>("contra"));

        // Agregar las columnas a la TableView
        tbUSUARIOS.getColumns().addAll(colId, colNombre, colDireccion, colTelefono, colCarne, colContrasenia);

     }
    
    private void cargarLibrosDesdeBaseDeDatos() {
        listaLibros.clear();
       String consultaSql = "SELECT * FROM rlibros";

       try (Connection conn = conectarBD.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(consultaSql)) {

           while (rs.next()) {
               int id_libro = rs.getInt("idlibro");
               String isbn = rs.getString("isbn");
               String titulo = rs.getString("titulo");
               String autor = rs.getString("autor");
               int anio = rs.getInt("anio_publicacion");
               String editorial = rs.getString("editorial");
               int cantidad = rs.getInt("cantidad");

               Libros libro = new Libros(id_libro, isbn, titulo, autor, anio, editorial, cantidad);
               listaLibros.add(libro);
           }
       } catch (SQLException ex) {
           ex.printStackTrace();
           verAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar libros desde la base de datos: " + ex.getMessage());
       }
       tbLIBROS.setItems(listaLibros);
}
 
    private void cargarUsuariosDesdeBaseDeDatos() {
    verlista.clear(); // Limpia
    String consultaSql = "SELECT * FROM usuarios";

    try (Connection conn = conectarBD.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(consultaSql)) {

        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String direccion = rs.getString("direccion");
            int telefono = rs.getInt("telefono");
            String carne = rs.getString("carne");
            String contrasenia = rs.getString("contrasenia");

            Usuario usuario = new Usuario(id, nombre, direccion, telefono, carne, contrasenia);
            verlista.add(usuario);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        verAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar usuarios desde la base de datos: " + ex.getMessage());
    }
    tbUSUARIOS.setItems(verlista);
}
    
    
      ArrayList<Usuario> listaUsuarios = new ArrayList<>();
      Connection conn;
      
    //METODOS CRUD PARA USUARIOS
    @FXML
    private void clickGUARDAR(ActionEvent event) {
       PreparedStatement st = null;

        try {
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

            st.setInt(1, usu.getIdusuario());
            st.setString(2, usu.getNombre());
            st.setString(3, usu.getDireccion());
            st.setInt(4, usu.getTelefono());
            st.setString(5, usu.getCarne());
            st.setString(6, usu.getContra());

            st.executeUpdate();
            limpiar();
            verAlerta(Alert.AlertType.INFORMATION, "Registro Exitoso", "El usuario ha sido registrado");
        } catch (SQLException ex) {
            Logger.getLogger(MenuAdminController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(Alert.AlertType.ERROR, "Error en el Registro", "Error en el registro: " + ex.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MenuAdminController.class.getName()).log(Level.SEVERE, null, ex);
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
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MenuAdminController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
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
