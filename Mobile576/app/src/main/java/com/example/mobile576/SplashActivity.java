package com.example.mobile576;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("Mobile576", MODE_PRIVATE);

        Intent intent;

        if (sharedPreferences.getString("access_token", "").isEmpty()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, DataActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
