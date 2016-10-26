/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import db.BorrowDB;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author alex_
 */
public class BorrowDBJpaController implements Serializable {

    public BorrowDBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BorrowDB borrowDB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(borrowDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BorrowDB borrowDB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            borrowDB = em.merge(borrowDB);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = borrowDB.getId();
                if (findBorrowDB(id) == null) {
                    throw new NonexistentEntityException("The borrowDB with id " + id + " no longer exists.");
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
            BorrowDB borrowDB;
            try {
                borrowDB = em.getReference(BorrowDB.class, id);
                borrowDB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The borrowDB with id " + id + " no longer exists.", enfe);
            }
            em.remove(borrowDB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BorrowDB> findBorrowDBEntities() {
        return findBorrowDBEntities(true, -1, -1);
    }

    public List<BorrowDB> findBorrowDBEntities(int maxResults, int firstResult) {
        return findBorrowDBEntities(false, maxResults, firstResult);
    }

    private List<BorrowDB> findBorrowDBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BorrowDB.class));
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

    public BorrowDB findBorrowDB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BorrowDB.class, id);
        } finally {
            em.close();
        }
    }

    public int getBorrowDBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BorrowDB> rt = cq.from(BorrowDB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<BorrowDB> findBorrowsByUser(int user) {
        EntityManager em = getEntityManager();
        try {
            Query nq = (Query) em.createNamedQuery("BorrowDB.findByUserAccount");
            nq.setParameter("userAccount", user);
            return nq.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public BorrowDB findBorrowsByUserBook(int user, int book) {
        EntityManager em = getEntityManager();
        try {
            Query q = (Query) em.createNamedQuery("BorrowDB.findByUserBook");
            q.setParameter("userAccount", user);
            q.setParameter("book", book);
            return (BorrowDB) q.getSingleResult();
        } catch (NoResultException ex) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return null;
    }

    public int countByBook(int book) {
        EntityManager em = getEntityManager();
        try {
            Query q = (Query) em.createQuery("SELECT count(b) FROM BorrowDB b WHERE b.book = :id");
            q.setParameter("id", book);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return -1;
    }

    public int countByUser(int user) {
        EntityManager em = getEntityManager();
        try {
            Query q = (Query) em.createQuery("SELECT count(b) FROM BorrowDB b WHERE b.userAccount = :id");
            q.setParameter("id", user);
            return ((Long) q.getSingleResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return -1;
    }
}
