package com.example.rotravel.HelperClasses;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.Model.User;
import com.example.rotravel.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MadeReservationsAdapter extends RecyclerView.Adapter<MadeReservationsAdapter.ViewHolder> {

    Context context;
    ArrayList<Reservation> reservations;

    public MadeReservationsAdapter(Context context, ArrayList<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_made_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTotalPayment.setText(reservations.get(position).getTotal());
        holder.txtSelectedDate.setText(reservations.get(position).getDate());

        String c = reservations.get(position).getTotalCapacity() + "";
        holder.txtCapacity2.setText(c);
//        holder.txtPhoneUser.setText(user.getPhone());
//        String fullName = user.getFirstName() + " " + user.getLastName();
//        holder.txtNameUser.setText(fullName);


    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTotalPayment;
        TextView txtSelectedDate;
        TextView txtCapacity2;
        TextView txtNameUser;
        TextView txtPhoneUser;
        MaterialButton btnDecline;
        MaterialButton btnAccept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTotalPayment = itemView.findViewById(R.id.txtTotalPayment);
            txtSelectedDate = itemView.findViewById(R.id.txtSelectedDate);
            txtCapacity2 = itemView.findViewById(R.id.txtCapacity2);
            txtNameUser = itemView.findViewById(R.id.txtFullNameUser);
            txtPhoneUser = itemView.findViewById(R.id.txtPhoneUser);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }

        public MaterialButton accept(){ return btnAccept; }

        public MaterialButton decline(){ return  btnDecline; }
    }
}
