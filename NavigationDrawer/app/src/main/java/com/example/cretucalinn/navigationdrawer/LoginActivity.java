package com.example.cretucalinn.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import model.UserAccount;
import remote.RemoteProcs;
import rpc.HiRpc;

public class LoginActivity extends AppCompatActivity {

    public static UserAccount userAccount;
    private String usernameText;
    private String passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        setTitle("Login");

        // getSupportActionBar().hide();

        Button loginButton = (Button) findViewById(R.id.buttonLogin);
        final CheckBox checkBoxPassword = (CheckBox)findViewById(R.id.showPassword);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText username = (EditText) findViewById(R.id.username);


        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                usernameText = username.getText().toString();
                passwordText  = password.getText().toString();


                NetworkManager.connect("192.168.43.119", usernameText, passwordText);

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {

                            userAccount = NetworkManager.getRemoteProcs().logUser(usernameText,passwordText);
                            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                            String usernameString = userAccount.getName();
                            intent.putExtra("UserName", usernameString);
                            startActivity(intent);
                            System.out.println("Nume" + userAccount.getName());
                            return  null;
                        }
                    }.execute();
            }
        });

        checkBoxPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(checkBoxPassword.isChecked()){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
