package com.example.myapplication.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activities.ApartmentActivity;
import com.example.myapplication.Activities.SearchActivity;
import com.example.myapplication.Classes.Apartment;
import com.example.myapplication.Classes.MultipleApartmentsStructure;
import com.example.myapplication.R;

import java.util.ArrayList;

public class MultipleApartmentRecViewAdapter extends RecyclerView.Adapter<MultipleApartmentRecViewAdapter.ViewHolder> {

    private static final String TAGVARIANT = "MultipleApartment";

    private ArrayList<MultipleApartmentsStructure> apartments = new ArrayList<>();
    private String startDate, endDate;
    private Context mContext;
    private ViewHolder holder;
    private int position;

    public MultipleApartmentRecViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_multipleapartments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAGVARIANT, "onBindViewOlder: Called");
        holder.txtNum.setText(String.valueOf(apartments.get(position).getOrder())+"/"+String.valueOf(apartments.get(position).getTotal()));
        holder.txtName.setText(apartments.get(position).getApartment().getDescription());
        holder.txtAddress.setText((apartments.get(position).getApartment().getLocation()));
        holder.txtPrice.setText((apartments.get(position).getApartment().getPricePerNight().toString()).concat("€"));
        Glide.with(mContext).asBitmap().load(apartments.get(position).getApartment().getImageUrl()).into(holder.imgApartment);

        int i = position;

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, apartments.get(i).getOrder() + " apartment.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return apartments.size();
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        notifyDataSetChanged();
    }

    public void setApartments(ArrayList<MultipleApartmentsStructure> apartments) {
        this.apartments = apartments;
        notifyDataSetChanged();
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        private ImageView imgApartment;
        private TextView txtName, txtAddress, txtPrice, txtNumString, txtNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            imgApartment = itemView.findViewById(R.id.imageApartment);
            txtName = itemView.findViewById(R.id.nameApartment);
            txtAddress = itemView.findViewById(R.id.apptAddress);
            txtPrice = itemView.findViewById(R.id.priceNight);
            txtNumString = itemView.findViewById(R.id.textNumApartments);
            txtNum  = itemView.findViewById(R.id.numApartments);
        }
    }
}
