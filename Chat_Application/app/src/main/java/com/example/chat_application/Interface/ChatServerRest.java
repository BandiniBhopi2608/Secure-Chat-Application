package com.example.chat_application.Interface;

import com.example.chat_application.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;

import okhttp3.ResponseBody;

/**
 * Created by brin pereira on 06/04/2017.
 */

public interface ChatServerRest {
    @POST("users/reg/register")
    Call<ResponseBody> register(@Body User user);

    @POST("users/reg/verify")
    Call<ResponseBody> verify(@Body User user);

    @POST("users/login/{requestno}")
    Call<ResponseBody> login(@Body User user, @Path("requestno") int requestno);

}

