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

import com.example.rotravel.Model.Property;
import com.example.rotravel.R;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserPropertiesRecViewAdapter extends RecyclerView.Adapter<UserPropertiesRecViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Property property);
        void onItemClickRequests(Property property);
    }

    Context context;
    ArrayList<Property> properties;
    UserPropertiesRecViewAdapter.OnItemClickListener listener;

    public UserPropertiesRecViewAdapter(Context context, ArrayList<Property> properties, UserPropertiesRecViewAdapter.OnItemClickListener listener) {
        this.context = context;
        this.properties = properties;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_property, parent, false);
        return new UserPropertiesRecViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = properties.get(position);
        holder.txtPropertyName.setText(property.getName());
//        holder.getDetails().setOnClickListener(v -> listener.onItemClick(property));
        holder.getRequests().setOnClickListener(v -> listener.onItemClickRequests(property));
        holder.getReservations().setOnClickListener(v -> listener.onItemClick(property));
        Picasso.get().load(property.getImage()).into(holder.imgProperty);

    }

    @Override
    public int getItemCount() {
        return properties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProperty;
        TextView txtPropertyName;
        MaterialButton btnEdit;
        MaterialButton btnCheckRequests;
        MaterialButton btnCheckReservations;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProperty = itemView.findViewById(R.id.imgProperty);
            txtPropertyName = itemView.findViewById(R.id.txtPropertyName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnCheckReservations = itemView.findViewById(R.id.btnCheckReservations);
            btnCheckRequests = itemView.findViewById(R.id.btnCheckRequests);
        }

        public MaterialButton getEdit(){
            return btnEdit;
        }

        public MaterialButton getReservations(){
            return btnCheckReservations;
        }

        public MaterialButton getRequests() { return btnCheckRequests; }
    }


}
