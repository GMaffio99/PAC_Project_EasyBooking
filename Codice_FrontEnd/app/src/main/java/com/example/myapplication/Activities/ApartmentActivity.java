package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.API.ReservationAPI;
import com.example.myapplication.Classes.Apartment;
import com.example.myapplication.R;
import com.example.myapplication.Classes.Reservation;
import com.example.myapplication.SessionManager.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApartmentActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    ImageView image;
    TextView descriptionApartment, locationApartment, textNumGuests, numGuest,textCostNight, costNight;
    Button btnBooking;
    GoogleMap map;
    String startDate, endDate;
    Bundle bundle;
    SessionManager sessionManager;
    HashMap<String,String> userDetails;
    ReservationAPI reservationAPI;
    Apartment apartment;

    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);

        initViews();

        setData(apartment);

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.baseUrl)).addConverterFactory(GsonConverterFactory.create()).build();

                reservationAPI = retrofit.create(ReservationAPI.class);

                if (sessionManager.checkLogin()==false) {
                    Toast.makeText(ApartmentActivity.this,"Please login in order to book the apartment.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ApartmentActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
                else {

                    reservationAPI.insertReservation(new Reservation(startDate, endDate, apartment.getId(), userDetails.get(SessionManager.KEY_ID)),apartment.getId()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(!response.isSuccessful()) {
                                Toast.makeText(ApartmentActivity.this, "Apartment already booked, select another period.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ApartmentActivity.this,"Apartment booked correctly! Good holidays!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ApartmentActivity.this, SearchActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                            Toast.makeText(ApartmentActivity.this,"Something gone wrong, please make another attempt.", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });

    }

    private void setData(Apartment apartment) {

        descriptionApartment.setText(apartment.getDescription());
        locationApartment.setText(apartment.getLocation());
        numGuest.setText(String.valueOf(apartment.getNumGuests()));
        costNight.setText(String.valueOf(apartment.getPricePerNight()).concat("€"));
        Glide.with(ApartmentActivity.this).asBitmap().load(apartment.getImageUrl()).into(image);


    }

    private void initViews() {

        image = findViewById(R.id.apartmentImage);
        descriptionApartment = findViewById(R.id.descriptionApartement);
        locationApartment = findViewById(R.id.locationApartment);
        numGuest = findViewById(R.id.numGuest);
        textNumGuests = findViewById(R.id.textNumGuests);
        textCostNight = findViewById(R.id.textCostNight);
        costNight = findViewById(R.id.costNight);
        btnBooking = findViewById(R.id.btnBooking);
        sessionManager = new SessionManager(this);
        userDetails = sessionManager.getUserDetailsFromSession();
        bundle = getIntent().getExtras();
        apartment = bundle.getParcelable("Requested apartment");
        startDate = bundle.getString("Start Date");
        endDate = bundle.getString("End Date");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;

        try {
            List<Address> addressList = geocoder.getFromLocationName(apartment.getLocation(), 1);
            Address address = addressList.get(0);

            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Apartment location");
            map.addMarker(markerOptions);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}