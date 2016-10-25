package com.example.cretucalinn.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import model.Book;

public class ListViewNewsFeed extends AppCompatActivity {

    MyCustomAdapter dataAdapter = null;
    ArrayList<Integer> myBooksIDs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_news_field);
        setTitle("News Feed");

        displayListView();

    }

    private void displayListView() {

        //Array list of countries

        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    ArrayList<ElementRaw> countryList = new ArrayList<ElementRaw>();
                    myBooksIDs = NetworkManager.getRemoteProcs().generateNewsFeed(LoginActivity.userAccount.getId());
                    for(int i = 0; i<10; i++)
                    {
                        Book myBook = NetworkManager.getRemoteProcs().findBook(myBooksIDs.get(i));
                        ElementRaw country = new ElementRaw(myBook);
                        countryList.add(country);
                    }
                    dataAdapter = new MyCustomAdapter(ListViewNewsFeed.this,
                            R.layout.raw_list_view_news_feed, countryList);
                    ListView listView = (ListView) findViewById(R.id.mobile_list);
                    // Assign adapter to ListView
                    listView.setAdapter(dataAdapter);
                    return null;
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //create an ArrayAdaptar from the String Array

    }

    private class MyCustomAdapter extends ArrayAdapter<ElementRaw> {

        private ArrayList<ElementRaw> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ElementRaw> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<ElementRaw>();
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
                convertView = vi.inflate(R.layout.raw_list_view_news_feed, null);

                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.title);
                holder.text2 = (TextView) convertView.findViewById(R.id.author);
                holder.text3 = (TextView) convertView.findViewById(R.id.description);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(ListViewNewsFeed.this, "Salutare boss", Toast.LENGTH_SHORT).show();
                    }
                });

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            ElementRaw country = countryList.get(position);
            holder.text1.setText(country.getFirstText());
            holder.text2.setText(country.getSecondText());
            holder.text3.setText(country.getThirdText());
            byte[] byteArray = countryList.get(position).getMyBook().getImage();
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0,byteArray.length );
            holder.image.setImageBitmap(bmp);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Book myBook = countryList.get(position).getMyBook();
                    ExtraBook extraBook = new ExtraBook(myBook);
                    Intent intent = new Intent(ListViewNewsFeed.this,DescriptionBookActivity.class );

                    intent.putExtra("ThisBook", extraBook);
                    startActivity(intent);


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
