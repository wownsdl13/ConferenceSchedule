package com.example.conference;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import server.SignInServer;
import util.SignIn;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new SignIn(this);
    }
}
