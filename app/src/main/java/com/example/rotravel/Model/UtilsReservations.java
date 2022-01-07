package com.example.rotravel.Model;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class UtilsReservations {
    private static UtilsReservations instance;
    private ArrayList<Reservation> reservations;

    private UtilsReservations() {
        if (reservations == null) {
            reservations = new ArrayList<>();
        }
    }

    public static UtilsReservations getInstance() {
        if(instance == null){
            instance = new UtilsReservations();
        }

        return instance;
    }

    public ArrayList<Reservation> getAllReservations(){ return reservations; }

    public Reservation getReservationId(String id){
        for(Reservation r: reservations){
            if(r.getId().equals(id))
                return r;
        }
        return null;
    }

    public boolean addReservation(Reservation reservation){
        return reservations.add(reservation);
    }
}
