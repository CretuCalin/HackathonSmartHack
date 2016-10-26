/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import db.BookTagDB;
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
public class BookTagDBJpaController implements Serializable {

    public BookTagDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BookTagDB bookTagDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(bookTagDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BookTagDB bookTagDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            bookTagDB = em.merge(bookTagDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bookTagDB.getId();
                if (findBookTagDB(id) == null) {
                    throw new NonexistentEntityException("The bookTagDB with id " + id + " no longer exists.");
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
            BookTagDB bookTagDB;
            try {
                bookTagDB = em.getReference(BookTagDB.class, id);
                bookTagDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookTagDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(bookTagDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BookTagDB> findBookTagDBEntities() {
        return findBookTagDBEntities(true, -1, -1);
    }

    public List<BookTagDB> findBookTagDBEntities(int maxResults, int firstResult) {
        return findBookTagDBEntities(false, maxResults, firstResult);
    }

    private List<BookTagDB> findBookTagDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BookTagDB.class));
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

    public BookTagDB findBookTagDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BookTagDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookTagDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BookTagDB> rt = cq.from(BookTagDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<BookTagDB> findBookTagByBook(int book) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("BookTagDB.findByBook");
            q.setParameter("book", book);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
