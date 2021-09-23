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
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByNombreUsuario", query = "SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    , @NamedQuery(name = "Usuario.findByCorreoUsuario", query = "SELECT u FROM Usuario u WHERE u.correoUsuario = :correoUsuario")
    , @NamedQuery(name = "Usuario.findByContrase\u00f1aUsuario", query = "SELECT u FROM Usuario u WHERE u.contrase\u00f1aUsuario = :contrase\u00f1aUsuario")
    , @NamedQuery(name = "Usuario.findByCedulaUsuario", query = "SELECT u FROM Usuario u WHERE u.cedulaUsuario = :cedulaUsuario")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "NombreUsuario", nullable = false, length = 50)
    private String nombreUsuario;
    @Basic(optional = false)
    @Column(name = "CorreoUsuario", nullable = false, length = 50)
    private String correoUsuario;
    @Basic(optional = false)
    @Column(name = "Contrase\u00f1aUsuario", nullable = false, length = 50)
    private String contraseñaUsuario;
    @Id
    @Basic(optional = false)
    @Column(name = "CedulaUsuario", nullable = false)
    private Integer cedulaUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cedUsuario")
    private List<Cuenta> cuentaList;

    public Usuario() {
    }

    public Usuario(Integer cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public Usuario(Integer cedulaUsuario, String nombreUsuario, String correoUsuario, String contraseñaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.contraseñaUsuario = contraseñaUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getContraseñaUsuario() {
        return contraseñaUsuario;
    }

    public void setContraseñaUsuario(String contraseñaUsuario) {
        this.contraseñaUsuario = contraseñaUsuario;
    }

    public Integer getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(Integer cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    @XmlTransient
    public List<Cuenta> getCuentaList() {
        return cuentaList;
    }

    public void setCuentaList(List<Cuenta> cuentaList) {
        this.cuentaList = cuentaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedulaUsuario != null ? cedulaUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.cedulaUsuario == null && other.cedulaUsuario != null) || (this.cedulaUsuario != null && !this.cedulaUsuario.equals(other.cedulaUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidad.Usuario[ cedulaUsuario=" + cedulaUsuario + " ]";
    }
    
}
