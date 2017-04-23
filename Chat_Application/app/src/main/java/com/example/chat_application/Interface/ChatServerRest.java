package com.example.chat_application.Interface;

import com.example.chat_application.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Body;
import okhttp3.ResponseBody;

/**
 * Created by brin pereira on 06/04/2017.
 */

public interface ChatServerRest
{
        /**
        String url = "https://cryptoninja.me/api/users/reg/";
        @FormUrlEncoded
        @POST("/login")
        void login(@Field("Mobile number") String phone, Callback cb);

        @FormUrlEncoded
        @POST("/register")
        void register(  //@Field("First name") String Fname,
                     // @Field("Last name") String Lname,
                      @Field("UserName") String Uname,
                      @Field("Password") String pass,
                      @Field("Country") String code,
                      @Field("PhoneNumber") String phone,
                      @Field("EmailID") String Email,
                      Callback pm);
      **/
        @POST("users/reg/register")
        Call<ResponseBody> register(@Body User user);
    }

