/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Cuenta;
import entidad.Tipotransaccion;
import entidad.Transaccion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author wposs
 */
public class TransaccionJpaController implements Serializable {

    public TransaccionJpaController() {
        this.emf = Persistence.createEntityManagerFactory("BancoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaccion transaccion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuenta numCuenta = transaccion.getNumCuenta();
            if (numCuenta != null) {
                numCuenta = em.getReference(numCuenta.getClass(), numCuenta.getNumCuenta());
                transaccion.setNumCuenta(numCuenta);
            }
            Tipotransaccion tipo = transaccion.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getIdTipo());
                transaccion.setTipo(tipo);
            }
            em.persist(transaccion);
            if (numCuenta != null) {
                numCuenta.getTransaccionList().add(transaccion);
                numCuenta = em.merge(numCuenta);
            }
            if (tipo != null) {
                tipo.getTransaccionList().add(transaccion);
                tipo = em.merge(tipo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaccion transaccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaccion persistentTransaccion = em.find(Transaccion.class, transaccion.getIdTransaccion());
            Cuenta numCuentaOld = persistentTransaccion.getNumCuenta();
            Cuenta numCuentaNew = transaccion.getNumCuenta();
            Tipotransaccion tipoOld = persistentTransaccion.getTipo();
            Tipotransaccion tipoNew = transaccion.getTipo();
            if (numCuentaNew != null) {
                numCuentaNew = em.getReference(numCuentaNew.getClass(), numCuentaNew.getNumCuenta());
                transaccion.setNumCuenta(numCuentaNew);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getIdTipo());
                transaccion.setTipo(tipoNew);
            }
            transaccion = em.merge(transaccion);
            if (numCuentaOld != null && !numCuentaOld.equals(numCuentaNew)) {
                numCuentaOld.getTransaccionList().remove(transaccion);
                numCuentaOld = em.merge(numCuentaOld);
            }
            if (numCuentaNew != null && !numCuentaNew.equals(numCuentaOld)) {
                numCuentaNew.getTransaccionList().add(transaccion);
                numCuentaNew = em.merge(numCuentaNew);
            }
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getTransaccionList().remove(transaccion);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getTransaccionList().add(transaccion);
                tipoNew = em.merge(tipoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = transaccion.getIdTransaccion();
                if (findTransaccion(id) == null) {
                    throw new NonexistentEntityException("The transaccion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaccion transaccion;
            try {
                transaccion = em.getReference(Transaccion.class, id);
                transaccion.getIdTransaccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaccion with id " + id + " no longer exists.", enfe);
            }
            Cuenta numCuenta = transaccion.getNumCuenta();
            if (numCuenta != null) {
                numCuenta.getTransaccionList().remove(transaccion);
                numCuenta = em.merge(numCuenta);
            }
            Tipotransaccion tipo = transaccion.getTipo();
            if (tipo != null) {
                tipo.getTransaccionList().remove(transaccion);
                tipo = em.merge(tipo);
            }
            em.remove(transaccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaccion> findTransaccionEntities() {
        return findTransaccionEntities(true, -1, -1);
    }

    public List<Transaccion> findTransaccionEntities(int maxResults, int firstResult) {
        return findTransaccionEntities(false, maxResults, firstResult);
    }

    private List<Transaccion> findTransaccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaccion.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Transaccion findTransaccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransaccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaccion> rt = cq.from(Transaccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
