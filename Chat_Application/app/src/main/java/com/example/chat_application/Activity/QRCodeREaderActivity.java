package com.example.chat_application.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.chat_application.Adapter.SampleFragmentPagerAdapter;
import com.example.chat_application.CommonUtility.PreferenceManager;
import com.example.chat_application.CommonUtility.RetroBuilder;
import com.example.chat_application.Fragments.FriendList;
import com.example.chat_application.Model.EncryptionConfiguration;
import com.example.chat_application.Model.PreferenceKeys;
import com.example.chat_application.Model.User;
import com.example.chat_application.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.in;

/*
Bandini added this class to scan the QR code and add the user in friend list to do chat
*/
public class QRCodeREaderActivity extends AppCompatActivity {
    //Variable Declaration
    private FriendList objFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start scanning of QR code
                IntentIntegrator integrator = new IntentIntegrator(QRCodeREaderActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan Friend's Publick Key");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        objFriendList = new FriendList();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(objFriendList);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.showQRCode:
                Intent intent1 = new Intent(QRCodeREaderActivity.this, QRCodeGeneratorActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                String strFriendPublicKey = result.getContents(); //This contains friend's RSA public key, DSA public key and his/her User ID
                final String[] strFrinedInfo = strFriendPublicKey.split(EncryptionConfiguration.QR_CODE_SEPERATOR);//Separate above mentioned three fields
                if (strFrinedInfo != null && strFrinedInfo.length == 3) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("SenderID", Integer.toString(PreferenceManager.getInt(PreferenceKeys.USER_ID)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Send request to server to get receiver's data based on his/her User ID
                    RetroBuilder.ConnectToWebService().getUser(json, strFrinedInfo[2]).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                final User objReceiver = response.body();
                                objReceiver.setPublicKey(strFrinedInfo[0]);
                                objReceiver.setDSPublicKey(strFrinedInfo[1]);
                                objFriendList.addUser(objReceiver);
                            } else {
                                try {
                                    Toast.makeText(getApplicationContext(), "Error: " + response.errorBody().string() + "Please try again or contact administrator", Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "Error: " + t.getMessage() + "Please try again or contact administrator", Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });
                } else {
                    Toast.makeText(this, "Invalid Code.", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
