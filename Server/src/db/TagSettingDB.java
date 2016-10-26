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
@Table(name = "tag_setting")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TagSettingDB.findAll", query = "SELECT t FROM TagSettingDB t"),
    @NamedQuery(name = "TagSettingDB.findById", query = "SELECT t FROM TagSettingDB t WHERE t.id = :id"),
    @NamedQuery(name = "TagSettingDB.findByUserAccount", query = "SELECT t FROM TagSettingDB t WHERE t.userAccount = :userAccount"),
    @NamedQuery(name = "TagSettingDB.findByTag", query = "SELECT t FROM TagSettingDB t WHERE t.tag = :tag")})
public class TagSettingDB implements Serializable {

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
    @Column(name = "tag")
    private int tag;

    public TagSettingDB() {
    }

    public TagSettingDB(Integer id) {
        this.id = id;
    }

    public TagSettingDB(Integer id, int userAccount, int tag) {
        this.id = id;
        this.userAccount = userAccount;
        this.tag = tag;
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
        if (!(object instanceof TagSettingDB)) {
            return false;
        }
        TagSettingDB other = (TagSettingDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.TagSettingDB[ id=" + id + " ]";
    }
    
}
