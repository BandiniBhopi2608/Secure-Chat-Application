package com.example.chat_application;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.*;
import android.content.Intent;
import android.app.ProgressDialog;
import com.example.chat_application.*;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.attr.name;
import static java.security.AccessController.getContext;


public class RegisterActivity extends AppCompatActivity {

    Button btnReg;
    EditText edtFirst, edtLast, edtUser, edtPass, edtConfPass, edtEmail, edtNumber, edtPnumber;
    Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
            + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //initialization of all editText
        edtFirst = (EditText) findViewById(R.id.edtfirstname);
        edtLast = (EditText) findViewById(R.id.edtlastname);
        edtUser = (EditText) findViewById(R.id.edtUsername);
        edtPass = (EditText) findViewById(R.id.edtPass);
        edtConfPass = (EditText) findViewById(R.id.edtConfirmPass);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtNumber = (EditText) findViewById(R.id.edtNumber);
        edtPnumber = (EditText) findViewById(R.id.edtPnumber);
        //Initialization of Register Button
        btnReg = (Button) findViewById(R.id.button1);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //Registration button functionality
        btnReg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (edtFirst.getText().toString().length() == 0) {
                    edtFirst.setError("First name not entered");
                    edtFirst.requestFocus();
                }
                if (edtLast.getText().toString().length() == 0) {
                    edtLast.setError("Last name not entered");
                    edtLast.requestFocus();
                }

                if (edtUser.getText().toString().length() == 0) {
                    edtUser.setError("Username is Required");
                    edtUser.requestFocus();
                }
                if (edtPass.getText().toString().length() == 0) {
                    edtPass.setError("Password not entered");
                    edtPass.requestFocus();
                }

                if (edtPass.length() < 8 || edtPass.length() > 20) {
                    edtPass.setError("Please enter password of length between 8-20");
                    edtPass.requestFocus();
                }

                if (edtPass.getText().toString().length() == 0) {
                    edtConfPass.setError("Please confirm password");
                    edtConfPass.requestFocus();
                }

                if (!edtPass.getText().toString().equals(edtConfPass.getText().toString())) {
                    edtConfPass.setError("Password Not matched");
                    edtConfPass.requestFocus();
                }

                if (edtPnumber.length() < 10 || edtPnumber.length() > 10) {
                    edtPnumber.setError("Please enter 10 digit mobile number");
                    edtPnumber.requestFocus();
                }

                if (!EMAIL_ADDRESS_PATTERN.matcher(edtEmail.getText()).matches())
                {
                    edtEmail.setError("Please enter valid email addresss");
                    edtEmail.requestFocus();

                }
                else {
                    pDialog.setMessage("Please wait ...");
                    pDialog.show();
                    registerUser();
                    pDialog.dismiss();
                  // verificationCode();
                    Intent i = new Intent(getApplicationContext(), VerificationActivity.class);
                    startActivity(i);
                    finish();

                }

            }
        });
    }

    public void registerUser()
    {
        Retrofit adapter = new Retrofit.Builder().baseUrl(ChatServerRest.url).build();

        //Creating Rest Services
        final ChatServerRest restInterface = adapter.create(ChatServerRest.class);

        //Calling method to register

        restInterface.register(edtUser.getText().toString(),edtPass.getText().toString(), edtNumber.getText().toString(),edtPnumber.getText().toString(),
                edtEmail.getText().toString(), new Callback<ResponseBody>()
                {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        if (response.isSuccessful())
                        {
                            try
                            {
                             JSONObject resp = new JSONObject(response.body().string());
                             //   Bundle bundle = new Bundle();
//                                bundle.putString("user_id", resp.getString("id"));

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Error:Please try again", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {
                        Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage() + ". Please try again", Toast.LENGTH_LONG).show();
                        t.printStackTrace();

                    }
                });
    }
}