package com.example.rotravel.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.R;

import java.util.ArrayList;


// TO DO: inflate list with reservations

public class AllReservedPropertiesAdapter extends RecyclerView.Adapter<AllReservedPropertiesAdapter.ViewHolder> {

    Context context;
    ArrayList<Property> properties;
    ArrayList<Reservation> reservations;

    public AllReservedPropertiesAdapter(Context context, ArrayList<Property> properties, ArrayList<Reservation> reservations) {
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
        for(Reservation r : reservations){
            if(property.getId().equals(r.getIdProperty()))
                holder.txtReservedPropertyDate.setText(r.getDate());
                holder.txtReservedPropertyName.setText(property.getName());
        }
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtReservedPropertyName;
        TextView txtReservedPropertyDate;
        ImageView btnDeleteReservation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtReservedPropertyName = itemView.findViewById(R.id.txtReservedPropertyName);
            txtReservedPropertyDate = itemView.findViewById(R.id.txtReservedPropertyDate);
            btnDeleteReservation = itemView.findViewById(R.id.btnDeleteReservation);
        }
    }
}