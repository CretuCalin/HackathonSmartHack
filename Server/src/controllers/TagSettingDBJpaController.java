/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import db.TagSettingDB;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author alex_
 */
public class TagSettingDBJpaController implements Serializable {

    public TagSettingDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TagSettingDB tagSettingDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tagSettingDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TagSettingDB tagSettingDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tagSettingDB = em.merge(tagSettingDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tagSettingDB.getId();
                if (findTagSettingDB(id) == null) {
                    throw new NonexistentEntityException("The tagSettingDB with id " + id + " no longer exists.");
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
            TagSettingDB tagSettingDB;
            try {
                tagSettingDB = em.getReference(TagSettingDB.class, id);
                tagSettingDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tagSettingDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(tagSettingDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TagSettingDB> findTagSettingDBEntities() {
        return findTagSettingDBEntities(true, -1, -1);
    }

    public List<TagSettingDB> findTagSettingDBEntities(int maxResults, int firstResult) {
        return findTagSettingDBEntities(false, maxResults, firstResult);
    }

    private List<TagSettingDB> findTagSettingDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TagSettingDB.class));
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

    public TagSettingDB findTagSettingDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TagSettingDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getTagSettingDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TagSettingDB> rt = cq.from(TagSettingDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    List<TagSettingDB> findTagSettingsByUser(int user) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("TagSettingDB.findByUserAccount");
            q.setParameter("userAccount", user);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
