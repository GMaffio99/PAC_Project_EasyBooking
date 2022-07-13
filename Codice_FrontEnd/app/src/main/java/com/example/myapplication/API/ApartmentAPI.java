package com.example.myapplication.API;

import com.example.myapplication.Classes.Apartment;
import com.example.myapplication.Classes.SearchBody;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApartmentAPI {

    @POST("controller/research")
    Call<ArrayList<Apartment>> getApartments(@Body SearchBody body);
}
