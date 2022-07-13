package com.example.myapplication.API;

import com.example.myapplication.Classes.Apartment;
import com.example.myapplication.Classes.MultipleApartmentsStructure;
import com.example.myapplication.Classes.SearchBody;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MultipleApartmentAPI {

    @POST("controller/research")
    Call<ArrayList<MultipleApartmentsStructure>> getApartments(@Body SearchBody body);
}
