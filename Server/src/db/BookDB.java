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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alex_
 */
@Entity
@Table(name = "book")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BookDB.findAll", query = "SELECT b FROM BookDB b"),
    @NamedQuery(name = "BookDB.findById", query = "SELECT b FROM BookDB b WHERE b.id = :id"),
    @NamedQuery(name = "BookDB.findByTitle", query = "SELECT b FROM BookDB b WHERE b.title = :title"),
    @NamedQuery(name = "BookDB.findByAuthor", query = "SELECT b FROM BookDB b WHERE b.author = :author"),
    @NamedQuery(name = "BookDB.findByUser", query = "SELECT b FROM BookDB b, UserAccountDB u, BorrowDB i WHERE u.id = :user AND i.userAccount = u.id AND b.id = i.book"),
    @NamedQuery(name = "BookDB.findByTag", query = "SELECT b FROM BookDB b, BookTagDB c, TagDB t WHERE c.book = b.id AND c.tag = t.id AND t.id = :tag"),
    @NamedQuery(name = "BookDB.findIdByTag", query = "SELECT b.id FROM BookDB b, BookTagDB c, TagDB t WHERE c.book = b.id AND c.tag = t.id AND t.id = :tag"),
    @NamedQuery(name = "BookDB.searchByTitle", query = "SELECT b FROM BookDB b WHERE b.title LIKE :title")})
public class BookDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "author")
    private String author;
    @Basic(optional = false)
    @Lob
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Lob
    @Column(name = "image")
    private byte[] image;

    public BookDB() {
    }

    public BookDB(Integer id) {
        this.id = id;
    }

    public BookDB(Integer id, String title, String author, String description, byte[] image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
        if (!(object instanceof BookDB)) {
            return false;
        }
        BookDB other = (BookDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.BookDB[ id=" + id + " ]";
    }

}
