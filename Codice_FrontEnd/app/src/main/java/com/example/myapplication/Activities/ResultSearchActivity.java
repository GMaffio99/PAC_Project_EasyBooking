package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myapplication.Classes.Apartment;
import com.example.myapplication.RecyclerView.ApartmentRecViewAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ResultSearchActivity extends AppCompatActivity {

    private RecyclerView apartmentRecView;
    private ApartmentRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        adapter = new ApartmentRecViewAdapter(this);
        apartmentRecView = findViewById(R.id.apartmentRecView);
        apartmentRecView.setAdapter(adapter);

        apartmentRecView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Apartment> apartments;
        Bundle bundle = getIntent().getExtras();
        apartments = (ArrayList<Apartment>) bundle.getSerializable("Result search");

        adapter.setApartments(apartments);
        adapter.setStartDate(bundle.getString("Start Date"));
        adapter.setEndDate(bundle.getString("End Date"));


    }
}