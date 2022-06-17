package com.example.rotravel.HelperClasses;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.Model.AcceptedReservation;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AllReservedPropertiesAdapter extends RecyclerView.Adapter<AllReservedPropertiesAdapter.ViewHolder> {

    Context context;
    ArrayList<Property> properties;
    ArrayList<AcceptedReservation> reservations;

    public AllReservedPropertiesAdapter(Context context, ArrayList<Property> properties, ArrayList<AcceptedReservation> reservations) {
        this.context = context;
        this.properties = properties;
        this.reservations = reservations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = properties.get(position);
        AcceptedReservation reservation = reservations.get(position);
            if(property.getId().equals(reservation.getIdProperty())) {
                holder.txtReservedPropertyDate.setText(reservation.getDate());
                holder.txtReservedPropertyName.setText(property.getName());

                holder.deleteButton().setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to cancel reservation?" );
                    builder.setPositiveButton("Yes", (dialogInterface, i) ->
                            FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app")
                                    .getReference("AcceptedReservations")
                                    .child(reservation.getId())
                                    .removeValue());
                    builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                    builder.show();

                });
            }
    }


    @Override
    public int getItemCount() {
        return properties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView parent;
        TextView txtReservedPropertyName;
        TextView txtReservedPropertyDate;
        ImageView btnDeleteReservation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            txtReservedPropertyName = itemView.findViewById(R.id.txtReservedPropertyName);
            txtReservedPropertyDate = itemView.findViewById(R.id.txtReservedPropertyDate);
            btnDeleteReservation = itemView.findViewById(R.id.btnDeleteReservation);
        }

        public ImageView deleteButton(){ return btnDeleteReservation; }
    }
}
