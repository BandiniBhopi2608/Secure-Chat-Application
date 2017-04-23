package com.example.chat_application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chat_application.Model.User;

public class VerificationActivity extends AppCompatActivity
{
    //Variable Declaration
    //region

    Button btnVer;
    EditText edtVeriCode;
    private ProgressDialog pDialog;
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
        edtVeriCode = (EditText) findViewById(R.id.edtVeriCode);
        btnVer = (Button) findViewById(R.id.button4); // verify button
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Verification button functionality
        btnVer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                // TODO Auto-generated method stub
                if (edtVeriCode.length() < 10 || edtVeriCode.length() > 10)
                {
                    edtVeriCode.setError("Please enter verification code received on your email id");
                    edtVeriCode.requestFocus();
                }
                else
                {
                    pDialog.setMessage("Please wait ...");
                    pDialog.show();
                    ValidateUser();
                    pDialog.dismiss();
                    Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    public void ValidateUser()
    {
        // code for validation. 
    }
}

