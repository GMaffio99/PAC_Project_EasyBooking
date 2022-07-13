package com.example.myapplication.SupportViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Activities.SearchActivity;
import com.example.myapplication.R;

public class HomePageFragment extends Fragment {

    Button btnStartHolidays;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_homepage, container,false);

        btnStartHolidays = view.findViewById(R.id.btnStartHolidays);
        btnStartHolidays.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), SearchActivity.class));

            }
        });

        return view;

    }
}

