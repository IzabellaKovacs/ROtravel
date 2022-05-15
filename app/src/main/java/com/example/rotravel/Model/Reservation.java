package com.example.rotravel.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Reservation implements Parcelable{
    String id;
    String idProperty;
    String idUser;
    String date;
    String total;
    Long first;
    Long last;
    int totalCapacity;

    public Reservation(){ }

    protected Reservation(Parcel in) {
        id = in.readString();
        idProperty = in.readString();
        idUser = in.readString();
        date = in.readString();
        total = in.readString();
        totalCapacity = in.readInt();
        first = in.readLong();
        last = in.readLong();
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getLast() {
        return last;
    }

    public void setLast(Long last) {
        this.last = last;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(idProperty);
        parcel.writeString(idUser);
        parcel.writeString(date);
        parcel.writeString(total);
        parcel.writeInt(totalCapacity);
        parcel.writeLong(first);
        parcel.writeLong(last);
    }
}

