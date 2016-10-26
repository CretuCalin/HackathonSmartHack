package model;

import java.io.Serializable;
import java.util.Date;

public class Borrow implements Serializable, Comparable<Borrow> {

    private Integer id;
    private int userAccount;
    private int book;
    private Date date;
    private Date expiration;
    private boolean extended;

    public Borrow() {
    }

    public Borrow(Integer id) {
        this.id = id;
    }

    public Borrow(int userAccount, int book, Date date, Date expiration, boolean extended) {
        this.userAccount = userAccount;
        this.book = book;
        this.date = date;
        this.expiration = expiration;
        this.extended = extended;
    }

    public Borrow(Integer id, int userAccount, int book, Date date, Date expiration, boolean extended) {
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

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    @Override
    public int compareTo(Borrow o) {
        return this.expiration.compareTo(o.expiration);
    }
}
