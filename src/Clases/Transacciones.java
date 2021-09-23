/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import javax.swing.JOptionPane;

/**
 *
 * @author wposs
 */
public class Transacciones {
    private int cedulaTran;
    
    
    public void realizarDeposito(int numCuentaManda, double montoDepositar,int numCuentaRecibe, double saldoActualCuentaManda, int saldoCuentaRecibe){
        if(saldoActualCuentaManda> montoDepositar){
            saldoCuentaRecibe+=montoDepositar;
            saldoActualCuentaManda-=montoDepositar;
        }else{
            JOptionPane.showMessageDialog(null, "Fondos insuficientes");
        }

    }
    public void realizarRetiro(int numCuenta, double montoRetirar, double saldoActual){
    }
}



