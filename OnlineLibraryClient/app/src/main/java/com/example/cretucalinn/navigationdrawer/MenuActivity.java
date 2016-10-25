package com.example.cretucalinn.navigationdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        Button preferencesButton = (Button) findViewById(R.id.preferencesButton);
        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, PreferencesActivity.class);
                startActivity(intent);
            }
        });

        Button newsFeedButton = (Button) findViewById(R.id.newsfeedButton);
        newsFeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ListViewNewsFeed.class);
                startActivity(intent);
            }
        });

        Button borrowedBooksButton = (Button) findViewById(R.id.myBorrowedBooksButton);
        borrowedBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ListViewBorrowedBook.class);
                startActivity(intent);
            }
        });

    }
}
