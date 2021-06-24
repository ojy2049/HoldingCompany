package com.example.app;

import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SearchViewHolder extends RecyclerView.ViewHolder{
    TextView name;
    TextView city_address;
    TextView gu_address;
    TextView dong_address;
    Button r_button;
    RatingBar ratingbar;

    SearchViewHolder(View itemView)
    {
        super(itemView);

        name = itemView.findViewById(R.id.title);
        city_address = itemView.findViewById(R.id.city_address);
        gu_address = itemView.findViewById(R.id.gu_address);
        dong_address = itemView.findViewById(R.id.dong_address);
        r_button= itemView.findViewById(R.id.r_button);
        ratingbar= itemView.findViewById(R.id.ratingBar);
        ratingbar.setFocusable(false);

    }
}