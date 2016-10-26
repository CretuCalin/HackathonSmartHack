/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import db.BookDB;
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
public class BookDBJpaController implements Serializable {

    public BookDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BookDB bookDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(bookDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BookDB bookDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            bookDB = em.merge(bookDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bookDB.getId();
                if (findBookDB(id) == null) {
                    throw new NonexistentEntityException("The bookDB with id " + id + " no longer exists.");
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
            BookDB bookDB;
            try {
                bookDB = em.getReference(BookDB.class, id);
                bookDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(bookDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BookDB> findBookDBEntities() {
        return findBookDBEntities(true, -1, -1);
    }

    public List<BookDB> findBookDBEntities(int maxResults, int firstResult) {
        return findBookDBEntities(false, maxResults, firstResult);
    }

    private List<BookDB> findBookDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BookDB.class));
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

    public BookDB findBookDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BookDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BookDB> rt = cq.from(BookDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<BookDB> findBooksByUser(int user) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("BookDB.findByUser");
            q.setParameter("user", user);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<BookDB> findBooksByTag(int tag) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("BookDB.findByTag");
            q.setParameter("tag", tag);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Integer> findBookIdsByTag(int tag) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("BookDB.findIdByTag");
            q.setParameter("tag", tag);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    List<BookDB> searchByTitle(String title) {
        EntityManager em = getEntityManager();
        try {
            Query q = (Query) em.createNamedQuery("BookDB.searchByTitle");
            q.setParameter("title", "%" + title + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public String findBookName(int book) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT b.title FROM BookDB b WHERE b.id = :book");
            q.setParameter("book", book);
            return (String) q.getSingleResult();
        } catch (Exception e) {
        } finally {
            em.close();
        }
        return null;
    }
}
