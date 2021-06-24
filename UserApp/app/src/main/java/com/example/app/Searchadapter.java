package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Searchadapter extends RecyclerView.Adapter<SearchViewHolder> {

    private ArrayList<Result> myDataList = null;
    Searchadapter(ArrayList<Result> dataList)
    {
        myDataList = dataList;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //뷰홀더 객체 생성
        View view = inflater.inflate(R.layout.layout, parent, false);
        SearchViewHolder viewHolder = new SearchViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(SearchViewHolder viewHolder, int position)
    {
        //ViewHolder가 관리하는 View에 position에 해당하는 데이터 바인딩
        viewHolder.name.setText( myDataList.get(position).getName());
        viewHolder.city_address.setText(myDataList.get(position).getCity_address());
        viewHolder.dong_address.setText(myDataList.get(position).getDong_address());
        viewHolder.gu_address.setText(myDataList.get(position).getGu_address());
        viewHolder.ratingbar.setRating(myDataList.get(position).getRating());
        viewHolder.r_button.setOnClickListener(v -> {
            System.out.println(myDataList.get(position).getName());
            Context context = v.getContext();
            Intent intent =new Intent(v.getContext(), ChatActivity.class);
            intent.putExtra("frienduid","gqbvMUtOk6QNFNKVlkIofCyRi6C2");
            context.startActivity(intent);



            /*retrofitservice retrofit = new retrofitservice();
            Call<Result> call = retrofit.api.getfcm();
            call.enqueue(new Callback<Result>(){
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    System.out.println("성공");

                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    System.out.println("실패");
                }
            });*/
        });

    }

    @Override
    public int getItemCount()
    {
        //Adapter가 관리하는 전체 데이터 개수 반환
        return myDataList.size();
    }
}