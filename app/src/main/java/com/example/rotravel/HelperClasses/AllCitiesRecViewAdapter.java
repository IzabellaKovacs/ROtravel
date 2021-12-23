package com.example.rotravel.HelperClasses;

import android.content.Context;
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

import java.util.ArrayList;

public class AllCitiesRecViewAdapter extends RecyclerView.Adapter<AllCitiesRecViewAdapter.ViewHolder>{

    Context context;
    ArrayList<Place> places = new ArrayList<>();

    public AllCitiesRecViewAdapter(Context context, ArrayList<Place> places) {
        this.context = context;
        this.places = places;
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
        holder.imgPlace.setImageResource(place.getImage());
        holder.txtPlaceName.setText(place.getName());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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
