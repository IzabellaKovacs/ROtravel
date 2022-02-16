package com.example.rotravel.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.Model.Place;
import com.example.rotravel.R;
import com.example.rotravel.TripReserveActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllCitiesRecViewAdapter extends RecyclerView.Adapter<AllCitiesRecViewAdapter.ViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(Place place);
    }

    Context context;
    ArrayList<Place> places;
    OnItemClickListener listener;

    public AllCitiesRecViewAdapter(Context context, ArrayList<Place> places, OnItemClickListener listener) {
        this.context = context;
        this.places = places;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.txtPlaceName.setText(place.getName());
        Picasso.get().load(place.getImage()).into(holder.imgPlace);
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(place);
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView parent;
        ImageView imgPlace;
        TextView txtPlaceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            imgPlace = itemView.findViewById(R.id.imgPlace);
            txtPlaceName = itemView.findViewById(R.id.txtPlaceName);
        }
    }
}
