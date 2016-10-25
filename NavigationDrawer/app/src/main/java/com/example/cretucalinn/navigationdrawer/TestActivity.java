package com.example.cretucalinn.navigationdrawer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import model.Book;
import remote.RemoteProcs;
import rpc.HiRpc;

public class TestActivity extends AppCompatActivity {


    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imageView = (ImageView) findViewById(R.id.imageView3);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Book book = NetworkManager.getRemoteProcs().findBook(2);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    byte[] bytesArray = book.getImage();
                    BitmapFactory.decodeByteArray(bytesArray,0,bytesArray.length,options);
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytesArray, 0, bytesArray.length);
                    imageView.setImageBitmap(bmp);
                } catch (Exception e) {
                    System.out.println(e);
                }
                return  null;
            }
        }.execute();





    }
}
