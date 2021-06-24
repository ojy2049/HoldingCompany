package com.example.app;

import com.google.gson.annotations.SerializedName;


public class Result {
    @SerializedName("token")
    private String token;

    @SerializedName("name")
    private String name;
    @SerializedName("broker_id")
    private String broker_id;

    @SerializedName("city_address")
    private String city_address;

    @SerializedName("gu_address")
    private String gu_address;

    @SerializedName("dong_address")
    private String dong_address;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("real_estate")
    private String real_estate;

    @SerializedName("email")
    private String email;

    @SerializedName("uid")
    private String uid;

    @SerializedName("message")
    private String message;

    @SerializedName("broker_uid")
    private String broker_uid;

    @SerializedName("reviewer_uid")
    private String reviewer_uid;

    @SerializedName("rating")
    private float rating;



    public String toString() {
        return "result{" +
                "name"+name+
                "city_address"+city_address+
                "dong_address"+dong_address+
                "gu_address"+gu_address+
                '}';
    }

    public Result(String name, String city_address, String gu_address, String dong_address, float rating)
    {
        this.name = name;
        this.city_address = city_address;
        this.gu_address = gu_address;
        this.dong_address = dong_address;
        this.rating=rating;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBroker_id() {
        return broker_id;
    }

    public void setBroker_id(String broker_id) {
        this.broker_id = broker_id;
    }

    public String getCity_address() {
        return city_address;
    }

    public void setCity_address(String city_address) {
        this.city_address = city_address;
    }

    public String getGu_address() {
        return gu_address;
    }

    public void setGu_address(String gu_address) {
        this.gu_address = gu_address;
    }

    public String getDong_address() {
        return dong_address;
    }

    public void setDong_address(String dong_address) {
        this.dong_address = dong_address;
    }

    public Result(){
        this.name=name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReal_estate() {
        return real_estate;
    }

    public void setReal_estate(String real_estate) {
        this.real_estate = real_estate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getBroker_uid() {
        return broker_uid;
    }

    public void setBroker_uid(String broker_uid) {
        this.broker_uid = broker_uid;
    }

    public String getReviewer_uid() {
        return reviewer_uid;
    }

    public void setReviewer_uid(String reviewer_uid) {
        this.reviewer_uid = reviewer_uid;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
