package com.example.cretucalinn.navigationdrawer;

import android.media.Image;

import model.Book;

/**
 * Created by teo on 10.08.2016.
 */
public class ElementRaw {
    private String firstText;
    private String secondText;
    private String thirdText;
    private Book myBook;

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

    public ElementRaw(Book myBook){//, byte[] byteArray){
        this.myBook = myBook;
        firstText = myBook.getTitle();
        secondText = myBook.getAuthor();
        thirdText = myBook.getDescription();
        this.byteArray = myBook.getImage();
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