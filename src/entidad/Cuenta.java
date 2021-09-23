/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidad;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author wposs
 */
@Entity
@Table(name = "cuenta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuenta.findAll", query = "SELECT c FROM Cuenta c")
    , @NamedQuery(name = "Cuenta.findByNumCuenta", query = "SELECT c FROM Cuenta c WHERE c.numCuenta = :numCuenta")
    , @NamedQuery(name = "Cuenta.findBySaldoActual", query = "SELECT c FROM Cuenta c WHERE c.saldoActual = :saldoActual")
    , @NamedQuery(name = "Cuenta.findByEstado", query = "SELECT c FROM Cuenta c WHERE c.estado = :estado")})
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "numCuenta", nullable = false)
    private Integer numCuenta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "saldoActual", precision = 22)
    private Double saldoActual;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "numCuenta")
    private List<Transaccion> transaccionList;
    @JoinColumn(name = "cedUsuario", referencedColumnName = "CedulaUsuario", nullable = false)
    @ManyToOne(optional = false)
    private Usuario cedUsuario;

    public Cuenta() {
    }

    public Cuenta(Integer numCuenta) {
        this.numCuenta = numCuenta;
    }

    public Integer getNumCuenta() {
        return numCuenta;
    }

    public void setNumCuenta(Integer numCuenta) {
        this.numCuenta = numCuenta;
    }

    public Double getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(Double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Transaccion> getTransaccionList() {
        return transaccionList;
    }

    public void setTransaccionList(List<Transaccion> transaccionList) {
        this.transaccionList = transaccionList;
    }

    public Usuario getCedUsuario() {
        return cedUsuario;
    }

    public void setCedUsuario(Usuario cedUsuario) {
        this.cedUsuario = cedUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (numCuenta != null ? numCuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cuenta)) {
            return false;
        }
        Cuenta other = (Cuenta) object;
        if ((this.numCuenta == null && other.numCuenta != null) || (this.numCuenta != null && !this.numCuenta.equals(other.numCuenta))) {
            return false;
        }
        return true;
    }
     public int CrearCuenta(){
        int numero = (int)(Math.random()*1000000000);
        return numero=(Integer.parseInt("0"+numero));
        
    
    }

    @Override
    public String toString() {
        return "entidad.Cuenta[ numCuenta=" + numCuenta + " ]";
    }
    
}
