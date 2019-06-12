package com.example.conference;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import util.getTimeTable;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        new getTimeTable(this);
    }
}
