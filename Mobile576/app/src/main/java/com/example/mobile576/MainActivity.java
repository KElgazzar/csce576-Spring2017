package com.example.mobile576;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.btn_login) Button loginButton;
    @BindView(R.id.btn_register) Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBrowserForLogin();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBrowserForRegistering();
            }
        });
    }

    protected void loadBrowserForLogin() {
        String url = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=228J26&scope=activity%20heartrate";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        finish();
    }

    protected void loadBrowserForRegistering() {
        String url = "https://www.fitbit.com/signup";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        finish();
    }
}
