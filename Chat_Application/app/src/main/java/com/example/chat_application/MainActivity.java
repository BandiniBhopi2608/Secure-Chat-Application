package com.example.chat_application;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chat_application.CommonUtility.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.init(getApplicationContext());
    }
}