/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import db.AdminDB;
import db.UserAccountDB;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Admin;

/**
 *
 * @author alex_
 */
public class AdminDBJpaController implements Serializable {

    public AdminDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AdminDB adminDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(adminDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AdminDB adminDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            adminDB = em.merge(adminDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = adminDB.getId();
                if (findAdminDB(id) == null) {
                    throw new NonexistentEntityException("The adminDB with id " + id + " no longer exists.");
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
            AdminDB adminDB;
            try {
                adminDB = em.getReference(AdminDB.class, id);
                adminDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adminDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(adminDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AdminDB> findAdminDBEntities() {
        return findAdminDBEntities(true, -1, -1);
    }

    public List<AdminDB> findAdminDBEntities(int maxResults, int firstResult) {
        return findAdminDBEntities(false, maxResults, firstResult);
    }

    private List<AdminDB> findAdminDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AdminDB.class));
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

    public AdminDB findAdminDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AdminDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdminDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AdminDB> rt = cq.from(AdminDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public AdminDB findAdminByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            Query nq = (Query) em.createNamedQuery("AdminDB.findByUsername");
            nq.setParameter("username", username);
            return (AdminDB) nq.getSingleResult();
        } catch (NoResultException ex) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

}
