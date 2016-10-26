/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import db.TagDB;
import db.UserAccountDB;
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
public class TagDBJpaController implements Serializable {

    public TagDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TagDB tagDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tagDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TagDB tagDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tagDB = em.merge(tagDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tagDB.getId();
                if (findTagDB(id) == null) {
                    throw new NonexistentEntityException("The tagDB with id " + id + " no longer exists.");
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
            TagDB tagDB;
            try {
                tagDB = em.getReference(TagDB.class, id);
                tagDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tagDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(tagDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TagDB> findTagDBEntities() {
        return findTagDBEntities(true, -1, -1);
    }

    public List<TagDB> findTagDBEntities(int maxResults, int firstResult) {
        return findTagDBEntities(false, maxResults, firstResult);
    }

    private List<TagDB> findTagDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TagDB.class));
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

    public TagDB findTagDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TagDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getTagDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TagDB> rt = cq.from(TagDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<TagDB> findTagsByUser(int user) {
        EntityManager em = getEntityManager();
        try {
            Query nq = (Query) em.createNamedQuery("TagDB.findByUser");
            nq.setParameter("user", user);
            return nq.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public List<TagDB> findTagsByBook(int book) {
        EntityManager em = getEntityManager();
        try {
            Query nq = (Query) em.createNamedQuery("TagDB.findByBook");
            nq.setParameter("book", book);
            return nq.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }
}
