package com.example.student.miniproject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MenuService {
    String URL = "http://70.12.245.130/Moyeora/";

    @POST("myinfo.myr")
    @FormUrlEncoded
    Call<Cust> myinfo(@Field("id") String id);

    @POST("getcustom.myr")
    @FormUrlEncoded
    Call<Cust> getcustom(@Field("id") String id);

    @POST("customize.myr")
    @FormUrlEncoded
    Call<Result> customize(@Field("id") String id, @Field("light") String light, @Field("temp") String temp,@Field("seat") String seat, @Field("car") String car);

    @POST("reservationCheck.myr")
    @FormUrlEncoded
    Call<Order> reservationCheck(@Field("id") String id);

    @POST("mydelete.myr")
    @FormUrlEncoded
    Call<Result> mydelete(@Field("id") String id);

    @POST("reservationcancel.myr")
    @FormUrlEncoded
    Call<Result> reservationcancel(@Field("id") String id);

    @POST("passwordchange.myr")
    @FormUrlEncoded
    Call<Result> passwordchange(@Field("id") String id, @Field("password") String password);
}