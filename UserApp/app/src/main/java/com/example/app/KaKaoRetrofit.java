package com.example.app;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KaKaoRetrofit {

    private static final String URL = "https://dapi.kakao.com/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public KaKaoApi kakaoapi = retrofit.create(KaKaoApi.class);
}