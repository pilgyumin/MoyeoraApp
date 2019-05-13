package com.example.student.miniproject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface LoginService {
    String URL = "http://70.12.245.130/Moyeora/";

    @POST("login.myr")
    @FormUrlEncoded
    Call<Result> getData2(@Field("id") String id, @Field("pw") String pw);

    @POST("idcheck.myr")
    @FormUrlEncoded
    Call<Result> idcheck(@Field("id") String id,@Field("pw") String pw);

    @POST("register.myr")
    @FormUrlEncoded
    Call<Result> register(@Field("id") String id, @Field("pw") String pw,@Field("name") String name, @Field("birthdate") String birthdate,
                         @Field("email") String email, @Field("phone") int phone, @Field("address") String address,@Field("sex") String sex,
                         @Field("fav1") String fav1,@Field("fav2") String fav2, @Field("fav3") String fav3,@Field("favcar") String favcar);

    @POST("reregister.myr")
    @FormUrlEncoded
    Call<Result> reregister(@Field("cust_id") String cust_id, @Field("mtype") String mtype, @Field("moption1") String moption1,
                            @Field("moption2") String moption2,@Field("moption3") String moption3,
                            @Field("moption4") String moption4,@Field("totalprice") int totalprice,
                            @Field("slng") String slng,@Field("slag") String slag,@Field("elng") String elng,
                            @Field("elag") String elag,@Field("stime") String stime,@Field("etime") String etime,@Field("ostatus") String ostatus);

}
