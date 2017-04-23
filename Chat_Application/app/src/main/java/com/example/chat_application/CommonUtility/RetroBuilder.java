package com.example.chat_application.CommonUtility;

import com.example.chat_application.Interface.ChatServerRest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BANDINI on 22-04-2017.
 */

public class RetroBuilder {

    private static final String strServerURL = "http://localhost/api/";//"https://cryptoninja.me/api/";
    public  static ChatServerRest ConnectToWebService()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(strServerURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChatServerRest objRestService = retrofit.create(ChatServerRest.class);
        return objRestService;
    }
}
