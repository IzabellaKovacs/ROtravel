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

import com.example.rotravel.Model.Hotel;
import com.example.rotravel.Model.Place;
import com.example.rotravel.R;
import com.example.rotravel.TripReserveActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllPlacesToStayRecViewAdapter extends RecyclerView.Adapter<AllPlacesToStayRecViewAdapter.ViewHolder> {

    Context context;
    ArrayList<Hotel> hotels;

    public AllPlacesToStayRecViewAdapter(Context context, ArrayList<Hotel> hotels) {
        this.context = context;
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public AllPlacesToStayRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reserve_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllPlacesToStayRecViewAdapter.ViewHolder holder, int position) {
        Hotel hotel = hotels.get(position);
        holder.txtPlaceToStay.setText(hotel.getTxtHotelName());
        holder.imgPlaceToStay.setImageResource(hotel.getImgHotel());
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPlaceToStay;
        TextView txtPlaceToStay;
        Button btnCheckDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPlaceToStay = itemView.findViewById(R.id.imgPlaceToStay);
            txtPlaceToStay = itemView.findViewById(R.id.txtPlaceToStay);
            btnCheckDetails = itemView.findViewById(R.id.btnCheckDetails);
        }
    }
}
