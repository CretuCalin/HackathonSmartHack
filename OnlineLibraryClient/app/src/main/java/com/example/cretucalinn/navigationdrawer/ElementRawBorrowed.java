package com.example.cretucalinn.navigationdrawer;

import android.media.Image;

import model.Book;
import model.Borrow;

/**
 * Created by teo on 10.08.2016.
 */
public class ElementRawBorrowed {
    private String firstText;
    private String secondText;
    private String thirdText;
    private Book myBook;

    public Borrow getBorrow() {
        return borrow;
    }

    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }

    private Borrow borrow;

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    private byte[] byteArray;

    public Book getMyBook() {
        return myBook;
    }

    public void setMyBook(Book myBook) {
        this.myBook = myBook;
    }

    public ElementRawBorrowed(Book myBook, Borrow borrow){//, byte[] byteArray){
        this.myBook = myBook;
        firstText = myBook.getTitle();
        secondText = myBook.getAuthor();
        thirdText = "Expiration Date : " + borrow.getExpiration().toString();
        this.byteArray = myBook.getImage();
        this.borrow = borrow;
    }


    public String getFirstText() {
        return firstText;
    }

    public void setFirstText(String firstText) {
        this.firstText = firstText;
    }

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText(String secondText) {
        this.secondText = secondText;
    }

    public String getThirdText() {
        return thirdText;
    }

    public void setThirdText(String thirdText) {
        this.thirdText = secondText;
    }

}