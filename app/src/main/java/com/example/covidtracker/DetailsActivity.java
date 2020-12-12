package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(0);

        Bundle b = getIntent().getExtras();
        int statePosition = b.getInt("statePosition");
        int districtPosition = b.getInt("districtPosition");
        String jsonResponse = b.getString("response");

        TextView confirmed = (TextView) findViewById(R.id.confirmed);
        TextView active = (TextView) findViewById(R.id.active);
        TextView recovered = (TextView) findViewById(R.id.recovered);
        TextView deceased = (TextView) findViewById(R.id.deceased);

        MyConnectionHelper connectionHelper = new MyConnectionHelper();
        String[] arr = new String[4];

        try {
            arr = connectionHelper.parseJsonDistrictDetails(jsonResponse,statePosition,districtPosition);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        confirmed.setText("Confirmed: "+arr[0]);
        active.setText("Active: "+arr[1]);
        recovered.setText("Recovered: "+arr[2]);
        deceased.setText("Deceased: "+arr[3]);

    }
}