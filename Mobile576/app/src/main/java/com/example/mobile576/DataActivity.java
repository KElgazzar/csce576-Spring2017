package com.example.mobile576;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DataActivity extends AppCompatActivity {
    @BindView(R.id.btn_hr) Button hrButton;
    @BindView(R.id.btn_steps) Button stepsButton;
    @BindView(R.id.btn_cb) Button cbButton;
    @BindView(R.id.btn_date) Button dateButton;
    @BindView(R.id.btn_logout) Button logoutButton;
    String access_token;
    static String type, authHeader, date = "";
    Context context;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        ButterKnife.bind(this);

        context = this;

        if (getIntent().getData() != null) {
            String path = getIntent().getDataString();
            path = path.replace("https://mobile576.co.in/#access_token=", "");

            access_token = path.substring(0, path.indexOf("&"));

            sharedPreferences = getSharedPreferences("Mobile576", MODE_PRIVATE);
            sharedPreferences.edit().putString("access_token", access_token).commit();
        } else {
            sharedPreferences = getSharedPreferences("Mobile576", MODE_PRIVATE);
            access_token = sharedPreferences.getString("access_token", "");
        }

        authHeader = "Bearer " + access_token;

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(), "datepicker");
            }
        });

        hrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.isEmpty()) {
                    askToSetDate();
                    return;
                }

                showData("hr", date);
            }
        });

        stepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.isEmpty()) {
                    askToSetDate();
                    return;
                }

                showData("steps", date);
            }
        });

        cbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.isEmpty()) {
                    askToSetDate();
                    return;
                }

                showData("cb", date);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().remove("access_token").commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    static void setDate(String date) {
        DataActivity.date = date;
    }

    void askToSetDate() {
        Toast.makeText(getApplicationContext(), "Please set date first", Toast.LENGTH_LONG).show();
    }

    void showData(String type, String date) {
        Intent intent = new Intent(this, DisplayActivity.class);
        intent.putExtra("Date", date);
        intent.putExtra("Type", type);
        intent.putExtra("AH", authHeader);
        startActivity(intent);
    }

    public static class DatePickerFragment extends DialogFragment
                                implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getContext(), this, 2017, 4, 1);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String d, m;

            if (day < 10) {
                d = "0" + String.valueOf(day);
            } else {
                d = String.valueOf(day);
            }

            if (month < 10) {
                m = "0" + String.valueOf(month + 1);
            } else {
                m = String.valueOf(month);
            }

            String date = String.valueOf(year) + "-" + m + "-" + d;
            DataActivity.setDate(date);
        }
    }
}
