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
    private Button btnACTUALIZAR;
    @FXML
    private TableView<Libros> tbLIBROS;
    @FXML
    private TableView<Libros> tbADQUIRIDOS;
    @FXML
    private TextField txtIDLIBROS;
    
    private ObservableList<Libros> listaLibros = FXCollections.observableArrayList();
    private ObservableList<Libros> listaLibrosAdquiridos = FXCollections.observableArrayList();
    private Connection conn;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    // Limpiar la lista de libros
        listaLibros.clear();
        // Configurar las columnas de la tabla
        configurarColumnasTabla();
        // Cargar los libros iniciales desde la base de datos
        cargarLibrosDesdeBaseDeDatos();
        
        listaLibrosAdquiridos.clear(); 
        // Cargar los libros adquiridos desde la base de datos
        int idUsuario = SesionUsuario.getIdUsuario();
        cargarLibrosAdquiridosDesdeBaseDeDatos(idUsuario);
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
    
    // Configuración de columnas para tbADQUIRIDOS
        TableColumn<Libros, Integer> colIdLibroAdquirido = new TableColumn<>("ID");
        TableColumn<Libros, String> colIsbnAdquirido = new TableColumn<>("ISBN");
        TableColumn<Libros, String> colTituloAdquirido = new TableColumn<>("Título");
        TableColumn<Libros, String> colAutorAdquirido = new TableColumn<>("Autor");
        TableColumn<Libros, Integer> colAnioAdquirido = new TableColumn<>("Año");
        TableColumn<Libros, String> colEditorialAdquirido = new TableColumn<>("Editorial");

        colIdLibroAdquirido.setCellValueFactory(new PropertyValueFactory<>("id_libro"));
        colIsbnAdquirido.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTituloAdquirido.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutorAdquirido.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnioAdquirido.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colEditorialAdquirido.setCellValueFactory(new PropertyValueFactory<>("editorial"));

        tbADQUIRIDOS.getColumns().addAll(colIdLibroAdquirido, colIsbnAdquirido, colTituloAdquirido, colAutorAdquirido, colAnioAdquirido, colEditorialAdquirido);
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
     public void verAlerta(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
       
    private void buscarLibros(String terminoBusqueda) {
    listaLibros.clear();
    System.out.println("Iniciando búsqueda de libros..."); // Log de depuración

    String sql = "SELECT * FROM rlibros WHERE CAST(idlibro AS TEXT) LIKE ? OR isbn LIKE ? OR titulo LIKE ? OR autor LIKE ?";

    try (Connection conn = conectarBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        System.out.println("Conexión abierta para buscar libros."); // Log de depuración

        String termino = "%" + terminoBusqueda + "%";
        stmt.setString(1, termino);
        stmt.setString(2, termino);
        stmt.setString(3, termino);
        stmt.setString(4, termino);

        try (ResultSet rs = stmt.executeQuery()) {
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
        }

        tbLIBROS.setItems(listaLibros);
        System.out.println("Búsqueda completada y datos cargados en la tabla."); // Log de depuración

    } catch (SQLException ex) {
        verAlerta(Alert.AlertType.ERROR, "Error en la búsqueda", "No se pudo realizar la búsqueda: " + ex.getMessage());
    }
}      
    @FXML
    private void clickBUSCAR(ActionEvent event) {
          listaLibros.clear();
          String textoBusqueda = txtIDLIBROS.getText();
          if (textoBusqueda.isEmpty()) {
              verAlerta(Alert.AlertType.WARNING, "Campo de Búsqueda Vacío", "Por favor ingrese un término de búsqueda");
              return;
          }
          buscarLibros(textoBusqueda);
    }
    
    //----------------------------------------------------------------------------------------------------------------------------
    


    private boolean verificarDisponibilidadLibro(int idLibro, Connection conn) throws SQLException {
        String checkLibroSql = "SELECT cantidad FROM rlibros WHERE idlibro = ?";
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

    private boolean verificarPrestamosPendientes(int idUsuario, Connection conn) throws SQLException {
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

    private void registrarPrestamo(int idUsuario, int idLibro, java.sql.Date fechaPrestamo, java.sql.Date fechaDevolucionPrevista, Connection conn) throws SQLException {
        String insertPrestamoSql = "INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, fecha_devolucion_prevista, estado) VALUES (?, ?, ?, ?, 'Prestado')";
        try (PreparedStatement insertPrestamoStmt = conn.prepareStatement(insertPrestamoSql)) {
            insertPrestamoStmt.setInt(1, idUsuario);
            insertPrestamoStmt.setInt(2, idLibro);
            insertPrestamoStmt.setDate(3, fechaPrestamo);
            insertPrestamoStmt.setDate(4, fechaDevolucionPrevista);
            insertPrestamoStmt.executeUpdate();
        }
    }

    private void actualizarCantidadLibro(int idLibro, Connection conn) throws SQLException {
        String updateLibroSql = "UPDATE rlibros SET cantidad = cantidad - 1 WHERE idlibro = ?";
        try (PreparedStatement updateLibroStmt = conn.prepareStatement(updateLibroSql)) {
            updateLibroStmt.setInt(1, idLibro);
            updateLibroStmt.executeUpdate();
        }
    }

    public void realizarPrestamo(int idUsuario, int idLibro, java.sql.Date fechaPrestamo, java.sql.Date fechaDevolucionPrevista) {
        try (Connection conn = conectarBD.getConnection()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejar transacciones manualmente
            if (verificarDisponibilidadLibro(idLibro, conn) && verificarPrestamosPendientes(idUsuario, conn)) {
                registrarPrestamo(idUsuario, idLibro, fechaPrestamo, fechaDevolucionPrevista, conn);
                actualizarCantidadLibro(idLibro, conn);
                conn.commit();
                verAlerta(Alert.AlertType.INFORMATION, "Préstamo Realizado", "El préstamo ha sido realizado con éxito");
            }
        } catch (SQLException ex) {
            try (Connection conn = conectarBD.getConnection()) {
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
            java.util.Date fechaActual = new java.util.Date();
            java.sql.Date fechaPrestamo = new java.sql.Date(fechaActual.getTime());

            // Calcular la fecha de devolución prevista (7 días después)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaActual);
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            java.sql.Date fechaDevolucionPrevista = new java.sql.Date(calendar.getTimeInMillis());

            // Llamar al método realizarPrestamo con los cuatro argumentos
            realizarPrestamo(idUsuario, idLibro, fechaPrestamo, fechaDevolucionPrevista);
        } else {
            verAlerta(Alert.AlertType.WARNING, "Libro no Seleccionado", "Por favor, seleccione un libro para adquirir.");
        }
    }
//--------------------------------------------------------------------------------------------------------------------------------------
    
    private void cargarLibrosAdquiridosDesdeBaseDeDatos(int idUsuario) {
        listaLibrosAdquiridos.clear();
        String consultaSql = "SELECT l.idlibro, l.isbn, l.titulo, l.autor, l.anio_publicacion, l.editorial " +
                             "FROM prestamos p " +
                             "JOIN rlibros l ON p.id_libro = l.idlibro " +
                             "WHERE p.id_usuario = ? AND p.estado = 'Prestado'";

        try (Connection conn = conectarBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(consultaSql)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id_libro = rs.getInt("idlibro");
                    String isbn = rs.getString("isbn");
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    int anio = rs.getInt("anio_publicacion");
                    String editorial = rs.getString("editorial");

                    Libros libro = new Libros(id_libro, isbn, titulo, autor, anio, editorial, 0);
                    listaLibrosAdquiridos.add(libro);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            verAlerta(Alert.AlertType.ERROR, "Error", "Error al cargar libros adquiridos desde la base de datos: " + ex.getMessage());
        }
        tbADQUIRIDOS.setItems(listaLibrosAdquiridos);
    }
    
    // Método para devolver libros
    private void devolverLibro(int idUsuario, int idLibro) throws SQLException {
        String updatePrestamoSql = "UPDATE prestamos SET fecha_devolucion_real = ?, estado = 'Devuelto' WHERE id_usuario = ? AND id_libro = ? AND estado = 'Prestado'";
        try (Connection conn = conectarBD.getConnection();
             PreparedStatement updatePrestamoStmt = conn.prepareStatement(updatePrestamoSql)) {

            // Obtener la fecha actual para la fecha de devolución real
            java.util.Date fechaActual = new java.util.Date();
            java.sql.Date fechaDevolucionReal = new java.sql.Date(fechaActual.getTime());

            // Configurar los parámetros de la consulta
            updatePrestamoStmt.setDate(1, fechaDevolucionReal);
            updatePrestamoStmt.setInt(2, idUsuario);
            updatePrestamoStmt.setInt(3, idLibro);

            int filasActualizadas = updatePrestamoStmt.executeUpdate();

            if (filasActualizadas > 0) {
                // Actualizar la cantidad del libro en rlibros
                String updateLibroSql = "UPDATE rlibros SET cantidad = cantidad + 1 WHERE idlibro = ?";
                try (PreparedStatement updateLibroStmt = conn.prepareStatement(updateLibroSql)) {
                    updateLibroStmt.setInt(1, idLibro);
                    updateLibroStmt.executeUpdate();
                }
            } else {
                throw new SQLException("No se encontró un préstamo activo para el libro y usuario proporcionados.");
            }
        }
    }
    
    private void actualizarTablaAdquiridos() {
    int idUsuario = SesionUsuario.getIdUsuario(); // Obtener el ID del usuario desde la sesión
    cargarLibrosAdquiridosDesdeBaseDeDatos(idUsuario);
    }
    
    @FXML
    private void clickDEVOLVER(ActionEvent event) {
        Libros libroSeleccionado = tbADQUIRIDOS.getSelectionModel().getSelectedItem();
        if (libroSeleccionado != null) {
            int idLibro = libroSeleccionado.getId_libro();
            int idUsuario = SesionUsuario.getIdUsuario(); // Obtener el ID del usuario desde la sesión

            try {
                devolverLibro(idUsuario, idLibro);
                verAlerta(Alert.AlertType.INFORMATION, "Devolución Exitosa", "El libro ha sido devuelto con éxito");
                cargarLibrosAdquiridosDesdeBaseDeDatos(idUsuario); // Actualizar tabla de libros adquiridos
                cargarLibrosDesdeBaseDeDatos(); // Actualizar tabla de libros disponibles
            } catch (SQLException ex) {
                Logger.getLogger(MenuUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                verAlerta(Alert.AlertType.ERROR, "Error en la Devolución", "Error al devolver el libro: " + ex.getMessage());
            }
        } else {
            verAlerta(Alert.AlertType.WARNING, "Libro no Seleccionado", "Por favor, seleccione un libro para devolver.");
        }
        actualizarTablaAdquiridos();
    }
    
    @FXML
    private void clickACTUALIZAR(ActionEvent event) {
        int idUsuario = SesionUsuario.getIdUsuario(); // Obtener el ID del usuario desde la sesión
    cargarLibrosAdquiridosDesdeBaseDeDatos(idUsuario);
    }
}