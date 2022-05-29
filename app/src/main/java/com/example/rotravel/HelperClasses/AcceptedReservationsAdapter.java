package com.example.rotravel.HelperClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rotravel.Model.AcceptedReservation;
import com.example.rotravel.Model.User;
import com.example.rotravel.R;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class AcceptedReservationsAdapter extends RecyclerView.Adapter<AcceptedReservationsAdapter.ViewHolder> {
    Context context;
    ArrayList<AcceptedReservation> reservations;
    ArrayList<User> users;

    public AcceptedReservationsAdapter(Context context, ArrayList<AcceptedReservation> reservations, ArrayList<User> users) {
        this.context = context;
        this.reservations = reservations;
        this.users = users;
    }

    @NonNull
    @Override
    public AcceptedReservationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_made_reservation, parent, false);
        return new AcceptedReservationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedReservationsAdapter.ViewHolder holder, int position) {
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
        MaterialButton btnAccept;
        MaterialButton btnDecline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTotalPayment = itemView.findViewById(R.id.txtTotalPayment);
            txtSelectedDate = itemView.findViewById(R.id.txtSelectedDate);
            txtCapacity2 = itemView.findViewById(R.id.txtCapacity2);
            txtNameUser = itemView.findViewById(R.id.txtFullNameUser);
            txtPhoneUser = itemView.findViewById(R.id.txtPhoneUser);
            btnDecline = itemView.findViewById(R.id.btnDecline);
            btnAccept = itemView.findViewById(R.id.btnAccept);

            btnAccept.setVisibility(View.GONE);
            btnDecline.setVisibility(View.GONE);
        }

    }
}
