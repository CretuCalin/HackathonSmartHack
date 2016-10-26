package model;

import java.io.Serializable;

public class Admin implements Serializable {
    private Integer id;
    private String username;
    private byte[] password;

    public Admin() {
    }

    public Admin(Integer id) {
        this.id = id;
    }

    public Admin(String username, byte[] password) {
        this.username = username;
        this.password = password;
    }

    public Admin(Integer id, String username, byte[] password) {
        this.id = id;
        this.username = username;
        this.password = password;
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

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] password) {
        this.password = password;
    }
    
    
}
