/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author wposs
 */
public class Usuario {
    private String nombre;
   // private String apellido;
    private String correo;
    private String contraseña;
    private int cedula;

    public Usuario(String nombre, String correo, String contraseña, int cedula) {
        this.nombre = nombre;
      //  this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
//
//    public String getApellido() {
//        return apellido;
//    }
//
//    public void setApellido(String apellido) {
//        this.apellido = apellido;
//    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    @Override
    public String toString() {
        return "Usuario{" + "nombre=" + nombre +  ", correo=" + correo + ", contrase\u00f1a=" + contraseña + ", cedula=" + cedula + '}';
    }
   
    
    
}
