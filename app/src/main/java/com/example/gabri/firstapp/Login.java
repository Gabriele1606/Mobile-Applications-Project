package com.example.gabri.firstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void registrationForm(View view){
        Intent registrationActivity=new Intent(this, RegistrationForm.class);
        startActivity(registrationActivity);

    }

    public void login(View view){
        Intent homePage=new Intent(this, HomePage.class);
        startActivity(homePage);

    }
}
