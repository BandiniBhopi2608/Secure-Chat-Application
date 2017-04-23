package com.example.chat_application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chat_application.Model.User;

public class VerificationActivity extends AppCompatActivity {

    //Variable Declaration
    //region
    private User objUser;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        Intent i = getIntent();
        objUser = (User) i.getSerializableExtra("UserObject");

        //Brin Task :  On button click assign verification code to object User
        //objUser.setVerificationCode(); and then call Web service as called in RegisterActivity.
    }
}
