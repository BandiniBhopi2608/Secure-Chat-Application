package com.example.chat_application.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chat_application.Adapter.MessageAdapter;
import com.example.chat_application.BackgroundService.MessageService;
import com.example.chat_application.CommonUtility.EncryptionUtility;
import com.example.chat_application.CommonUtility.PreferenceManager;
import com.example.chat_application.CommonUtility.RetroBuilder;
import com.example.chat_application.CommonUtility.ShowErrorMessage;
import com.example.chat_application.Model.Message;
import com.example.chat_application.Model.PreferenceKeys;
import com.example.chat_application.Model.User;
import com.example.chat_application.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private int intRecieverId;
    private int intSenderID;
    private Realm realm;
    private User objChatUser;
    private ListView listView;
    private BroadcastReceiver receiver;
    String strReceiverPublicKey;
    private EditText edtMessage;
    private Button btnSendMessage;
    private boolean isRegistered;
    private EncryptionUtility objEncryption;
    private Intent msgServiceIntent;
    private Message objMessage = new Message();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Start message service to receive data
        msgServiceIntent = new Intent(getApplicationContext(), MessageService.class);
        getApplicationContext().startService(msgServiceIntent);

        PreferenceManager.init(getApplicationContext());

        Intent i = getIntent();
        if (i != null) {
            intRecieverId = i.getIntExtra("id", 0);
            if (intRecieverId == 0) {
                finish();
                ShowErrorMessage.ShowError(ChatActivity.this, "Error occurred while loading chat");
            }
        }
        intSenderID = PreferenceManager.getInt(PreferenceKeys.USER_ID);
        try {
            objEncryption = new EncryptionUtility();
        } catch (Exception ex) {
        }
        realm = Realm.getDefaultInstance();
        objChatUser = realm.where(User.class).equalTo("ID", intRecieverId).findFirst();

        RealmQuery<Message> query = realm.where(Message.class).equalTo("From", intRecieverId)
                .equalTo("To", intSenderID)
                .or()
                .equalTo("From", intSenderID)
                .equalTo("To", intRecieverId);
        RealmResults<Message> result = query.findAll();

        listView = (ListView) findViewById(R.id.listview_messages);
        final MessageAdapter adapter = new MessageAdapter(this, 0, result, intRecieverId, intSenderID);
        listView.setAdapter(adapter);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount() - 1);
            }
        };

        getSupportActionBar().setTitle(objChatUser.getFirstName());

        strReceiverPublicKey = objChatUser.getPublicKey();

        btnSendMessage = (Button) findViewById(R.id.button_send_message);
        edtMessage = (EditText) findViewById(R.id.edittext_message);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strMessage = edtMessage.getText().toString();
                if (strMessage.trim().length() > 0) {
                    strMessage = strMessage.trim();
                    final String strPlainText = strMessage;

                    objMessage.setFrom(intSenderID);
                    objMessage.setTo(intRecieverId);
                    try {
                        objMessage.setMessage(objEncryption.fnEncryptMessage(strMessage, strReceiverPublicKey));
                    } catch (Exception ex) {
                        ShowErrorMessage.ShowError(ChatActivity.this, "ENC1 : Error occurred while sending message.");
                        return;
                    }
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    objMessage.setSendOn(df.format(Calendar.getInstance().getTime()));

                    try {
                        RetroBuilder.ConnectToWebService().sendMessage(objMessage).enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, final Response<Message> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        //objMessage.setMessage(strPlainText);
                                        realm.executeTransaction(new Realm.Transaction() {

                                            @Override
                                            public void execute(Realm realm) {
                                                Message objchatMessage = response.body();
                                                objchatMessage.setTo(intRecieverId);
                                                objchatMessage.setFrom(intSenderID);
                                                objchatMessage.setMessage(strPlainText);
                                                realm.copyToRealmOrUpdate(objchatMessage);
                                                adapter.notifyDataSetChanged();
                                                listView.setSelection(adapter.getCount() - 1);
                                                edtMessage.setText("");
                                            }
                                        });
                                        //getApplicationContext().startService(msgServiceIntent);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
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
                            public void onFailure(Call<Message> call, Throwable t) {
                                ShowErrorMessage.ShowError(getApplicationContext(), "Error: " + t.getMessage() + "Please try again or contact administrator");
                            }
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        if (!isRegistered) {
            registerReceiver(receiver, new IntentFilter(Message.UPDATE_MESSAGES));
            isRegistered = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
        if (isRegistered) {
            unregisterReceiver(receiver);
            isRegistered = false;
        }
    }
}
