package com.example.rotravel.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Property implements Parcelable {
    String id;
    String idPlace;
    String idUser;
    int price;
    String name;
    String description;
    String image;

    public Property() {}

    protected Property(Parcel in) {
        id = in.readString();
        idPlace = in.readString();
        idUser = in.readString();
        price = in.readInt();
        name = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public static final Creator<Property> CREATOR = new Creator<Property>() {
        @Override
        public Property createFromParcel(Parcel in) {
            return new Property(in);
        }

        @Override
        public Property[] newArray(int size) {
            return new Property[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(String idPlace) {
        this.idPlace = idPlace;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(idPlace);
        dest.writeString(idUser);
        dest.writeInt(price);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
    }
}
