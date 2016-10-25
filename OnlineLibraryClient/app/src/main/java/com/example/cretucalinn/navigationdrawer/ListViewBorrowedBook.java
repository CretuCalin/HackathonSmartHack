package com.example.cretucalinn.navigationdrawer;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import model.Book;
import model.Borrow;

public class ListViewBorrowedBook extends AppCompatActivity {

    MyCustomAdapter dataAdapter = null;
    ArrayList<Integer> myBooksIDs;
    private ListView listView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_borrowed_book);

        setTitle("Borrowed Books");
        //Generate list View from ArrayList
        displayListView();
        //NetworkManager.getRemoteProcs().borrowBook(LoginActivity.userAccount.getId());

    }

    private void displayListView() {

        //Array list of countries

        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    ArrayList<ElementRawBorrowed> countryList = new ArrayList<ElementRawBorrowed>();
                    TreeSet<Book> myBook = NetworkManager.getRemoteProcs().findBooksByUser(LoginActivity.userAccount.getId());
                    for(Book b : myBook)
                    {
                        Borrow borrow = NetworkManager.getRemoteProcs().findBorrowByUserBook
                                (LoginActivity.userAccount.getId(),b.getId());
                        ElementRawBorrowed country = new ElementRawBorrowed(b, borrow);
                        countryList.add(country);
                    }
                    dataAdapter = new MyCustomAdapter(ListViewBorrowedBook.this,
                            R.layout.raw_list_view_borrowed_book, countryList);
                    listView = (ListView) findViewById(R.id.mobile_list);
                    // Assign adapter to ListView

                    return null;
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //create an ArrayAdaptar from the String Array
        listView.setAdapter(dataAdapter);
    }

    private class MyCustomAdapter extends ArrayAdapter<ElementRawBorrowed> {

        private ArrayList<ElementRawBorrowed> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ElementRawBorrowed> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<ElementRawBorrowed>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            TextView text1;
            TextView text2;
            TextView text3;
            ImageView image;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.raw_list_view_borrowed_book, null);

                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.title);
                holder.text2 = (TextView) convertView.findViewById(R.id.author);
                holder.text3 = (TextView) convertView.findViewById(R.id.borrowDate);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ElementRawBorrowed country = countryList.get(position);
            holder.text1.setText(country.getFirstText());
            holder.text2.setText(country.getSecondText());

            holder.text3.setText("Expiration Date :"
                    +country.getBorrow().getExpiration().toString());



            byte[] byteArray = countryList.get(position).getMyBook().getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length );
            holder.image.setImageBitmap(bmp);

            Button extendBtn = (Button)  convertView.findViewById(R.id.buttonExtend);
            Button returnBtn = (Button) convertView.findViewById(R.id.buttonReturn);

            extendBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v)
                {
                    Context context = getApplicationContext();
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {

                            NetworkManager.getRemoteProcs().extendBorrow(LoginActivity.userAccount.getId(),
                                    country.getMyBook().getId());


                            return  null;
                        }
                    }.execute();


                }

            });

            returnBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v)
                {
                    Context context = getApplicationContext();

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            NetworkManager.getRemoteProcs().deleteBorrow(LoginActivity.userAccount.getId(),
                                    country.getMyBook().getId());

                            return  null;
                        }
                    }.execute();


                }

            });

            return convertView;

        }

        private void editImage(View convertView,byte[] byteArray){
            ViewHolder holder = null;
            holder = (ViewHolder) convertView.getTag();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView image = (ImageView) findViewById(R.id.imageView);

            holder.image.setImageBitmap(bmp);

        }

    }



}


