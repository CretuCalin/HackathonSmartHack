
package com.example.cretucalinn.navigationdrawer;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.spark.submitbutton.SubmitButton;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import model.Tag;
import model.UserAccount;


public class PreferencesActivity extends AppCompatActivity {

    private TreeSet<Tag> myTags;
    private TreeSet<Tag> treeSet;
    MyCustomAdapter dataAdapter = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_activity);

        setTitle("Preferences");

        displayListView();
        checkButtonClick();
    }

    private void displayListView()
    {
        final ArrayList<CheckItem> checkedList = new ArrayList<CheckItem>();
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    treeSet = NetworkManager.getRemoteProcs().findTags();
                    for(Tag t : treeSet)
                    {
                        TreeSet<Tag> selectedTags = NetworkManager.getRemoteProcs().findTagsByUser(LoginActivity.userAccount.getId());
                        if(selectedTags.contains(t)) {
                            CheckItem checkItem = new CheckItem(t.getName(), true, t);
                            checkedList.add(checkItem);
                        }
                        else {
                            CheckItem checkItem2 = new CheckItem(t.getName(), false, t);
                            checkedList.add(checkItem2);
                        }
                    }
                    return  null;
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.preferences_layout_line, checkedList);
        ListView listView = (ListView) findViewById(R.id.preferences_list_view);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                CheckItem country = (CheckItem) parent.getItemAtPosition(position);

            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<CheckItem>
    {
        private ArrayList<CheckItem> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<CheckItem> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<CheckItem>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.preferences_layout_line, null);

                holder = new ViewHolder();
                //holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkboxPref);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        CheckItem country = (CheckItem) cb.getTag();
                        country.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            CheckItem country = countryList.get(position);
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            return convertView;
        }
    }

    private void checkButtonClick()
    {
        myTags = new TreeSet<Tag>();
        SubmitButton submitButton = (SubmitButton) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread()
                {
                    public void run(){
                        ArrayList<CheckItem> checkItemList = dataAdapter.countryList;
                        for(int i=0;i<checkItemList.size();i++){
                            CheckItem checkItem = checkItemList.get(i);
                            if(checkItem.isSelected()){
                                myTags.add(checkItem.getTag());
                            }
                        }

                        try {
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {

                                    NetworkManager.getRemoteProcs().changeTagsOfUser(LoginActivity.userAccount.getId(), myTags);
                                    Intent intent = new Intent(PreferencesActivity.this,  MenuActivity.class);
                                    startActivity(intent);
                                    return null;

                                }
                            }.execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }

        });
    }
    }





