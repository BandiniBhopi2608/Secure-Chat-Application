package com.example.chat_application;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.chat_application.Model.FriendList;
import com.example.chat_application.Model.User;

import static java.security.AccessController.getContext;

public class ScreenActivity extends AppCompatActivity
{
    private ListView listView;
    private FriendList adapter;

    //http://www.androidhive.info/2016/05/android-working-with-realm-database-replacing-sqlite-core-data/
  //  private Realm realm;

    @Nullable
   // @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listfriends, container, false);

        listView = (ListView) rootView.findViewById(R.id.listview_friends);  // input parameter

      /*  realm = Realm.getDefaultInstance();
        RealmQuery<User> query = realm.where(User.class);
        RealmResults<User> result = query.findAll();*/

        // FriendList is database handling code.

    //    adapter = new FriendList(getContext(), 0, result);
        listView.setAdapter(adapter);
      //  listView.setOnItemClickListener(this);

        return rootView;
    }


  //  @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        User user = (User) adapterView.getAdapter().getItem(i);
     //   Intent intent = new Intent(getContext(), ChatActivity.class);   ChatAct
   //     intent.putExtra("id", user.getId());
   //     startActivity(intent);
    }

    public void addUser(final User user) {
      //  realm.executeTransaction(new Realm.Transaction() {
//            @Override
         //   public void execute(Realm realm) {
         //       realm.copyToRealmOrUpdate(user);
       //         adapter.notifyDataSetChanged();
           // }
       // });
    }

    @Override
    public void onStart() {
        super.onStart();
     //   realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
       // realm.close();
    }
}

