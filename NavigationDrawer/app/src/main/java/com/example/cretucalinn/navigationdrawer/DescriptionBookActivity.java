package com.example.cretucalinn.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;



public class DescriptionBookActivity extends AppCompatActivity {

    private boolean bookOverLimit;
    private ExtraBook myExtraBook;
    private String myString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_presentation_screen);

        setTitle("Book Description");

        Intent intent = getIntent();
        myExtraBook= (ExtraBook) intent.getSerializableExtra("ThisBook");
        editTitle(myExtraBook.getMyBook().getTitle());
        editAuthor(myExtraBook.getMyBook().getAuthor());
        editDescription(myExtraBook.getMyBook().getDescription());
        editImage(myExtraBook.getMyBook().getImage());

        Button borrowButton = (Button) findViewById(R.id.buttonBorrow);
        bookOverLimit = false;

        borrowButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if(
                            NetworkManager.getRemoteProcs().countBorrowsByUser(LoginActivity.userAccount.getId()) == 5)
                            {
                                bookOverLimit =true;
                            }
                            else {
                                NetworkManager.getRemoteProcs().borrowBook(LoginActivity.userAccount.getId(),
                                        myExtraBook.getMyBook().getId());
                                bookOverLimit = false;
                            }
                            return  null;
                        }
                    }.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(bookOverLimit == true)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(DescriptionBookActivity.this).create(); //Read Update
                    alertDialog.setTitle("Borrowed Books over Limit !");
                    alertDialog.setMessage("You borrowed five books");
                    alertDialog.show();
                }else {
                    AlertDialog alertDialog = new AlertDialog.Builder(DescriptionBookActivity.this).create(); //Read Update
                    alertDialog.setTitle(myExtraBook.getMyBook().getTitle());
                    alertDialog.setMessage("Book borrowed !");
                    alertDialog.show();
                }
            }
        });

        Button downloadButton = (Button) findViewById(R.id.buttonDownload);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new AsyncTask<Void, Void, Void>()
                     {
                         @Override
                         protected Void doInBackground (Void...params){

                            byte[] bookContent = NetworkManager.getRemoteProcs().findBookContent(myExtraBook.getMyBook().getId());
                             myString =  Arrays.toString(bookContent);

                             String filename = "myfile";
                             File file = new File(DescriptionBookActivity.this.getFilesDir(), filename);
                             System.out.println(DescriptionBookActivity.this.getFilesDir().toString());
                             FileOutputStream outputStream;

                             try {
                                 outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                 outputStream.write(bookContent);
                                 outputStream.close();
                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                             try {
                                 FileInputStream inputStream = openFileInput(filename);
                             } catch (FileNotFoundException e) {
                                 e.printStackTrace();
                             }

                             return null;
                         }

                     }.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                editDescription(myString);
                AlertDialog alertDialog = new AlertDialog.Builder(DescriptionBookActivity.this).create(); //Read Update
                alertDialog.setTitle("Book downloaded");
                alertDialog.setMessage("You just download your book !");
                alertDialog.show();
            }

        });
    }

    private void editTitle(String title){
        TextView titleBook = (TextView) findViewById(R.id.title);
        titleBook.setText(title);
    }

    private void editAuthor(String author){
        TextView authorBook = (TextView) findViewById(R.id.author);
        authorBook.setText("by " + author);
    }

    private void editDescription(String description){
        TextView descriptionBook = (TextView) findViewById(R.id.descriptionBook);
        descriptionBook.setText(description);
    }

    private void editImage(byte[] byteArray){
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.imageBook);
        image.setImageBitmap(bmp);
    }
}
