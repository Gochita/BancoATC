/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author wposs
 */
@Entity
@Table(name = "transaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transaccion.findAll", query = "SELECT t FROM Transaccion t")
    , @NamedQuery(name = "Transaccion.findByIdTransaccion", query = "SELECT t FROM Transaccion t WHERE t.idTransaccion = :idTransaccion")
    , @NamedQuery(name = "Transaccion.findByNumDestino", query = "SELECT t FROM Transaccion t WHERE t.numDestino = :numDestino")
    , @NamedQuery(name = "Transaccion.findByMontoTransaccion", query = "SELECT t FROM Transaccion t WHERE t.montoTransaccion = :montoTransaccion")})
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTransaccion", nullable = false)
    private Integer idTransaccion;
    @Column(name = "numDestino")
    private Integer numDestino;
    @Basic(optional = false)
    @Column(name = "montoTransaccion", nullable = false)
    private double montoTransaccion;
    @JoinColumn(name = "numCuenta", referencedColumnName = "numCuenta", nullable = false)
    @ManyToOne(optional = false)
    private Cuenta numCuenta;
    @JoinColumn(name = "tipo", referencedColumnName = "idTipo", nullable = false)
    @ManyToOne(optional = false)
    private Tipotransaccion tipo;

    public Transaccion() {
    }

    public Transaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Transaccion(Integer idTransaccion, double montoTransaccion) {
        this.idTransaccion = idTransaccion;
        this.montoTransaccion = montoTransaccion;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Integer getNumDestino() {
        return numDestino;
    }

    public void setNumDestino(Integer numDestino) {
        this.numDestino = numDestino;
    }

    public double getMontoTransaccion() {
        return montoTransaccion;
    }

    public void setMontoTransaccion(double montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public Cuenta getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(Cuenta numCuenta) {
        this.numCuenta = numCuenta;
    }

    public Tipotransaccion getTipo() {
        return tipo;
    }

    public void setTipo(Tipotransaccion tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTransaccion != null ? idTransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.idTransaccion == null && other.idTransaccion != null) || (this.idTransaccion != null && !this.idTransaccion.equals(other.idTransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Transaccion[ idTransaccion=" + idTransaccion + " ]";
    }
    
}
