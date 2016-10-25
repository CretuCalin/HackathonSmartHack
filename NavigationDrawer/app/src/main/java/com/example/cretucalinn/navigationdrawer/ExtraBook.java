package com.example.cretucalinn.navigationdrawer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import model.Book;

/**
 * Created by Cretu Calinn on 10/23/2016.
 */

public class ExtraBook implements Serializable{
    private Book myBook;

    public ExtraBook(Book myBook)
    {
        this.myBook = myBook;
    }

    public Book getMyBook() {
        return myBook;
    }

    public void setMyBook(Book myBook) {
        this.myBook = myBook;
    }
}
