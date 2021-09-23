/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import controlador.exceptions.IllegalOrphanException;
import controlador.exceptions.NonexistentEntityException;
import entidad.Tipotransaccion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
public class TipotransaccionJpaController implements Serializable {

    public TipotransaccionJpaController() {
        this.emf = Persistence.createEntityManagerFactory("BancoPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipotransaccion tipotransaccion) {
        if (tipotransaccion.getTransaccionList() == null) {
            tipotransaccion.setTransaccionList(new ArrayList<Transaccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Transaccion> attachedTransaccionList = new ArrayList<Transaccion>();
            for (Transaccion transaccionListTransaccionToAttach : tipotransaccion.getTransaccionList()) {
                transaccionListTransaccionToAttach = em.getReference(transaccionListTransaccionToAttach.getClass(), transaccionListTransaccionToAttach.getIdTransaccion());
                attachedTransaccionList.add(transaccionListTransaccionToAttach);
            }
            tipotransaccion.setTransaccionList(attachedTransaccionList);
            em.persist(tipotransaccion);
            for (Transaccion transaccionListTransaccion : tipotransaccion.getTransaccionList()) {
                Tipotransaccion oldTipoOfTransaccionListTransaccion = transaccionListTransaccion.getTipo();
                transaccionListTransaccion.setTipo(tipotransaccion);
                transaccionListTransaccion = em.merge(transaccionListTransaccion);
                if (oldTipoOfTransaccionListTransaccion != null) {
                    oldTipoOfTransaccionListTransaccion.getTransaccionList().remove(transaccionListTransaccion);
                    oldTipoOfTransaccionListTransaccion = em.merge(oldTipoOfTransaccionListTransaccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipotransaccion tipotransaccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tipotransaccion persistentTipotransaccion = em.find(Tipotransaccion.class, tipotransaccion.getIdTipo());
            List<Transaccion> transaccionListOld = persistentTipotransaccion.getTransaccionList();
            List<Transaccion> transaccionListNew = tipotransaccion.getTransaccionList();
            List<String> illegalOrphanMessages = null;
            for (Transaccion transaccionListOldTransaccion : transaccionListOld) {
                if (!transaccionListNew.contains(transaccionListOldTransaccion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Transaccion " + transaccionListOldTransaccion + " since its tipo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Transaccion> attachedTransaccionListNew = new ArrayList<Transaccion>();
            for (Transaccion transaccionListNewTransaccionToAttach : transaccionListNew) {
                transaccionListNewTransaccionToAttach = em.getReference(transaccionListNewTransaccionToAttach.getClass(), transaccionListNewTransaccionToAttach.getIdTransaccion());
                attachedTransaccionListNew.add(transaccionListNewTransaccionToAttach);
            }
            transaccionListNew = attachedTransaccionListNew;
            tipotransaccion.setTransaccionList(transaccionListNew);
            tipotransaccion = em.merge(tipotransaccion);
            for (Transaccion transaccionListNewTransaccion : transaccionListNew) {
                if (!transaccionListOld.contains(transaccionListNewTransaccion)) {
                    Tipotransaccion oldTipoOfTransaccionListNewTransaccion = transaccionListNewTransaccion.getTipo();
                    transaccionListNewTransaccion.setTipo(tipotransaccion);
                    transaccionListNewTransaccion = em.merge(transaccionListNewTransaccion);
                    if (oldTipoOfTransaccionListNewTransaccion != null && !oldTipoOfTransaccionListNewTransaccion.equals(tipotransaccion)) {
                        oldTipoOfTransaccionListNewTransaccion.getTransaccionList().remove(transaccionListNewTransaccion);
                        oldTipoOfTransaccionListNewTransaccion = em.merge(oldTipoOfTransaccionListNewTransaccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipotransaccion.getIdTipo();
                if (findTipotransaccion(id) == null) {
                    throw new NonexistentEntityException("The tipotransaccion with id " + id + " no longer exists.");
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
            Tipotransaccion tipotransaccion;
            try {
                tipotransaccion = em.getReference(Tipotransaccion.class, id);
                tipotransaccion.getIdTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipotransaccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Transaccion> transaccionListOrphanCheck = tipotransaccion.getTransaccionList();
            for (Transaccion transaccionListOrphanCheckTransaccion : transaccionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipotransaccion (" + tipotransaccion + ") cannot be destroyed since the Transaccion " + transaccionListOrphanCheckTransaccion + " in its transaccionList field has a non-nullable tipo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipotransaccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipotransaccion> findTipotransaccionEntities() {
        return findTipotransaccionEntities(true, -1, -1);
    }

    public List<Tipotransaccion> findTipotransaccionEntities(int maxResults, int firstResult) {
        return findTipotransaccionEntities(false, maxResults, firstResult);
    }

    private List<Tipotransaccion> findTipotransaccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipotransaccion.class));
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

    public Tipotransaccion findTipotransaccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipotransaccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipotransaccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipotransaccion> rt = cq.from(Tipotransaccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
