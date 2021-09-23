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
public class Cuenta {
 private int numCuenta;
 private double saldoActual;
 private int cedUsuario;
 private boolean estado;

    public Cuenta(int numCuenta, double saldoActual, int cedUsuario, boolean estado) {
        this.numCuenta = numCuenta;
        this.saldoActual = 1000000;
        this.cedUsuario = cedUsuario;
        this.estado= estado;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    

    public int getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(int numCuenta) {
        this.numCuenta = numCuenta;
    }

    public int getSaldoActual() {
        return (int) saldoActual;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public int getCedUsuario() {
        return cedUsuario;
    }

    public void setCedUsuario(int cedUsuario) {
        this.cedUsuario = cedUsuario;
    }

    @Override
    public String toString() {
        return "Cuenta{" + "numCuenta=" + numCuenta + ", saldoActual=" + saldoActual + ", cedUsuario=" + cedUsuario + '}';
    }
    
    
    public int generarNumCuenta(){
        int numero=0;
        numero = (int)(Math.random()*1000000000);
        return numero=Integer.parseInt("0"+numero);
    }
    
}