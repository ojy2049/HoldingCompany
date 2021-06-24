package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.LocationViewHolder> {
    Context context;
    ArrayList<KaKao> list;
    SearchView text;
    RecyclerView recyclerview;

    public Adapter(ArrayList<KaKao> items, Context context, SearchView text, RecyclerView recyclerview) {
        this.context = context;
        this.list = items;
        this.text = text;
        this.recyclerview = recyclerview;
    }


    public void clear() {
        list.clear();
    }
    public int getItemCount() {
        return list.size();
    }


    public void addItem(KaKao item) {
        list.add(item);
    }
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }
    public void setText(SearchView text){
        this.text=text;
    }
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        final KaKao model = list.get(position);
        holder.placeNameText.setText(model.getPlaceName());
        holder.addressText.setText(model.getAddressName());
        holder.placeNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text.setQuery(model.getPlaceName(),false);
                recyclerview.setVisibility(View.GONE);
            }
        });

    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameText;
        TextView addressText;

        public LocationViewHolder(@NonNull final View itemView) {
            super(itemView);
            placeNameText = itemView.findViewById(R.id.place_name);
            addressText = itemView.findViewById(R.id.place_address);
        }
    }
}
