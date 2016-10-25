package com.example.cretucalinn.navigationdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity_layout);



        Intent intent = getIntent();

        setTitle("Welcome");

        TextView welcomeTextView = (TextView) findViewById(R.id.textView2);
        welcomeTextView.setText ("Welcome "+ intent.getStringExtra("UserName"));

        Button nextButton = (Button) findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(WelcomeActivity.this, PreferencesActivity.class);
                startActivity(intent1);
            }
        });


    }
}
