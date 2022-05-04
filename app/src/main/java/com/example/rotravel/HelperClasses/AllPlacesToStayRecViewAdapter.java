package com.example.rotravel.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.Model.Place;
import com.example.rotravel.Model.Property;
import com.example.rotravel.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllPlacesToStayRecViewAdapter extends RecyclerView.Adapter<AllPlacesToStayRecViewAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Property property);
    }

    Context context;
    ArrayList<Property> properties;
    OnItemClickListener listener;

    public AllPlacesToStayRecViewAdapter(Context context, ArrayList<Property> properties, OnItemClickListener listener) {
        this.context = context;
        this.properties = properties;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AllPlacesToStayRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reserve_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPlacesToStayRecViewAdapter.ViewHolder holder, int position) {
        Property property = properties.get(position);
        holder.txtPlaceToStay.setText(property.getName());
        holder.getBtnCheckDetails().setOnClickListener(v -> {
            listener.onItemClick(property);
        });
        Picasso.get().load(property.getImage()).into(holder.imgPlaceToStay);
    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPlaceToStay;
        TextView txtPlaceToStay;
        Button btnCheckDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPlaceToStay = itemView.findViewById(R.id.imgPlaceToStay);
            txtPlaceToStay = itemView.findViewById(R.id.txtPlaceToStay);
            btnCheckDetails = itemView.findViewById(R.id.btnCheckDetails);
        }

        public Button getBtnCheckDetails() {
            return btnCheckDetails;
        }
    }
}
