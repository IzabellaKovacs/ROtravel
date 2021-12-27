package com.example.rotravel.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rotravel.Model.Place;
import com.example.rotravel.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllCitiesRecViewAdapter extends FirebaseRecyclerAdapter<Place, AllCitiesRecViewAdapter.ViewHolder> {

    ArrayList<Place> places;

    public AllCitiesRecViewAdapter(FirebaseRecyclerOptions<Place> place) {
      super(place);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Place model) {
        holder.txtPlaceName.setText(model.getName());

        Picasso.get().load("image").into(holder.imgPlace);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlace;
        TextView txtPlaceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPlace = itemView.findViewById(R.id.imgPlace);
            txtPlaceName = itemView.findViewById(R.id.txtPlaceName);

        }
    }
}
