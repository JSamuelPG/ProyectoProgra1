/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package finalproyecto;

/**
 *
 * @author patza
 */
public class Usuario {
    private int idusuario;
    private String nombre;
    private String direccion;
    private int telefono;
    private String carne;
    private String contra;

    public Usuario(int idusuario, String nombre, String direccion, int telefono, String carne, String contra) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.carne = carne;
        this.contra = contra;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idusuario=" + idusuario 
                + ", nombre=" + nombre 
                + ", direccion=" + direccion 
                + ", telefono=" + telefono 
                + ", carne=" + carne 
                + ", contra=" + contra + '}';
    }
    
}
