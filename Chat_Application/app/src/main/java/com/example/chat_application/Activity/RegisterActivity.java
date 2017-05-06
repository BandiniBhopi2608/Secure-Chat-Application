package com.example.chat_application.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.*;

import android.content.Intent;
import android.app.ProgressDialog;

import com.example.chat_application.CommonUtility.RetroBuilder;
import com.example.chat_application.Model.User;
import com.example.chat_application.R;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {

    Button btnReg;
    EditText edtFirst, edtLast, edtUser, edtPass, edtConfPass, edtEmail, edtNumber, edtPnumber;
    Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@" + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
            + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //initialization of all editText
        //For Testing
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

                if (!EMAIL_ADDRESS_PATTERN.matcher(edtEmail.getText()).matches()) {
                    edtEmail.setError("Please enter valid email addresss");
                    edtEmail.requestFocus();

                } else {
                    pDialog.setMessage("Please wait ...");
                    pDialog.show();
                    registerUser();
                    pDialog.dismiss();
                    //Intent i = new Intent(getApplicationContext(), VerificationActivity.class);
                    //startActivity(i);
                    finish();

                }
            }
        });
    }

    //Bandini added this function on 22-04-2017
    //This function register the user and ask to verify
    public void registerUser() {
        //Creating User object and assigning values to it
        //region
        final User objUser = new User();
        objUser.setFirstName(edtFirst.getText().toString().trim());
        objUser.setLastName(edtLast.getText().toString().trim());
        objUser.setPassword(edtPass.getText().toString());
        objUser.setCountry(edtNumber.getText().toString());
        objUser.setPhoneNumber(edtPnumber.getText().toString().trim());
        objUser.setEmailID(edtEmail.getText().toString().trim());
        String salt = BCrypt.gensalt();
        objUser.setSalt(BCrypt.gensalt());
        //endregion

        try {
            RetroBuilder.ConnectToWebService().register(objUser).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject serverResp = new JSONObject(response.body().string());
                            if (serverResp.has("error")) {
                                /*Error Codes received in response
                                Error Code 1 : Validation Failed
	                            Error Code 2 : Phone number is not unique. That means User already exits
	                            Error Code 3 : EmailID is not unique
	                            Error Code 4 : Could not send Verification Email. Therefore Registration fails.
                                 */
                                int intErrorCode = serverResp.getInt("code");
                                switch (intErrorCode) {
                                    case 1:
                                        Toast.makeText(getApplicationContext(), "Validation failed. Please check input and try again", Toast.LENGTH_LONG).show();
                                        break;
                                    case 2:
                                        Bundle bundle = new Bundle();
                                        bundle.putString("phone", serverResp.getString("phone"));
                                        bundle.putString("id", serverResp.getString("id"));
                                        LoginActivity loginagain = new LoginActivity();  // vague code
                                        Toast.makeText(getApplicationContext(), "We located your account. Please login.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        break;
                                    case 3:
                                        Toast.makeText(getApplicationContext(), "This email id is already register", Toast.LENGTH_LONG).show();
                                        break;
                                    case 4:
                                        Toast.makeText(getApplicationContext(), "Registration failed. Check EmailID or contact administrator", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            } else { //User Data saved in database but not active as verification pending
                                String strID = serverResp.getString("ID");
                                objUser.setID(Integer.parseInt(serverResp.getString("ID")));
                                Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                                intent.putExtra("UserObject", objUser);
                                startActivity(intent);
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