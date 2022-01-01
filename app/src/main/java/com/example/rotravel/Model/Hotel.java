package com.example.rotravel.Model;

public class Hotel {
    int imgHotel;
    String txtHotelName;

    public Hotel(int imgHotel, String txtHotelName) {
        this.imgHotel = imgHotel;
        this.txtHotelName = txtHotelName;
    }

    public int getImgHotel() {
        return imgHotel;
    }

    public void setImgHotel(int imgHotel) {
        this.imgHotel = imgHotel;
    }

    public String getTxtHotelName() {
        return txtHotelName;
    }

    public void setTxtHotelName(String txtHotelName) {
        this.txtHotelName = txtHotelName;
    }
}
