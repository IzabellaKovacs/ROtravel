package com.example.rotravel.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.rotravel.Model.Place;
import com.example.rotravel.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class AllPlacesAdapter extends ArrayAdapter<Place> {
   private final Context context;
   private final List<Place> places;
   private final int layoutResID;

    public AllPlacesAdapter(@NonNull Context context, int layoutResID, @NonNull List<Place> places) {
        super(context, layoutResID, places);
        this.context = context;
        this.layoutResID = layoutResID;
        this.places = places;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder itemHolder;
        View view = convertView;

        if(view == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            itemHolder = new ViewHolder();

            view = inflater.inflate(layoutResID, parent, false);
            itemHolder.imgPlace = view.findViewById(R.id.imgPlace);
            itemHolder.txtPlaceName = view.findViewById(R.id.txtPlaceName);
            view.setTag(itemHolder);

        }else{
            itemHolder = (ViewHolder) view.getTag();
        }

        final Place place = places.get(position);

        itemHolder.txtPlaceName.setText(place.getName());
        Picasso.get().load(place.getImage()).into(itemHolder.imgPlace);


        return view;
    }

    private static class ViewHolder {
        ImageView imgPlace;
        TextView txtPlaceName;
    }

}


