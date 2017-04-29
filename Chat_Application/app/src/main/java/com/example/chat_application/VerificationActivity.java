package com.example.chat_application;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat_application.CommonUtility.RetroBuilder;
import com.example.chat_application.Model.User;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {
    //Variable Declaration
    //region
    Button btnVer;
    EditText edtveriCode;
    private ProgressDialog pDialog;
    private User objUser;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        edtveriCode = (EditText) findViewById(R.id.edtVeriCode);
        btnVer = (Button) findViewById(R.id.button4); // verify button
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //Verification button functionality
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (edtveriCode.getText().toString() == null || edtveriCode.getText().toString().trim() == "") {
                    edtveriCode.setError("Please enter verification code received on your email id");
                    edtveriCode.requestFocus();
                } else {
                    Intent i = getIntent();
                    if (i != null) {
                        objUser = (User) i.getSerializableExtra("UserObject");
                    }
                    pDialog.setMessage("Please wait ...");
                    pDialog.show();
                    ValidateUser();
                    pDialog.dismiss();
                    finish();
                }
            }
        });
    }

    public void ValidateUser() {
        objUser.setVerificationCode(edtveriCode.getText().toString().trim());
        try {
            RetroBuilder.ConnectToWebService().verify(objUser).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject serverResp = new JSONObject(response.body().string());
                            if (serverResp.has("error")) {
                                /*Error Codes received in response
                                Error Code 5: User not found in database.
	                            Error Code 6: Verification Code does not match.
                                 */
                                int intErrorCode = serverResp.getInt("code");
                                switch (intErrorCode) {
                                    case 5:
                                        Toast.makeText(getApplicationContext(), "User does not exists. Contact administrator.", Toast.LENGTH_LONG).show();
                                        break;
                                    case 6:
                                        Toast.makeText(getApplicationContext(), "Verification does not match. Please try again.", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            } else { //Verification Successfull.
                                Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                                startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
        } catch (Exception e) {
            Log.d("onResponse", "There is an error");
            e.printStackTrace();
        }
    }
}

