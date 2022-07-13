package com.example.myapplication.API;

import com.example.myapplication.Classes.User;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {

    @GET("controller/user")
    Call<List<User>> getCustomers();

    @GET("controller/user/{email}")
    Call<User> getUser(@Path("email") String email);

    @POST("login")
    Call<ResponseBody> loginUser(@Body RequestBody body);

    @POST("controller/registration")
    Call<ResponseBody> registerUser(@Body User user);

    @GET("resetPwd/request/{email}")
    Call<User> resetPWDUser(@Path("email") String email);

}

