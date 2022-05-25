package com.example.rotravel.HelperClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.Model.AcceptedReservation;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.Model.User;
import com.example.rotravel.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MadeReservationsAdapter extends RecyclerView.Adapter<MadeReservationsAdapter.ViewHolder> {

    Context context;
    ArrayList<Reservation> reservations;
    ArrayList<User> users;

    public MadeReservationsAdapter(Context context, ArrayList<Reservation> reservations, ArrayList<User> users) {
        this.context = context;
        this.reservations = reservations;
        this.users = users;
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

        for(User user : users){
            if(reservations.get(position).getIdUser().equals(user.getId())){
                holder.txtPhoneUser.setText(user.getPhone());
                String fullName = user.getFirstName() + " " + user.getLastName();
                holder.txtNameUser.setText(fullName);
            }
        }

        DatabaseReference databaseReservations = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Reservations");
        DatabaseReference databaseAcceptedReservations = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("AcceptedReservations");

        Reservation reservation = reservations.get(position);

        holder.decline().setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to decline this reservation?");
            builder.setPositiveButton("Yes", (dialogInterface, i) ->
                    databaseReservations.child(reservations.get(position).getId()).removeValue());
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.show();

        });

        holder.accept().setOnClickListener(v -> {
            final AcceptedReservation newReservation = new AcceptedReservation();

            newReservation.setId(UUID.randomUUID().toString());
            newReservation.setIdProperty(reservation.getIdProperty());
            newReservation.setIdUser(reservation.getIdUser());
            newReservation.setDate(reservation.getDate());
            newReservation.setFirst(reservation.getFirst());
            newReservation.setLast(reservation.getLast());
            newReservation.setTotal(reservation.getTotal());
            newReservation.setTotalCapacity(reservation.getTotalCapacity());

            databaseAcceptedReservations.child(newReservation.getId()).setValue(newReservation);
            databaseReservations.child(reservation.getId()).removeValue();
        });
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
            btnDecline = itemView.findViewById(R.id.btnDecline);
            btnAccept = itemView.findViewById(R.id.btnAccept);
        }

        public MaterialButton decline(){ return  btnDecline; }

        public MaterialButton accept(){ return btnAccept; }
    }
}
