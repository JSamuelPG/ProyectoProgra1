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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

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
    private Button btnDEVOLVER;
    @FXML
    private TableView<Libros> tbLIBROS;
    @FXML
    private TableView<Libros> tbADQUIRIDOS;
    @FXML
    private TextField txtIDLIBROS;
    @FXML
    private TableColumn<Libros, Integer> colIDLIB;
    @FXML
    private TableColumn<Libros, String> colISBN;
    @FXML
    private TableColumn<Libros, String> colTITULO;
    @FXML
    private TableColumn<Libros, String> colAUTOR;
    @FXML
    private TableColumn<Libros, Integer> colANIO;
    @FXML
    private TableColumn<Libros, String> colEDITORIAL;
    @FXML
    private TableColumn<Libros, Integer> colCANTIDAD;
    
    private ObservableList<Libros> listaLibros = FXCollections.observableArrayList();
    private Connection conn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Configurar las columnas de la tabla
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
        tbLIBROS.setItems(listaLibros);
    }    
     public void verAlerta(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
       
    private void buscarLibros(String terminoBusqueda) {
    listaLibros = FXCollections.observableArrayList();
    
    String sql = "SELECT * FROM rlibros WHERE CAST(idlibro AS TEXT) LIKE ? OR isbn LIKE ? OR titulo LIKE ? OR autor LIKE ?";

    try (Connection conn = conectarBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        String termino = "%" + terminoBusqueda + "%";
        stmt.setString(1, termino);
        stmt.setString(2, termino);
        stmt.setString(3, termino);
        stmt.setString(4, termino);
        
        ResultSet rs = stmt.executeQuery();
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
        
        tbLIBROS.setItems(listaLibros);

    } catch (SQLException ex) {
        verAlerta(Alert.AlertType.ERROR, "Error en la búsqueda", "No se pudo realizar la búsqueda: " + ex.getMessage());
    }
}      
    @FXML
    private void clickBUSCAR(ActionEvent event) {
        String textoBusqueda = txtIDLIBROS.getText();
        if (textoBusqueda.isEmpty()) {
            verAlerta(Alert.AlertType.WARNING, "Campo de Búsqueda Vacío", "Por favor ingrese un término de búsqueda");
            return;
        }
        buscarLibros(textoBusqueda);
    }
    
     private boolean verificarDisponibilidadLibro(int idLibro) throws SQLException {
        String checkLibroSql = "SELECT cantidad FROM libros WHERE idlibro = ?";
        try (PreparedStatement checkLibroStmt = conn.prepareStatement(checkLibroSql)) {
            checkLibroStmt.setInt(1, idLibro);
            try (ResultSet rsLibro = checkLibroStmt.executeQuery()) {
                if (rsLibro.next()) {
                    int cantidadDisponible = rsLibro.getInt("cantidad");
                    if (cantidadDisponible > 0) {
                        return true;
                    } else {
                        throw new SQLException("El libro no está disponible");
                    }
                } else {
                    throw new SQLException("Libro no encontrado");
                }
            }
        }
    }
    
    private boolean verificarPrestamosPendientes(int idUsuario) throws SQLException {
        String checkUsuarioSql = "SELECT COUNT(*) FROM prestamos WHERE id_usuario = ? AND estado != 'Devuelto'";
        try (PreparedStatement checkUsuarioStmt = conn.prepareStatement(checkUsuarioSql)) {
            checkUsuarioStmt.setInt(1, idUsuario);
            try (ResultSet rsUsuario = checkUsuarioStmt.executeQuery()) {
                if (rsUsuario.next()) {
                    int prestamosPendientes = rsUsuario.getInt(1);
                    if (prestamosPendientes == 0) {
                        return true;
                    } else {
                        throw new SQLException("El usuario tiene préstamos pendientes");
                    }
                } else {
                    throw new SQLException("Usuario no encontrado");
                }
            }
        }
    }
    
    private void registrarPrestamo(int idUsuario, int idLibro, Date fechaPrestamo, Date fechaDevolucionPrevista) throws SQLException {
        String insertPrestamoSql = "INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, fecha_devolucion_prevista, estado) VALUES (?, ?, ?, ?, 'Prestado')";
        try (PreparedStatement insertPrestamoStmt = conn.prepareStatement(insertPrestamoSql)) {
            insertPrestamoStmt.setInt(1, idUsuario);
            insertPrestamoStmt.setInt(2, idLibro);
            insertPrestamoStmt.setDate(3, new java.sql.Date(fechaPrestamo.getTime()));
            insertPrestamoStmt.setDate(4, new java.sql.Date(fechaDevolucionPrevista.getTime()));
            insertPrestamoStmt.executeUpdate();
        }
    }
  
    private void actualizarCantidadLibro(int idLibro) throws SQLException {
        String updateLibroSql = "UPDATE libros SET cantidad = cantidad - 1 WHERE idlibro = ?";
        try (PreparedStatement updateLibroStmt = conn.prepareStatement(updateLibroSql)) {
            updateLibroStmt.setInt(1, idLibro);
            updateLibroStmt.executeUpdate();
        }
    }
    
    public void realizarPrestamo(int idUsuario, int idLibro, Date fechaPrestamo, Date fechaDevolucionPrevista) {
        try {
            if (verificarDisponibilidadLibro(idLibro) && verificarPrestamosPendientes(idUsuario)) {
                registrarPrestamo(idUsuario, idLibro, fechaPrestamo, fechaDevolucionPrevista);
                actualizarCantidadLibro(idLibro);
                conn.commit();
                verAlerta(Alert.AlertType.INFORMATION, "Préstamo Realizado", "El préstamo ha sido realizado con éxito");
            }
        } catch (SQLException ex) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                Logger.getLogger(MenuUsuarioController.class.getName()).log(Level.SEVERE, null, rollbackEx);
            }
            Logger.getLogger(MenuUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            verAlerta(Alert.AlertType.ERROR, "Error en el Préstamo", "Error al realizar el préstamo: " + ex.getMessage());
        }
    }
    

    @FXML
    private void clickADQUIRIR(ActionEvent event) {
       Libros libroSeleccionado = tbLIBROS.getSelectionModel().getSelectedItem();
    if (libroSeleccionado != null) {
        int idLibro = libroSeleccionado.getId_libro();
        int idUsuario = SesionUsuario.getIdUsuario(); // Obtener el ID del usuario desde la sesión
        
        // Obtener la fecha actual
        Date fechaPrestamo = new Date(System.currentTimeMillis());
        
        // Calcular la fecha de devolución prevista (7 días después)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaPrestamo);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date fechaDevolucionPrevista = (Date) calendar.getTime();
        
        // Llamar al método realizarPrestamo con los cuatro argumentos
        realizarPrestamo(idUsuario, idLibro, fechaPrestamo, fechaDevolucionPrevista);
    } else {
        verAlerta(Alert.AlertType.WARNING, "Libro no Seleccionado", "Por favor, seleccione un libro para adquirir.");
    }
    }

    @FXML
    private void clickDEVOLVER(ActionEvent event) {
    }
    
}
