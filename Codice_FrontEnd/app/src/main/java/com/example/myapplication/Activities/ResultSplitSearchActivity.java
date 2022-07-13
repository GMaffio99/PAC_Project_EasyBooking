package com.example.myapplication.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Classes.Apartment;
import com.example.myapplication.Classes.MultipleApartmentsStructure;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerView.ApartmentRecViewAdapter;
import com.example.myapplication.RecyclerView.MultipleApartmentRecViewAdapter;

import java.util.ArrayList;

public class ResultSplitSearchActivity extends AppCompatActivity {

    private RecyclerView apartmentRecView;
    private MultipleApartmentRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        adapter = new MultipleApartmentRecViewAdapter(this);
        apartmentRecView = findViewById(R.id.apartmentRecView);
        apartmentRecView.setAdapter(adapter);

        apartmentRecView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<MultipleApartmentsStructure> apartments;
        Bundle bundle = getIntent().getExtras();
        apartments = (ArrayList<MultipleApartmentsStructure>) bundle.getSerializable("Result search");

        adapter.setApartments(apartments);
        adapter.setStartDate(bundle.getString("Start Date"));
        adapter.setEndDate(bundle.getString("End Date"));


    }
}