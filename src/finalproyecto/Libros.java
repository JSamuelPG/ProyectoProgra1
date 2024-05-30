/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproyecto;

/**
 *
 * @author patza
 */
public class Libros {
    int id_libro;
    String isbn;
    String titulo;
    String autor;
    int anio;
    String editorial;
    int cantidad;

    public Libros(int id_libro, String isbn, String titulo, String autor, int anio, String editorial, int cantidad) {
        this.id_libro = id_libro;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.editorial = editorial;
        this.cantidad = cantidad;
    }

    public int getId_libro() {
        return id_libro;
    }

    public void setId_libro(int id_libro) {
        this.id_libro = id_libro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Libros{" + "id_libro=" + id_libro 
                + ", isbn=" + isbn 
                + ", titulo=" + titulo 
                + ", autor=" + autor 
                + ", anio=" + anio 
                + ", editorial=" + editorial 
                + ", cantidad=" + cantidad + '}';
    }
    
    
}
