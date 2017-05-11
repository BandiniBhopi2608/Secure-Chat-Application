package com.example.chat_application.Interface;

import com.example.chat_application.Model.Message;
import com.example.chat_application.Model.User;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;

import okhttp3.ResponseBody;

/**
 * Created by Bandini Bhopi on 06/04/2017.
 */

public interface ChatServerRest {
    @POST("users/reg/register")
    Call<ResponseBody> register(@Body User user);

    @POST("users/reg/verify")
    Call<ResponseBody> verify(@Body User user);

    @POST("users/login/{requestno}")
    Call<ResponseBody> login(@Body User user, @Path("requestno") int requestno);

    @POST("users/{id}")
    Call<User> getUser(@Body JSONObject json, @Path("id") String id);

    @POST("users/message/send")
    Call<Message> sendMessage(@Body Message objMessage);

    @POST("users/message/{id}/{mid}")
    Call<ResponseBody> getMessages(@Path("id") int id, @Path("mid") int mid);

}

