package com.example.chat_application;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.*;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.attr.password;

/**
 * Created by brin pereira on 01/04/2017.
 */

public class LoginActivity extends AppCompatActivity {

    Button btnsub1 , btnsub2;

    EditText edtPnumber;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnsub1 = (Button) findViewById(R.id.button2); // register button
        btnsub2 = (Button) findViewById(R.id.button3); // login button
        edtPnumber = (EditText) findViewById(R.id.edtNumber);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //Registration button functionality
        btnsub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        //  login button functionality
        btnsub2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pDialog.setMessage("Login ...");
                pDialog.show();
                loginUser();
                pDialog.dismiss();
                Intent i = new Intent(getApplicationContext(), ChatScreenActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
        public void loginUser()
    {
            Retrofit adapter = new Retrofit.Builder().baseUrl(ChatServerRest.url).build();
        //Creating Rest Services
            final ChatServerRest restInterfaceL = adapter.create(ChatServerRest.class);
        //Calling method to login

        restInterfaceL.login(edtPnumber.getText().toString(),new Callback<ResponseBody>()
                {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        if (response.isSuccessful())
                        {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                if (json.has("error")) {
                                    Toast.makeText(LoginActivity.this, json.getString("error"), Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("user_id", json.getString("ID"));

                                    String salt = json.getString("salt");
                                    String challenge = json.getString("challenge");
                                    replyAfterChallenge(salt, challenge);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Error:Please try again", Toast.LENGTH_LONG).show();

                        }
                    }


                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {
                        Toast.makeText(LoginActivity.this, "Error: " + t.getMessage() + ". Please try again", Toast.LENGTH_LONG).show();
                        t.printStackTrace();

                    }
                });
    }

    public void replyAfterChallenge (String salt , String challenge)
    {
            // do HMAC calculation and provide tag and then send it server

    }


    }


