package com.example.chat_application.Activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.chat_application.BackgroundService.MessageService;
import com.example.chat_application.CommonUtility.EncryptionUtility;
import com.example.chat_application.CommonUtility.HashFunctions;
import com.example.chat_application.CommonUtility.PreferenceManager;
import com.example.chat_application.CommonUtility.RetroBuilder;
import com.example.chat_application.Model.PreferenceKeys;
import com.example.chat_application.Model.User;
import com.example.chat_application.R;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.*;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brin pereira on 01/04/2017.
 */

public class LoginActivity extends AppCompatActivity {

    //Variable Declaration
    //region
    Button btnsub1, btnsub2;
    EditText edtPnumber, edtPassword;

    private ProgressDialog pDialog;
    private User objUser;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Move to mainactivity after JWT authentication
        PreferenceManager.init(getApplicationContext());

        btnsub1 = (Button) findViewById(R.id.button2); // register button
        btnsub2 = (Button) findViewById(R.id.button3); // login button
        edtPnumber = (EditText) findViewById(R.id.edtLUsername);
        edtPassword = (EditText) findViewById(R.id.edtLPass);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            //Set Phone number here coming from registration when user already exists
            edtPnumber.setText(intent.getExtras().getString("phone"));
        }


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
        btnsub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.setMessage("Login ...");
                pDialog.show();
                loginUser();
                pDialog.dismiss();
             //   finish();
            }
        });

    }

    public void loginUser() {
        //String strSaltedPwd = HashFunctions.getHashValue(edtPassword.getText().toString().trim());
        objUser = new User();
        objUser.setPhoneNumber(edtPnumber.getText().toString().trim());
        try {
            RetroBuilder.ConnectToWebService().login(objUser, 1).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject serverResp = new JSONObject(response.body().string());
                            if (serverResp.has("error")) {
                                Toast.makeText(getApplicationContext(), serverResp.getString("error"), Toast.LENGTH_LONG).show();
                            } else {
                                //In response, we received salt and challenge. Now we will calculate tag and send it to the Server again to verify
                                objUser.setChallenge(serverResp.getString("Challenge"));
                                String strSaltedPwd = HashFunctions.getHashValue(edtPassword.getText().toString().trim(), serverResp.getString("Salt"));
                                String strTag = HashFunctions.getSHA512SecurePWD(strSaltedPwd, objUser.getChallenge());
                                objUser.setTag(strTag);
                                fnReplyToChallenge();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        try {
                            Toast.makeText(getApplicationContext(), "Error: " + response.errorBody().string() + "Please try again or contact administrator", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error: " + t.getMessage() + "Please try again or contact administrator", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fnReplyToChallenge() {
        try {
            if (objUser != null) {
                RetroBuilder.ConnectToWebService().login(objUser, 2).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject serverResp = new JSONObject(response.body().string());
                                if (serverResp.has("error")) {
                                    Toast.makeText(getApplicationContext(), serverResp.getString("error"), Toast.LENGTH_LONG).show();
                                } else {
                                    //String strID = serverResp.getString("ID");
                                    //PreferenceManager.save(PreferenceKeys.USER_ID, Integer.parseInt(serverResp.getString("ID"))); //Save User ID
                                    Intent i = new Intent(getApplicationContext(), QRCodeREaderActivity.class);
                                    startActivity(i);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            try {
                                Toast.makeText(getApplicationContext(), "Error: " + response.errorBody().string() + "Please try again or contact administrator", Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error: " + t.getMessage() + "Please try again or contact administrator", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


