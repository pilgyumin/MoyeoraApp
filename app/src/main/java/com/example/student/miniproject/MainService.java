package com.example.student.miniproject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MainService {
    String URL = "http://70.12.245.130/Moyeora/";

    @POST("dooropen.myr")
    @FormUrlEncoded
    Call<Void> dooropen(@Field("id") String id);

    @POST("opencheck.myr")
    @FormUrlEncoded
    Call<Result> opencheck(@Field("id") String id);

    @POST("doorlock.myr")
    @FormUrlEncoded
    Call<Void> doorlock(@Field("id") String id);


    @POST("mapoff.myr")
    @FormUrlEncoded
    Call<Result> mapoff(@Field("id") String id, @Field("carmap") int carmap);

    @POST("mapoff2.myr")
    @FormUrlEncoded
    Call<Result> mapoff2(@Field("id") int cid, @Field("carmap") int carmap);

    @POST("getcid.myr")
    @FormUrlEncoded
    Call<Result> getcid(@Field("id") String id);

    @POST("checkreceive.myr")
    @FormUrlEncoded
    Call<Result> checkreceive(@Field("id") String id);

    @POST("reservationConfirm.myr")
    @FormUrlEncoded
    Call<Result> reservationconfirm(@Field("id") String id);

    @POST("carreceive.myr")
    @FormUrlEncoded
    Call<Result> carreceive(@Field("id") String id);

    @POST("carreturn.myr")
    @FormUrlEncoded
    Call<Result> carreturn(@Field("id") String id);

    @POST("extend.myr")
    @FormUrlEncoded
    Call<Result> extend(@Field("id") String id, @Field("time") String time);

}