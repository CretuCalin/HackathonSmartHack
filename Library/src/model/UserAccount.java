package model;

import java.io.Serializable;
import java.util.Objects;

public class UserAccount implements Serializable, Comparable<UserAccount> {

    protected Integer id;
    protected String username;
    protected String name;
    protected byte[] password;
    protected String email;

    public UserAccount() {
    }

    public UserAccount(Integer id) {
        this.id = id;
    }

    public UserAccount(String username, String name, byte[] password, String email) {
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

    public UserAccount(Integer id, String username, String name, byte[] password, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    @Override
    public int compareTo(UserAccount o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserAccount other = (UserAccount) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
