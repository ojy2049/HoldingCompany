package com.example.app;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by sonchangwoo on 2017. 1. 1..
 */

public interface ResApi {

    @FormUrlEncoded
    @POST("login")
    Call<Result> getPosts(@Field("token") String token,
                          @Field("uid") String uid);

    @FormUrlEncoded
    @POST("broker")
    Call<List<Result>> getGet(@Field("gu_address") String gu_address);

    @GET("fcm")
    Call<Result> getfcm();

    @FormUrlEncoded
    @POST("register")
    Call<Result> getRegister(@Field("user") String user,
                             @Field("real_estate") String real_estate,
                             @Field("phone") String phone,
                             @Field("address") String address,
                             @Field("email") String email);

    @FormUrlEncoded
    @POST("call")
    Call<Result> getCall(@Field("uid") String uid);

    @FormUrlEncoded
    @POST("rating")
    Call<Result> rating(@Field("broker_uid") String broker_uid,
                        @Field("reviewer_uid") String reviewer_uid,
                        @Field("rating") float rating);


}