/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import controlador.exceptions.PreexistingEntityException;
import entidad.Cuenta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidad.Usuario;
import entidad.Transaccion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author wposs
 */
public class CuentaJpaController implements Serializable {

    public CuentaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("BancoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuenta cuenta) throws PreexistingEntityException, Exception {
        if (cuenta.getTransaccionList() == null) {
            cuenta.setTransaccionList(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario cedUsuario = cuenta.getCedUsuario();
            if (cedUsuario != null) {
                cedUsuario = em.getReference(cedUsuario.getClass(), cedUsuario.getCedulaUsuario());
                cuenta.setCedUsuario(cedUsuario);
            }
            List<Transaccion> attachedTransaccionList = new ArrayList<Transaccion>();
            for (Transaccion transaccionListTransaccionToAttach : cuenta.getTransaccionList()) {
                transaccionListTransaccionToAttach = em.getReference(transaccionListTransaccionToAttach.getClass(), transaccionListTransaccionToAttach.getIdTransaccion());
                attachedTransaccionList.add(transaccionListTransaccionToAttach);
            }
            cuenta.setTransaccionList(attachedTransaccionList);
            em.persist(cuenta);
            if (cedUsuario != null) {
                cedUsuario.getCuentaList().add(cuenta);
                cedUsuario = em.merge(cedUsuario);
            }
            for (Transaccion transaccionListTransaccion : cuenta.getTransaccionList()) {
                Cuenta oldNumCuentaOfTransaccionListTransaccion = transaccionListTransaccion.getNumCuenta();
                transaccionListTransaccion.setNumCuenta(cuenta);
                transaccionListTransaccion = em.merge(transaccionListTransaccion);
                if (oldNumCuentaOfTransaccionListTransaccion != null) {
                    oldNumCuentaOfTransaccionListTransaccion.getTransaccionList().remove(transaccionListTransaccion);
                    oldNumCuentaOfTransaccionListTransaccion = em.merge(oldNumCuentaOfTransaccionListTransaccion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCuenta(cuenta.getNumCuenta()) != null) {
                throw new PreexistingEntityException("Cuenta " + cuenta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cuenta cuenta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuenta persistentCuenta = em.find(Cuenta.class, cuenta.getNumCuenta());
            Usuario cedUsuarioOld = persistentCuenta.getCedUsuario();
            Usuario cedUsuarioNew = cuenta.getCedUsuario();
            List<Transaccion> transaccionListOld = persistentCuenta.getTransaccionList();
            List<Transaccion> transaccionListNew = cuenta.getTransaccionList();
            List<String> illegalOrphanMessages = null;
            for (Transaccion transaccionListOldTransaccion : transaccionListOld) {
                if (!transaccionListNew.contains(transaccionListOldTransaccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaccion " + transaccionListOldTransaccion + " since its numCuenta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cedUsuarioNew != null) {
                cedUsuarioNew = em.getReference(cedUsuarioNew.getClass(), cedUsuarioNew.getCedulaUsuario());
                cuenta.setCedUsuario(cedUsuarioNew);
            }
            List<Transaccion> attachedTransaccionListNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionListNewTransaccionToAttach : transaccionListNew) {
                transaccionListNewTransaccionToAttach = em.getReference(transaccionListNewTransaccionToAttach.getClass(), transaccionListNewTransaccionToAttach.getIdTransaccion());
                attachedTransaccionListNew.add(transaccionListNewTransaccionToAttach);
            }
            transaccionListNew = attachedTransaccionListNew;
            cuenta.setTransaccionList(transaccionListNew);
            cuenta = em.merge(cuenta);
            if (cedUsuarioOld != null && !cedUsuarioOld.equals(cedUsuarioNew)) {
                cedUsuarioOld.getCuentaList().remove(cuenta);
                cedUsuarioOld = em.merge(cedUsuarioOld);
            }
            if (cedUsuarioNew != null && !cedUsuarioNew.equals(cedUsuarioOld)) {
                cedUsuarioNew.getCuentaList().add(cuenta);
                cedUsuarioNew = em.merge(cedUsuarioNew);
            }
            for (Transaccion transaccionListNewTransaccion : transaccionListNew) {
                if (!transaccionListOld.contains(transaccionListNewTransaccion)) {
                    Cuenta oldNumCuentaOfTransaccionListNewTransaccion = transaccionListNewTransaccion.getNumCuenta();
                    transaccionListNewTransaccion.setNumCuenta(cuenta);
                    transaccionListNewTransaccion = em.merge(transaccionListNewTransaccion);
                    if (oldNumCuentaOfTransaccionListNewTransaccion != null && !oldNumCuentaOfTransaccionListNewTransaccion.equals(cuenta)) {
                        oldNumCuentaOfTransaccionListNewTransaccion.getTransaccionList().remove(transaccionListNewTransaccion);
                        oldNumCuentaOfTransaccionListNewTransaccion = em.merge(oldNumCuentaOfTransaccionListNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cuenta.getNumCuenta();
                if (findCuenta(id) == null) {
                    throw new NonexistentEntityException("The cuenta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cuenta cuenta;
            try {
                cuenta = em.getReference(Cuenta.class, id);
                cuenta.getNumCuenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuenta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Transaccion> transaccionListOrphanCheck = cuenta.getTransaccionList();
            for (Transaccion transaccionListOrphanCheckTransaccion : transaccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cuenta (" + cuenta + ") cannot be destroyed since the Transaccion " + transaccionListOrphanCheckTransaccion + " in its transaccionList field has a non-nullable numCuenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario cedUsuario = cuenta.getCedUsuario();
            if (cedUsuario != null) {
                cedUsuario.getCuentaList().remove(cuenta);
                cedUsuario = em.merge(cedUsuario);
            }
            em.remove(cuenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cuenta> findCuentaEntities() {
        return findCuentaEntities(true, -1, -1);
    }

    public List<Cuenta> findCuentaEntities(int maxResults, int firstResult) {
        return findCuentaEntities(false, maxResults, firstResult);
    }

    private List<Cuenta> findCuentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuenta.class));
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

    public Cuenta findCuenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuenta> rt = cq.from(Cuenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
