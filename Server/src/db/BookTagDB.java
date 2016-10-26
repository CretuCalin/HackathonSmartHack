/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alex_
 */
@Entity
@Table(name = "book_tag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookTagDB.findAll", query = "SELECT b FROM BookTagDB b"),
    @NamedQuery(name = "BookTagDB.findById", query = "SELECT b FROM BookTagDB b WHERE b.id = :id"),
    @NamedQuery(name = "BookTagDB.findByBook", query = "SELECT b FROM BookTagDB b WHERE b.book = :book"),
    @NamedQuery(name = "BookTagDB.findByTag", query = "SELECT b FROM BookTagDB b WHERE b.tag = :tag")})
public class BookTagDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "book")
    private int book;
    @Basic(optional = false)
    @Column(name = "tag")
    private int tag;

    public BookTagDB() {
    }

    public BookTagDB(Integer id) {
        this.id = id;
    }

    public BookTagDB(Integer id, int book, int tag) {
        this.id = id;
        this.book = book;
        this.tag = tag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
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
        if (!(object instanceof BookTagDB)) {
            return false;
        }
        BookTagDB other = (BookTagDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.BookTagDB[ id=" + id + " ]";
    }
    
}
