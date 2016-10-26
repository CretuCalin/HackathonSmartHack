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
@Table(name = "user_account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserAccountDB.findAll", query = "SELECT u FROM UserAccountDB u"),
    @NamedQuery(name = "UserAccountDB.findById", query = "SELECT u FROM UserAccountDB u WHERE u.id = :id"),
    @NamedQuery(name = "UserAccountDB.findByUsername", query = "SELECT u FROM UserAccountDB u WHERE u.username = :username"),
    @NamedQuery(name = "UserAccountDB.findByName", query = "SELECT u FROM UserAccountDB u WHERE u.name = :name"),
    @NamedQuery(name = "UserAccountDB.findByEmail", query = "SELECT u FROM UserAccountDB u WHERE u.email = :email"),
    @NamedQuery(name = "UserAccountDB.searchByName", query = "SELECT u FROM UserAccountDB u WHERE u.name LIKE :name")})
public class UserAccountDB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "password")
    private byte[] password;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;

    public UserAccountDB() {
    }

    public UserAccountDB(Integer id) {
        this.id = id;
    }

    public UserAccountDB(Integer id, String username, String name, byte[] password, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(object instanceof UserAccountDB)) {
            return false;
        }
        UserAccountDB other = (UserAccountDB) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.UserAccountDB[ id=" + id + " ]";
    }

}
