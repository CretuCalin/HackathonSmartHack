/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alex_
 */
@Entity
@Table(name = "borrow")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BorrowDB.findAll", query = "SELECT b FROM BorrowDB b"),
    @NamedQuery(name = "BorrowDB.findById", query = "SELECT b FROM BorrowDB b WHERE b.id = :id"),
    @NamedQuery(name = "BorrowDB.findByUserAccount", query = "SELECT b FROM BorrowDB b WHERE b.userAccount = :userAccount"),
    @NamedQuery(name = "BorrowDB.findByBook", query = "SELECT b FROM BorrowDB b WHERE b.book = :book"),
    @NamedQuery(name = "BorrowDB.findByDate", query = "SELECT b FROM BorrowDB b WHERE b.date = :date"),
    @NamedQuery(name = "BorrowDB.findByExpiration", query = "SELECT b FROM BorrowDB b WHERE b.expiration = :expiration"),
    @NamedQuery(name = "BorrowDB.findByExtended", query = "SELECT b FROM BorrowDB b WHERE b.extended = :extended"),
    @NamedQuery(name = "BorrowDB.findByUserBook", query = "SELECT b FROM BorrowDB b WHERE b.userAccount = :userAccount AND b.book = :book")})
public class BorrowDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_account")
    private int userAccount;
    @Basic(optional = false)
    @Column(name = "book")
    private int book;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;
    @Basic(optional = false)
    @Column(name = "extended")
    private boolean extended;

    public BorrowDB() {
    }

    public BorrowDB(Integer id) {
        this.id = id;
    }

    public BorrowDB(Integer id, int userAccount, int book, Date date, Date expiration, boolean extended) {
        this.id = id;
        this.userAccount = userAccount;
        this.book = book;
        this.date = date;
        this.expiration = expiration;
        this.extended = extended;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(int userAccount) {
        this.userAccount = userAccount;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean getExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BorrowDB)) {
            return false;
        }
        BorrowDB other = (BorrowDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.BorrowDB[ id=" + id + " ]";
    }

}
