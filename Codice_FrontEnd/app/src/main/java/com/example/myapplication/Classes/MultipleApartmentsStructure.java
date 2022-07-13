package com.example.myapplication.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class MultipleApartmentsStructure implements Parcelable {

    public static final Creator<MultipleApartmentsStructure> CREATOR = new Creator<MultipleApartmentsStructure>() {
        @Override
        public MultipleApartmentsStructure createFromParcel(Parcel in) {
            return new MultipleApartmentsStructure(in);
        }

        @Override
        public MultipleApartmentsStructure[] newArray(int size) {
            return new MultipleApartmentsStructure[size];
        }
    };
    public int order;
    public int total;
    public Apartment apartment;

    public MultipleApartmentsStructure(int order, int total, Apartment apartment) {
        this.order = order;
        this.total = total;
        this.apartment = apartment;
    }

    protected MultipleApartmentsStructure(Parcel in) {
        order = in.readInt();
        total = in.readInt();
        apartment = in.readParcelable(Apartment.class.getClassLoader());
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(order);
        dest.writeInt(total);
        dest.writeParcelable(apartment, flags);
    }
}
