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
@Table(name = "tag")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TagDB.findAll", query = "SELECT t FROM TagDB t"),
    @NamedQuery(name = "TagDB.findById", query = "SELECT t FROM TagDB t WHERE t.id = :id"),
    @NamedQuery(name = "TagDB.findByName", query = "SELECT t FROM TagDB t WHERE t.name = :name"),
    @NamedQuery(name = "TagDB.findByUser", query = "SELECT t FROM TagDB t, TagSettingDB s WHERE s.userAccount = :user AND t.id = s.tag"),
    @NamedQuery(name = "TagDB.findByBook", query = "SELECT t FROM TagDB t, BookTagDB s WHERE s.book = :book AND t.id = s.tag")})
public class TagDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;

    public TagDB() {
    }

    public TagDB(Integer id) {
        this.id = id;
    }

    public TagDB(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof TagDB)) {
            return false;
        }
        TagDB other = (TagDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.TagDB[ id=" + id + " ]";
    }

}
