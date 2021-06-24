package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Searchview extends AppCompatActivity {

    private ArrayList<Result> dataList = new ArrayList<>();;
    Searchadapter sadapter;

    RecyclerView recyclerView;
    LinearLayoutManager manager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview);

        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        PostData();


    }

    public void PostData() {

        Intent getintent = getIntent();
        String Region = getintent.getStringExtra("Region");
        retrofitservice retrofit = new retrofitservice();
        Call<List<Result>> call = retrofit.api.getGet(Region);
        System.out.println(Region);
        call.enqueue(new Callback<List<Result>>() {
            @Override
            public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                if (response.isSuccessful()) {
                    List<Result> list = response.body();
                    for (Result result : list) {
                        dataList.add(new Result(result.getName(), result.getCity_address(), result.getGu_address(), result.getDong_address(),result.getRating()));
                    }

                    System.out.println(dataList);
                    RecyclerView recyclerView = findViewById(R.id.recyclerview1);
                    recyclerView.setLayoutManager(manager); // LayoutManager 등록
                    sadapter = new Searchadapter(dataList);
                    recyclerView.setAdapter(sadapter);
                }
                else
                    System.out.println("오류1");
            }

            @Override
            public void onFailure(Call<List<Result>> call, Throwable t) {
                System.out.println("오류2");
            }
        });
    }
}
