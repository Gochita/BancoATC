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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "tipotransaccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipotransaccion.findAll", query = "SELECT t FROM Tipotransaccion t")
    , @NamedQuery(name = "Tipotransaccion.findByIdTipo", query = "SELECT t FROM Tipotransaccion t WHERE t.idTipo = :idTipo")
    , @NamedQuery(name = "Tipotransaccion.findByTipo", query = "SELECT t FROM Tipotransaccion t WHERE t.tipo = :tipo")})
public class Tipotransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idTipo", nullable = false)
    private Integer idTipo;
    @Basic(optional = false)
    @Column(name = "tipo", nullable = false, length = 11)
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo")
    private List<Transaccion> transaccionList;

    public Tipotransaccion() {
    }

    public Tipotransaccion(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public Tipotransaccion(Integer idTipo, String tipo) {
        this.idTipo = idTipo;
        this.tipo = tipo;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<Transaccion> getTransaccionList() {
        return transaccionList;
    }

    public void setTransaccionList(List<Transaccion> transaccionList) {
        this.transaccionList = transaccionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipo != null ? idTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipotransaccion)) {
            return false;
        }
        Tipotransaccion other = (Tipotransaccion) object;
        if ((this.idTipo == null && other.idTipo != null) || (this.idTipo != null && !this.idTipo.equals(other.idTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Tipotransaccion[ idTipo=" + idTipo + " ]";
    }
    
}
