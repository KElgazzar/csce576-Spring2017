package com.example.mobile576;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayActivity extends AppCompatActivity {
    @BindView(R.id.textView) TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        ButterKnife.bind(this);

        String date = getIntent().getStringExtra("Date");
        String type = getIntent().getStringExtra("Type");
        String authHeader = getIntent().getStringExtra("AH");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.fitbit.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FitbitInterface service = retrofit.create(FitbitInterface.class);
        Call<ResponseBody> responseCall;

        Log.d("mobile576", type);
        Log.d("mobile576", date);

        switch (type) {
            case "hr":
                responseCall = service.getHeartbeats(authHeader, date);
                break;
            case "steps":
                responseCall = service.getSteps(authHeader, date);
                break;
            case "cb":
                responseCall = service.getCalories(authHeader, date);
                break;
            default:
                responseCall = service.getHeartbeats(authHeader, date);
        }

        Log.d("mobile576", responseCall.request().url().toString());

        responseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String jsonResponseString = response.body().string();
                        Log.d("mobile576", jsonResponseString);
                        JsonParser parser = new JsonParser();
                        JsonObject json = parser.parse(jsonResponseString).getAsJsonObject();

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String data = gson.toJson(json);
                        mTextView.setText(data);
                    } catch (Exception e) {
                        Log.e("mobile576", "Error retrieving data");
                    }
                } else {
                    getSharedPreferences("Mobile576", MODE_PRIVATE).edit().remove("access_token").commit();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please check internet connectivity", Toast.LENGTH_LONG).show();
            }
        });
    }
}
