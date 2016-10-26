/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import db.UserAccountDB;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author alex_
 */
public class UserAccountDBJpaController implements Serializable {

    public UserAccountDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UserAccountDB userAccountDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(userAccountDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UserAccountDB userAccountDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            userAccountDB = em.merge(userAccountDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = userAccountDB.getId();
                if (findUserAccountDB(id) == null) {
                    throw new NonexistentEntityException("The userAccountDB with id " + id + " no longer exists.");
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
            UserAccountDB userAccountDB;
            try {
                userAccountDB = em.getReference(UserAccountDB.class, id);
                userAccountDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The userAccountDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(userAccountDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UserAccountDB> findUserAccountDBEntities() {
        return findUserAccountDBEntities(true, -1, -1);
    }

    public List<UserAccountDB> findUserAccountDBEntities(int maxResults, int firstResult) {
        return findUserAccountDBEntities(false, maxResults, firstResult);
    }

    private List<UserAccountDB> findUserAccountDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserAccountDB.class));
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

    public UserAccountDB findUserAccountDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UserAccountDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserAccountDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UserAccountDB> rt = cq.from(UserAccountDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public UserAccountDB findUserByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            Query nq = (Query) em.createNamedQuery("UserAccountDB.findByUsername");
            nq.setParameter("username", username);
            return (UserAccountDB) nq.getSingleResult();
        } catch (NoResultException ex) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public List<UserAccountDB> searchByName(String name) {
        EntityManager em = getEntityManager();
        try {
            Query q = (Query) em.createNamedQuery("UserAccountDB.searchByName");
            q.setParameter("name", "%" + name + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
