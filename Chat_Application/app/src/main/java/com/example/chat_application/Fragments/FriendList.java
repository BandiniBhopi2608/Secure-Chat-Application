package com.example.chat_application.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.chat_application.Activity.ChatActivity;
import com.example.chat_application.Activity.ScreenActivity;
import com.example.chat_application.Adapter.FriendListAdapter;
import com.example.chat_application.Model.User;
import com.example.chat_application.R;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by brin pereira on 28/04/2017.
 */

public class FriendList extends Fragment implements AdapterView.OnItemClickListener {
    //Variable Declaration
    private Realm realm;
    private FriendListAdapter adapter;
    private ListView lstviewFriends;
    //Button btnShowPublicKey;
    //Button btnAddFriend;
    private User objUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listfriends, container, false);

        lstviewFriends = (ListView) rootView.findViewById(R.id.listview_friends);

        realm = Realm.getDefaultInstance();
        RealmQuery<User> query = realm.where(User.class);
        RealmResults<User> result = query.findAll();

        adapter = new FriendListAdapter(getContext(), 0, result);
        lstviewFriends.setAdapter(adapter);
        lstviewFriends.setOnItemClickListener(this);

        return rootView;
    }

    public void addUser(final User objuser) {
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(objuser);
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            User user = (User) adapterView.getAdapter().getItem(i);
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("id", user.getID()); //Receiver's ID
            startActivity(intent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            if (data != null) {
                objUser = (User) data.getSerializableExtra("UserObject");
                if (objUser != null)
                    addUser(objUser);
            }
        }
    }
*/
}
