package com.example.chat_application.CommonUtility;

import com.example.chat_application.Interface.ChatServerRest;
import com.example.chat_application.Model.PreferenceKeys;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BANDINI on 22-04-2017.
 */

//To make GET, POST request to the server we used Retrofit
//Reference: http://www.vogella.com/tutorials/Retrofit/article.html
public class RetroBuilder {

    private static final String strServerURL = "https://cryptoninja.me/api/";

    public static ChatServerRest ConnectToWebService() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        // If you have JWT add it to the header
        if (PreferenceManager.contains(PreferenceKeys.JWT)) {
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder().addHeader("Authorization", "Bearer " + PreferenceManager.getString(PreferenceKeys.JWT))
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
        }

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder retrofitbuilder = new Retrofit.Builder()
                .baseUrl(strServerURL)
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = retrofitbuilder.client(httpClient.build()).build();

        ChatServerRest objRestService = retrofit.create(ChatServerRest.class);
        return objRestService;
    }
}
