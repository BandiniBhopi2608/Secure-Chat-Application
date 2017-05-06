package com.example.chat_application.Activity;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by BANDINI on 30-04-2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        Realm.init(this);
        /*
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        */
    }

}
