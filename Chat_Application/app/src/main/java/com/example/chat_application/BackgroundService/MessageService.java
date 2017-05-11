package com.example.chat_application.BackgroundService;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.chat_application.Activity.ChatActivity;
import com.example.chat_application.CommonUtility.EncryptionUtility;
import com.example.chat_application.CommonUtility.PreferenceManager;
import com.example.chat_application.CommonUtility.RetroBuilder;
import com.example.chat_application.CommonUtility.ShowErrorMessage;
import com.example.chat_application.CommonUtility.UserToUserAuth;
import com.example.chat_application.Model.Message;
import com.example.chat_application.Model.PreferenceKeys;
import com.example.chat_application.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by BANDINI on 03-05-2017.
 */

//DESC: This service runs in background to fetch the latest message from the server.
public class MessageService extends IntentService {

    //Variable Declaration
    private static int intMyUserId;
    private static int intMsgLastestID = 0;
    String strPrivateKey = PreferenceManager.getString(PreferenceKeys.PRIVATE_KEY);
    private Handler handler = new Handler();
    private final int intInterval = 2000; // 2 Second

    public MessageService() {
        super("MessageService@CryproNinja");
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            Log.d("Inside msg service", "inside");
            intMyUserId = PreferenceManager.getInt(PreferenceKeys.USER_ID);
            //Send request to server to get latest message
            RetroBuilder.ConnectToWebService().getMessages(intMyUserId, intMsgLastestID).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {

                            @Override
                            public void execute(Realm realm) {
                                try {
                                    JSONArray messages = new JSONArray(response.body().string());

                                    for (int i = 0; i < messages.length(); i++) {
                                        try {
                                            JSONObject singleMessage = messages.getJSONObject(i);
                                            fnDecryptAndSaveMessage(singleMessage, realm);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            handler.postDelayed(runnableCode, intInterval);
                        } catch (Exception ex) {
                            ShowErrorMessage.ShowError(getApplicationContext(), " Handler PostDelayedException Error : " + ex.toString());
                        }
                    } else {
                        try {
                            ShowErrorMessage.ShowError(getApplicationContext(), "Error: " + response.errorBody().string() + "Please try again or contact administrator");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ShowErrorMessage.ShowError(getApplicationContext(), "Error: " + t.getMessage() + "Please try again or contact administrator");
                }
            });
        }
    };

    //DESC :  This function decrypts the message received from the server in JSON object
    private void fnDecryptAndSaveMessage(JSONObject singleMessage, Realm realm) throws JSONException {

        String strEncryptedMsg = singleMessage.getString("Message");
        String strSignature = singleMessage.getString("Signature");
        int intFrom = singleMessage.getInt("From");
        intMsgLastestID = singleMessage.getInt("ID");
        User objUser = realm.where(User.class).equalTo("ID", intFrom).findFirst();

        String strPlainMessage = null;
        boolean IsVerified = false;

        try {
            //First verify the digital signature generated by sender using sender's public key
            IsVerified = UserToUserAuth.fnVerify(objUser.getDSPublicKey(), strSignature, strEncryptedMsg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //If verification successfully then it will decrypt the message
        if (IsVerified) {
            try {
                //Decrypt the message using own private key
                strPlainMessage = EncryptionUtility.fnDecryptMessage(strEncryptedMsg, PreferenceManager.getString(PreferenceKeys.PRIVATE_KEY));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (strPlainMessage != null) {//Decryption is Successful

                Message objMessage = new Message();
                objMessage.setID(intMsgLastestID);
                objMessage.setFrom(singleMessage.getInt("From"));
                objMessage.setTo(intMyUserId);
                objMessage.setMessage(strPlainMessage);
                objMessage.setSendOn(singleMessage.getString("SendOn"));
                realm.copyToRealmOrUpdate(objMessage);
                sendBroadcast(new Intent(Message.UPDATE_MESSAGES));
            } else {
                Log.e("SecureChat", "Couldn't decrypt message");
            }
        } else {
            ShowErrorMessage.ShowError(getApplicationContext(), "User to User authentication failed.");
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        handler.post(runnableCode);
    }
}
