package com.example.myapplication.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Apartment implements Parcelable {

    public static final Creator<Apartment> CREATOR = new Creator<Apartment>() {
        @Override
        public Apartment createFromParcel(Parcel in) {
            return new Apartment(in);
        }

        @Override
        public Apartment[] newArray(int size) {
            return new Apartment[size];
        }
    };
    public String id;
    public String ownerEmail;
    public int numGuests;

    public String location;
    public Double pricePerNight;
    private String description;
    private String imageUrl;

    protected Apartment(Parcel in) {
        id = in.readString();
        ownerEmail = in.readString();
        description = in.readString();
        numGuests = in.readInt();
        location = in.readString();
        imageUrl = in.readString();
        if (in.readByte() == 0) {
            pricePerNight = null;
        } else {
            pricePerNight = in.readDouble();
        }
    }

    public Apartment(String description, String location, String imageUrl, double pricePerNight) {
        this.id = "0";
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.ownerEmail = null;
        this.pricePerNight = pricePerNight;
        this.numGuests = 0;
    }

    public Apartment(String id, String description, String location, String imageUrl, double pricePerNight, int numGuests) {
        this.id = id;
        this.description = description;
        this.location = location;
        this.imageUrl = imageUrl;
        this.ownerEmail = null;
        this.pricePerNight = pricePerNight;
        this.numGuests = numGuests;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(ownerEmail);
        parcel.writeString(description);
        parcel.writeInt(numGuests);
        parcel.writeString(location);
        parcel.writeString(imageUrl);
        if (pricePerNight == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(pricePerNight);
        }
    }
}
