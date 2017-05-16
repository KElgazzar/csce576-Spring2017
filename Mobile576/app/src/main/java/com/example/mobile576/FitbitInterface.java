package com.example.mobile576;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

interface FitbitInterface {
    @GET("1/user/-/activities/heart/date/{date}/1d/1min.json")
    Call<ResponseBody> getHeartbeats(@Header("Authorization") String authHeader, @Path("date") String date);

    @GET("1/user/-/activities/calories/date/{date}/1d.json")
    Call<ResponseBody> getCalories(@Header("Authorization") String authHeader, @Path("date") String date);

    @GET("1/user/-/activities/steps/date/{date}/1d.json")
    Call<ResponseBody> getSteps(@Header("Authorization") String authHeader, @Path("date") String date);
}
